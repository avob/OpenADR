package com.avob.openadr.server.oadr20b.vtn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventResponseRequiredEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventSimpleValueEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.DemandResponseEventCreateDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventActivePeriodDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventDescriptorDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventTargetDto;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrAppCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.user.OadrUserCreateDto;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.OadrAppService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.google.common.collect.Sets;

@Profile({ "fake-data" })
@Component
public class EmbeddedSqlDatabaseInit implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedSqlDatabaseInit.class);

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	protected static final String VEN_FILE_PREFIX = "ven";
	protected static final String ADMIN_FILE_PREFIX = "admin";
	protected static final String USER_FILE_PREFIX = "user";
	protected static final String APP_FILE_PREFIX = "app";

	@Value("${vtn.custom-cert-folder}")
	private String customCertFolder;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private VenService venService;

	@Resource
	private DemandResponseEventService demandeResponseEventService;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private OadrAppService oadrAppService;

	private List<String> getFilename(String type) {
		File file = new File(customCertFolder);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return name.contains(type) && name.endsWith(".key");
			}
		});
		if (directories == null) {
			return new ArrayList<>();
		}
		return Arrays.asList(directories);
	}

	private List<String> getAdminFilename() {
		return getFilename(ADMIN_FILE_PREFIX);
	}

	private List<String> getUserFilename() {
		return getFilename(USER_FILE_PREFIX);
	}

	private List<String> getAppFilename() {
		return getFilename(APP_FILE_PREFIX);
	}

	private List<String> getVenFilename() {
		return getFilename(VEN_FILE_PREFIX);
	}

	private String getFingerprint(String filename) {
		Path path = Paths.get(customCertFolder + "/" + filename + ".fingerprint");
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {

			String currentLine = reader.readLine();

			return currentLine.trim();

		} catch (IOException ex) {
			ex.printStackTrace(); // handle an exception here
		}
		return null;
	}

	private VenMarketContext saveMarketContextIfMissing(VenMarketContextDto dto) {
		VenMarketContext findOneByName = venMarketContextService.findOneByName(dto.getName());
		if (findOneByName == null) {
			VenMarketContext marketContext = venMarketContextService.prepare(dto);
			findOneByName = venMarketContextService.save(marketContext);
			LOGGER.debug("Create MarketContext: " + findOneByName.getName());
		}

		return findOneByName;
	}

	private VenGroup saveGroupIfMissing(VenGroupDto dto) {
		VenGroup findOneByName = venGroupService.findByName(dto.getName());
		if (findOneByName == null) {
			VenGroup group = venGroupService.prepare(dto);
			findOneByName = venGroupService.save(group);
			LOGGER.debug("Create Group: " + findOneByName.getName());
		}
		return findOneByName;
	}

	private Ven saveVenIfMissing(VenCreateDto dto, Set<VenMarketContext> linkedMarketContext,
			Set<VenGroup> linkedGroup) {
		Ven findOneByName = venService.findOneByUsername(dto.getUsername());
		if (findOneByName == null) {
			Ven prepare = venService.prepare(dto);
			prepare.setVenMarketContexts(linkedMarketContext);
			prepare.setVenGroup(linkedGroup);
			findOneByName = venService.save(prepare);
			LOGGER.debug("Create VEN: " + findOneByName.getUsername());
		}
		return findOneByName;
	}

	private OadrUser saveUserIfMissing(OadrUserCreateDto dto) {
		OadrUser findOneByName = oadrUserService.findByUsername(dto.getUsername());
		if (findOneByName == null) {
			OadrUser prepare = oadrUserService.prepare(dto);
			oadrUserService.save(prepare);
			LOGGER.debug("Create User: " + prepare.getUsername());
		}
		return findOneByName;
	}

	private OadrApp saveAppIfMissing(OadrAppCreateDto dto) {
		OadrApp findOneByName = oadrAppService.findByUsername(dto.getUsername());
		if (findOneByName == null) {
			OadrApp prepare = oadrAppService.prepare(dto);
			oadrAppService.save(prepare);
			LOGGER.debug("Create App: " + prepare.getUsername());
		}
		return findOneByName;
	}

	private DemandResponseEvent saveDemandResponseEventIfMissing(DemandResponseEventCreateDto dto) {
		DemandResponseEvent findByEventId = demandeResponseEventService.create(dto);
		LOGGER.debug("Create DREvent: " + findByEventId.getId());
		return findByEventId;
	}

	private static DemandResponseEventSignalDto getSimple(DemandResponseEventSimpleValueEnum val) {
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(val.getValue());
		signal.setSignalName("SIMPLE");
		signal.setSignalType("level");
		signal.setUnitType("none");
		return signal;
	}

	private static DemandResponseEventSignalDto getElectricityPrice(Float val) {
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(val);
		signal.setSignalName(SignalNameEnumeratedType.ELECTRICITY_PRICE.value());
		signal.setSignalType(SignalTypeEnumeratedType.PRICE.value());
		signal.setUnitType("euro_per_kwh");
		return signal;
	}

	private static DemandResponseEventSignalDto getLoadControl(Float val) {
		DemandResponseEventSignalDto signal = new DemandResponseEventSignalDto();
		signal.setCurrentValue(val);
		signal.setSignalName(SignalNameEnumeratedType.LOAD_CONTROL.value());
		signal.setSignalType(SignalTypeEnumeratedType.X_LOAD_CONTROL_CAPACITY.value());
		signal.setUnitType("none");
		return signal;
	}

	private static DemandResponseEventTargetDto getVenTarget(String username) {
		return new DemandResponseEventTargetDto("ven", username);
	}

	private static DemandResponseEventTargetDto getGroupTarget(VenGroup group) {
		return new DemandResponseEventTargetDto("group", group.getName());
	}

	private static DemandResponseEventDescriptorDto getDescriptor(String eventId, String marketcontextName) {
		DemandResponseEventDescriptorDto demandResponseEventDescriptorDto = new DemandResponseEventDescriptorDto();
		demandResponseEventDescriptorDto.setState(DemandResponseEventStateEnum.ACTIVE);
		demandResponseEventDescriptorDto.setMarketContext(marketcontextName);
		demandResponseEventDescriptorDto.setOadrProfile(DemandResponseEventOadrProfileEnum.OADR20B);
		demandResponseEventDescriptorDto.setResponseRequired(DemandResponseEventResponseRequiredEnum.ALWAYS);
		return demandResponseEventDescriptorDto;
	}

	private static DemandResponseEventActivePeriodDto getActivePeriod(Long start, String duration,
			String notificationDuration) {
		DemandResponseEventActivePeriodDto period = new DemandResponseEventActivePeriodDto();
		period.setStart(start);
		period.setNotificationDuration(notificationDuration);
		period.setDuration(duration);
		period.setNotificationDuration(notificationDuration);
		return period;
	}

	private void genDailyElectricityPriceSignals(String eventPrefix, String marketcontextName,
			List<DemandResponseEventTargetDto> targets, Date start, Date end) {

		String duration = "PT4H";
		String notificationDuration = "P1D";

		String everydayCron = "0 0 4 * * *";
		CronSequenceGenerator generator = new CronSequenceGenerator(everydayCron);

		Date temp = generator.next(start);

		while (temp.before(end)) {
			String eventId = eventPrefix + "_" + format.format(temp);

			DemandResponseEventCreateDto eventDto = new DemandResponseEventCreateDto();
			eventDto.setDescriptor(EmbeddedSqlDatabaseInit.getDescriptor(eventId, marketcontextName));
			eventDto.setActivePeriod(
					EmbeddedSqlDatabaseInit.getActivePeriod(temp.getTime(), duration, notificationDuration));

			eventDto.getTargets().addAll(targets);
			eventDto.getSignals().add(
					EmbeddedSqlDatabaseInit.getSimple(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_HIGH));
			eventDto.getSignals().add(EmbeddedSqlDatabaseInit.getElectricityPrice(10.0F));
			eventDto.setPublished(true);
			saveDemandResponseEventIfMissing(eventDto);

			temp = generator.next(temp);
		}
	}

	private void genLoadControlSignals(String eventPrefix, String marketcontextName,
			List<DemandResponseEventTargetDto> targets, Date start, Date end) {

		String duration = "PT6H";
		String notificationDuration = "P1D";

		String everydayCron = "0 0 6 * * *";
		CronSequenceGenerator generator = new CronSequenceGenerator(everydayCron);

		Date temp = generator.next(start);

		while (temp.before(end)) {
			String eventId = eventPrefix + "_" + format.format(temp);

			DemandResponseEventCreateDto eventDto = new DemandResponseEventCreateDto();
			eventDto.setDescriptor(EmbeddedSqlDatabaseInit.getDescriptor(eventId, marketcontextName));
			eventDto.setActivePeriod(
					EmbeddedSqlDatabaseInit.getActivePeriod(temp.getTime(), duration, notificationDuration));

			eventDto.getTargets().addAll(targets);
			eventDto.getSignals().add(
					EmbeddedSqlDatabaseInit.getSimple(DemandResponseEventSimpleValueEnum.SIMPLE_SIGNAL_PAYLOAD_NORMAL));
			eventDto.getSignals().add(EmbeddedSqlDatabaseInit.getLoadControl(99.0F));
			eventDto.setPublished(true);
			saveDemandResponseEventIfMissing(eventDto);

			temp = generator.next(temp);
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {

		// create fake market contexts
		VenMarketContext oadrMarketContext = saveMarketContextIfMissing(
				new VenMarketContextDto("http://oadr.avob.com", "Avob Test Market Context", "#90CAF9"));

		VenMarketContext marketContext = saveMarketContextIfMissing(
				new VenMarketContextDto("http://MarketContext1", "Test Market Context 1", "#ff8080"));

		// create fake groups
		VenGroup customCaCert = saveGroupIfMissing(new VenGroupDto("CustomCACert"));
		VenGroup group1 = saveGroupIfMissing(new VenGroupDto("Group1"));

		List<DemandResponseEventTargetDto> venTargets = new ArrayList<>();
		List<DemandResponseEventTargetDto> groupTargets = new ArrayList<>();
		groupTargets.add(getGroupTarget(group1));

		// load fake ven from ./cert
		List<String> venFilename = getVenFilename();
		int i = 0;
		if (!venFilename.isEmpty()) {

			for (String filename : venFilename) {
				String[] split = filename.split("\\.");
				String commonName = filename.replace("." + split[split.length - 1], "");
				String fingerprintForVen = getFingerprint(commonName);
				VenCreateDto dto = new VenCreateDto();
				String ven1Username = fingerprintForVen;

				dto.setUsername(ven1Username);
				dto.setAuthenticationType("x509");
				dto.setCommonName(commonName);
				dto.setOadrProfil("20b");
				HashSet<VenGroup> groups = Sets.newHashSet(customCaCert);
				DemandResponseEventTargetDto venTarget = getVenTarget(fingerprintForVen);
				venTargets.add(venTarget);
				if (i == 0) {
					groups.add(group1);
					i++;
				}
				saveVenIfMissing(dto, Sets.newHashSet(oadrMarketContext, marketContext), groups);

			}

			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -7);
			Date start = c.getTime();
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 14);
			Date end = c.getTime();

			// gen fake DR events links to loaded ven
			genDailyElectricityPriceSignals("daily_electricity_price", marketContext.getName(), venTargets, start, end);
			genLoadControlSignals("daily_load_control", oadrMarketContext.getName(), groupTargets, start, end);
		}

		// load fake user from ./cert
		List<String> filenames = getAdminFilename();
		if (!filenames.isEmpty()) {
			for (String filename : filenames) {
				String[] split = filename.split("\\.");
				String commonName = filename.replace("." + split[split.length - 1], "");
				String fingerprint = getFingerprint(commonName);
				OadrUserCreateDto dto = new OadrUserCreateDto();
				dto.setUsername(fingerprint);
				List<String> roles = Arrays.asList("ROLE_ADMIN");
				dto.setRoles(roles);
				saveUserIfMissing(dto);
			}
		}
		filenames = getUserFilename();
		if (!filenames.isEmpty()) {
			for (String filename : filenames) {
				String[] split = filename.split("\\.");
				String commonName = filename.replace("." + split[split.length - 1], "");
				String fingerprint = getFingerprint(commonName);
				OadrUserCreateDto dto = new OadrUserCreateDto();
				dto.setUsername(fingerprint);
				saveUserIfMissing(dto);
			}
		}

		// load fake app from ./cert
		filenames = getAppFilename();
		if (!filenames.isEmpty()) {
			for (String filename : filenames) {
				String[] split = filename.split("\\.");
				String commonName = filename.replace("." + split[split.length - 1], "");
				String fingerprint = getFingerprint(commonName);
				OadrAppCreateDto dto = new OadrAppCreateDto();
				dto.setUsername(fingerprint);
				saveAppIfMissing(dto);
			}
		}

	}
}
