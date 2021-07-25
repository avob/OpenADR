package com.avob.openadr.server.common.vtn.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportId;
import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalId;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitId;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextBaselineDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextReport;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextReportDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextSignal;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextSignalDao;

@Service
public class VenMarketContextService {

	@Resource
	private VenMarketContextDao venMarketcontextDao;

	@Resource
	private VenMarketContextSignalDao venMarketcontextSignalDao;

	@Resource
	private VenMarketContextReportDao venMarketcontextReportDao;

	@Resource
	private VenMarketContextBaselineDao venMarketContextBaselineDao;

	@Resource
	private KnownSignalService knownSignalService;

	@Resource
	private KnownReportService knownReportService;

	@Resource
	private KnownUnitService knownUnitService;

	public VenMarketContext prepare(VenMarketContextDto dto) {
		VenMarketContext context = new VenMarketContext(dto.getName(), dto.getDescription());
		context.setColor(dto.getColor());
		context.setCreatedTimestamp(System.currentTimeMillis());
		return context;
	}

	public VenMarketContext save(VenMarketContext entity) {

		Set<KnownUnit> units = new HashSet<>();
		Set<KnownSignal> signals = new HashSet<>();
		Set<KnownReport> reports = new HashSet<>();

		if (entity.getBaseline() != null) {
			venMarketContextBaselineDao.save(entity.getBaseline());
		}

		VenMarketContext save = venMarketcontextDao.save(entity);

		if (entity.getSignals() != null) {
			entity.getSignals().forEach(sig -> {
				sig.setVenMarketContext(save);
			});
			venMarketcontextSignalDao.saveAll(entity.getSignals());

			List<KnownSignal> collect = entity.getSignals().stream().map(s -> {
				return signalFrom(s);
			}).collect(Collectors.toList());

			signals.addAll(collect);
			units.addAll(signals.stream().filter(s -> s.getKnownSignalId().getItemBase() != null).map(s -> {
				return new KnownUnit(s.getKnownSignalId().getItemBase());
			}).collect(Collectors.toList()));
		}

		if (entity.getReports() != null) {
			entity.getReports().forEach(report -> {
				report.setVenMarketContext(save);
			});
			venMarketcontextReportDao.saveAll(entity.getReports());

			List<KnownReport> collect = entity.getReports().stream().map(s -> {
				return reportFrom(s);
			}).collect(Collectors.toList());

			reports.addAll(collect);
			units.addAll(reports.stream().filter(r -> r.getKnownReportId().getItemBase() != null).map(r -> {
				return new KnownUnit(r.getKnownReportId().getItemBase());
			}).collect(Collectors.toList()));

		}

		knownUnitService.save(units);
		knownSignalService.save(signals);
		knownReportService.save(reports);

		return save;
	}

	public Iterable<VenMarketContext> findAll() {
		return venMarketcontextDao.findAll();
	}

	public VenMarketContext findOneByName(String name) {
		return venMarketcontextDao.findOneByName(name);
	}

	public Optional<VenMarketContext> findById(Long id) {
		return venMarketcontextDao.findById(id);
	}

	public List<VenMarketContext> findByNameIn(List<String> name) {
		return venMarketcontextDao.findByNameIn(name);
	}

	public void delete(VenMarketContext venMarketContext) {
		venMarketcontextDao.delete(venMarketContext);
	}

	public long count() {
		return venMarketcontextDao.count();
	}

	private KnownUnit unitFrom(ItemBase itemBase) {
		if (itemBase == null) {
			return null;
		}
		KnownUnit unit = new KnownUnit();
		KnownUnitId knownUnitId = new KnownUnitId();
		knownUnitId.setItemDescription(itemBase.getItemDescription());
		knownUnitId.setItemUnits(itemBase.getItemUnits());
		knownUnitId.setXmlType(itemBase.getXmlType());
		knownUnitId.setSiScaleCode(itemBase.getSiScaleCode());
		unit.setKnownUnitId(knownUnitId);
		unit.setAttributes(itemBase.getAttributes());
		return unit;
	}

	private KnownSignal signalFrom(VenMarketContextSignal signal) {
		KnownSignal sig = new KnownSignal();
		KnownSignalId knownSignalId = new KnownSignalId();
		knownSignalId.setSignalName(signal.getSignalName());
		knownSignalId.setSignalType(signal.getSignalType());
		if (signal.getItemBase() != null) {
			knownSignalId.setItemBase(unitFrom(signal.getItemBase()).getKnownUnitId());

		} else {
			knownSignalId.setItemBase(new KnownUnitId());
		}
		sig.setKnownSignalId(knownSignalId);
		return sig;
	}

	private KnownReport reportFrom(VenMarketContextReport report) {
		KnownReport rep = new KnownReport();
		KnownReportId knownReportId = new KnownReportId();
		knownReportId.setReportName(report.getReportName());
		knownReportId.setReportType(report.getReportType());
		knownReportId.setReadingType(report.getReadingType());
		knownReportId.setPayloadBase(report.getPayloadBase());
		if (report.getItemBase() != null) {
			knownReportId.setItemBase(unitFrom(report.getItemBase()).getKnownUnitId());

		} else {
			knownReportId.setItemBase(new KnownUnitId());
		}
		rep.setKnownReportId(knownReportId);
		return rep;
	}

}
