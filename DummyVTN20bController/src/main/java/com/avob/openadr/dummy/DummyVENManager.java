package com.avob.openadr.dummy;

import java.lang.reflect.Type;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.JMSException;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.server.oadrvtn20b.api.GroupControllerApi;
import com.avob.server.oadrvtn20b.api.MarketContextControllerApi;
import com.avob.server.oadrvtn20b.api.Oadr20bVenControllerApi;
import com.avob.server.oadrvtn20b.api.ReportControllerApi;
import com.avob.server.oadrvtn20b.api.VenControllerApi;
import com.avob.server.oadrvtn20b.handler.ApiException;
import com.avob.server.oadrvtn20b.model.OtherReportDataFloatDto;
import com.avob.server.oadrvtn20b.model.OtherReportRequestDto;
import com.avob.server.oadrvtn20b.model.OtherReportRequestDtoCreateSubscriptionDto;
import com.avob.server.oadrvtn20b.model.VenCreateDto;
import com.avob.server.oadrvtn20b.model.VenDto;
import com.avob.server.oadrvtn20b.model.VenMarketContextDto;
import com.avob.server.oadrvtn20b.model.VenReportDto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
public class DummyVENManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyVENManager.class);

	private static final String MARKET_CONTEXT = "DummyMarketContext";

	private static final String MARKET_CONTEXT_DESCRIPTION = "DummyVTN20bController Market Context";

	public static final String OADR_APP_NOTIFICATION_REGISTER_REPORT_TOPIC = "topic.app.notification.registerReport.*";
	public static final String OADR_APP_NOTIFICATION_UPDATE_REPORT_TOPIC_FLOAT = "topic.app.notification.updateReport.float.*";
	public static final String OADR_APP_NOTIFICATION_UPDATE_REPORT_TOPIC_RESOURCESTATUS = "topic.app.notification.updateReport.resourcestatus.*";
	public static final String OADR_APP_NOTIFICATION_UPDATE_REPORT_TOPIC_KEYTOKEN = "topic.app.notification.updateReport.keytoken.*";

	private static final DateTimeFormatter formatter =  DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
	private static final Type floatListType = new TypeToken<ArrayList<OtherReportDataFloatDto>>() {
	}.getType();

	public static final String CONTROLLED_VEN_CERTIFICATES = "oadr.ven.certificate";


	@Value("${" + CONTROLLED_VEN_CERTIFICATES + "}")
	private String controlledVenCertificates;

	@Resource
	private Oadr20bVenControllerApi oadr20bVenControllerApi;

	@Resource
	private VenControllerApi venControllerApi;

	@Resource
	private MarketContextControllerApi marketContextControllerApi;

	@Resource
	private GroupControllerApi groupControllerApi;

	@Resource
	private ReportControllerApi reportControllerApi;

	private Gson gson = new GsonBuilder().create();

	@PostConstruct
	public void init() {

		Long marketcontextId;

		try {
			VenMarketContextDto findMarketContextByNameUsingGET = marketContextControllerApi.findMarketContextByNameUsingGET(MARKET_CONTEXT);
			marketcontextId = findMarketContextByNameUsingGET.getId();
			LOGGER.warn("Ven market context: "+MARKET_CONTEXT+ " is already provisioned");
		} catch (ApiException e) {
			if(e.getCode() != HttpStatus.NOT_FOUND_404) {
				LOGGER.error("Ven market context: "+MARKET_CONTEXT+ " can't be provisioned", e);
				return;
			} else {
				VenMarketContextDto dto = new VenMarketContextDto();
				dto.setName(MARKET_CONTEXT);
				dto.setDescription(MARKET_CONTEXT_DESCRIPTION);
				try {
					VenMarketContextDto createMarketContextUsingPOST = marketContextControllerApi.createMarketContextUsingPOST(dto );
					marketcontextId = createMarketContextUsingPOST.getId();
				} catch (ApiException e1) {
					LOGGER.error("Ven market context: "+MARKET_CONTEXT+ " can't be provisioned", e1);
					return;
				}
			}
		}




		if(marketcontextId == null) {
			LOGGER.error("Ven market context: "+MARKET_CONTEXT+ " can't be provisioned");
			return;
		}

		final Long id = marketcontextId;
		this.getControlledVenCertificates().forEach(crt -> {

			X509Certificate parseCertificate;
			String oadr20bFingerprint;
			String name;
			try {
				parseCertificate = OadrPKISecurity.parseCertificate(crt);
				X500Name x500name = new JcaX509CertificateHolder(parseCertificate).getSubject();
				RDN cn = x500name.getRDNs(BCStyle.CN)[0];

				name = IETFUtils.valueToString(cn.getFirst().getValue());
				oadr20bFingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(parseCertificate);
			} catch (OadrSecurityException | CertificateEncodingException e) {
				LOGGER.error("Ven certificate: "+crt+ " can't be provisioned", e);
				return;
			}

			try {
				VenDto findVenByUsernameUsingGET = venControllerApi.findVenByUsernameUsingGET(oadr20bFingerprint);
				LOGGER.warn("Ven certificate: "+crt+" is already provisioned");
				if(findVenByUsernameUsingGET.getRegistrationId() != null) {
					oadr20bVenControllerApi.requestRegisterReportUsingPOST(oadr20bFingerprint);
				}
				return;
			} catch (ApiException e) {
				if(e.getCode() != HttpStatus.NOT_FOUND_404) {
					LOGGER.error("Ven certificate: "+crt+ " can't be provisioned", e);
					return;
				} else {
					try {
						VenCreateDto dto = new VenCreateDto();
						dto.setHttpPullModel(false);
						dto.setTransport("http");
						dto.setPushUrl("https://"+name);
						dto.setOadrProfil("20b");
						dto.setCommonName(name);
						dto.setOadrName(name);
						dto.setUsername(oadr20bFingerprint);	
						dto.setAuthenticationType("x509");
						venControllerApi.createVenUsingPOST(dto );

					} catch (ApiException  e1) {
						LOGGER.error("Ven certificate: "+crt+ " can't be provisioned", e1);
						return;
					}
				}
			}

			try {
				venControllerApi.addMarketContextToVenUsingPOST(id, oadr20bFingerprint);
			} catch (ApiException e) {
				LOGGER.error("Market context id :"+id+" can't be associated with VEN: "+oadr20bFingerprint);
			}
		});
	}



	@JmsListener(destination = DummyVENManager.OADR_APP_NOTIFICATION_REGISTER_REPORT_TOPIC)
	public void onRegisterReportMessage(final Message<String> message) throws JMSException {
		VenReportDto fromJson = gson.fromJson(message.getPayload(), VenReportDto.class);
		subscribe(fromJson);

	}


	@JmsListener(destination = DummyVENManager.OADR_APP_NOTIFICATION_UPDATE_REPORT_TOPIC_FLOAT)
	public void onUpdateReportFloatMessage(final Message<String> message) throws JMSException {
		List<OtherReportDataFloatDto> fromJson = gson.fromJson(message.getPayload(), floatListType);
		fromJson.forEach(updateReport -> {
			Instant ofEpochMilli = Instant.ofEpochMilli(updateReport.getStart());

			OffsetDateTime ofInstant = OffsetDateTime.ofInstant(ofEpochMilli, ZoneId.systemDefault());
			LOGGER.info(String.format("%s - %s %s %s", formatter.format(ofInstant), updateReport.getReportSpecifierId(), updateReport.getRid(), String.valueOf(updateReport.getValue())));
		});

	}

	private void subscribe(VenReportDto venReport) {

		List<OtherReportRequestDto> viewReportRequestUsingGET;
		try {
			viewReportRequestUsingGET = oadr20bVenControllerApi.viewReportRequestUsingGET(venReport.getUsername(), null, null);
			viewReportRequestUsingGET.forEach(request -> {
				try {
					oadr20bVenControllerApi.cancelReportUsingPOST(request.getReportRequestId(), venReport.getUsername());
				} catch (ApiException e) {
					LOGGER.error("Can't subcribe", e);
				}

			});
		} catch (ApiException e) {
			LOGGER.error("Can't subcribe", e);
		}



		List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions = new ArrayList<>();
		venReport.getCapabilities().forEach(cap -> {
			OtherReportRequestDtoCreateSubscriptionDto sub = new OtherReportRequestDtoCreateSubscriptionDto();
			sub.setReportSpecifierId(cap.getReportSpecifierId());
			sub.setGranularity("PT10S");
			sub.setReportBackDuration("PT1M");
			Map<String, Boolean> rids = new HashMap<>();
			cap.getDescriptions().forEach(desc -> {
				rids.put(desc.getRid(), true);
			});
			sub.setRid(rids);
			subscriptions.add(sub);
		});


		try {
			oadr20bVenControllerApi.subscribeOtherReportCapabilityDescriptionRidUsingPOST(subscriptions, venReport.getUsername());
		} catch (ApiException e) {
			LOGGER.error("Can't subcribe", e);
		}
	}

	private List<String> getControlledVenCertificates() {
		// load coma separated trust certificate list
		if (controlledVenCertificates != null) {
			return Arrays.asList(controlledVenCertificates.split(","));
		} else {
			return new ArrayList<>();
		}
	}

}
