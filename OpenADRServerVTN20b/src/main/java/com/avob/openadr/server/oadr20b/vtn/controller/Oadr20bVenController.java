package com.avob.openadr.server.oadr20b.vtn.controller;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.dto.ReportCapabilityDescriptionDto;
import com.avob.openadr.model.oadr20b.dto.ReportCapabilityDto;
import com.avob.openadr.model.oadr20b.dto.ReportDataDto;
import com.avob.openadr.model.oadr20b.dto.ReportRequestDto;
import com.avob.openadr.model.oadr20b.dto.VenOptDto;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportData;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.VenOptService;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.google.common.collect.Lists;

@RestController
@RequestMapping("/Ven")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class Oadr20bVenController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVenController.class);

	@Resource
	private VenService venService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenOptService venOptService;

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private VenDistributeService venDistributeService;

	@Resource
	private OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	private OtherReportCapabilityDescriptionService otherReportCapabilityDescriptionService;

	@Resource
	private OtherReportRequestService otherReportRequestService;

	@Resource
	private OtherReportDataService otherReportDataService;

	@Resource
	private Oadr20bVTNEiReportService reportService;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@RequestMapping(value = "/{venID}/registerparty/requestReregistration", method = RequestMethod.POST)
	@ResponseBody
	public void registerPartyRequestReregistration(@PathVariable("venID") String venID)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		LOGGER.debug("Request Reregistration VEN: " + venID);

		Ven ven = checkVen(venID);
		OadrRequestReregistrationType build = Oadr20bEiRegisterPartyBuilders
				.newOadr20bRequestReregistrationBuilder(ven.getUsername()).build();

		venDistributeService.distribute(ven, build);
	}

	@RequestMapping(value = "/{venID}/registerparty/cancelPartyRegistration", method = RequestMethod.POST)
	@ResponseBody
	public void registerPartyCancelPartyRegistration(@PathVariable("venID") String venID)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		LOGGER.debug("Cancel registration VEN: " + venID);

		Ven ven = checkVen(venID);
		OadrCancelPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders.newOadr20bCancelPartyRegistrationBuilder(
				UUID.randomUUID().toString(), ven.getRegistrationId(), ven.getUsername()).build();

		venDistributeService.distribute(ven, build);
	}

	@RequestMapping(value = "/{venID}/report/available", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportCapabilityDto> viewOtherReportCapability(@PathVariable("venID") String venID)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		List<OtherReportCapability> findBySource = otherReportCapabilityService.findBySource(ven);

		return oadr20bDtoMapper.mapList(findBySource, ReportCapabilityDto.class);
	}

	@RequestMapping(value = "/{venID}/report/available/description", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportCapabilityDescriptionDto> viewOtherReportCapabilityDescription(
			@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		OtherReportCapability reportCapability = checkOtherReportCapability(reportSpecifierId);

		List<OtherReportCapabilityDescription> desc = otherReportCapabilityDescriptionService
				.findByOtherReportCapability(reportCapability);

		return oadr20bDtoMapper.mapList(desc, ReportCapabilityDescriptionDto.class);
	}

	@RequestMapping(value = "/{venID}/report/available/description/payload", method = RequestMethod.GET)
	@ResponseBody
	public String viewOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId,
			@RequestParam(value = "rid", required = true) String rid)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		OtherReportCapability reportCapability = checkOtherReportCapability(reportSpecifierId);

		OtherReportCapabilityDescription findByOtherReportCapability = otherReportCapabilityDescriptionService
				.findByOtherReportCapabilityAndRid(reportCapability, rid);
		return findByOtherReportCapability.getPayload();
	}

	@RequestMapping(value = "/{venID}/report/available/description/subscribe", method = RequestMethod.POST)
	@ResponseBody
	public void subscribeOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId,
			@RequestParam(value = "rid", required = true) String rid,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "reportBackDuration", required = false) String reportBackDuration)
			throws Oadr20bMarshalException, OadrElementNotFoundException {
		Ven ven = checkVen(venID);
		OtherReportCapability reportCapability = checkOtherReportCapability(reportSpecifierId);

		String[] split = rid.split("\\|");
		List<String> rids = Arrays.asList(split);
		reportService.subscribe(ven, reportCapability, rids, reportBackDuration, granularity, null, null);
	}

	@RequestMapping(value = "/{venID}/report/available/description/subscribeAll", method = RequestMethod.POST)
	@ResponseBody
	public void subscribeAllOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "reportBackDuration", required = false) String reportBackDuration)
			throws Oadr20bMarshalException, OadrElementNotFoundException {
		Ven ven = checkVen(venID);

		String[] split = reportSpecifierId.split(",");
		for (String s : split) {
			OtherReportCapability reportCapability = checkOtherReportCapability(s);
			reportService.subscribe(ven, reportCapability, null, reportBackDuration, granularity, null, null);
		}
	}

	@RequestMapping(value = "/{venID}/report/available/description/removeFromSubscribe", method = RequestMethod.POST)
	@ResponseBody
	public void removeSubscribeOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId,
			@RequestParam(value = "rid", required = true) String rid)
			throws Oadr20bMarshalException, OadrElementNotFoundException {
		Ven ven = checkVen(venID);
		OtherReportCapability reportCapability = checkOtherReportCapability(reportSpecifierId);

		String[] split = rid.split("\\|");
		List<String> rids = Arrays.asList(split);

		reportService.removeFromSubscription(ven, reportCapability, rids);
	}

	@RequestMapping(value = "/{venID}/report/available/description/request", method = RequestMethod.POST)
	@ResponseBody
	public void requestOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId,
			@RequestParam(value = "rid", required = true) String rid,
			@RequestParam(value = "start", required = false) Long start,
			@RequestParam(value = "end", required = false) Long end)
			throws Oadr20bMarshalException, OadrElementNotFoundException {
		Ven ven = checkVen(venID);
		OtherReportCapability reportCapability = checkOtherReportCapability(reportSpecifierId);

		String[] split = rid.split("\\|");
		List<String> rids = Arrays.asList(split);
		reportService.request(ven, reportCapability, rids, start, end);
	}

	@RequestMapping(value = "/{venID}/report/available/description/requestAll", method = RequestMethod.POST)
	@ResponseBody
	public void requestAllOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId,
			@RequestParam(value = "start", required = false) Long start,
			@RequestParam(value = "end", required = false) Long end)
			throws Oadr20bMarshalException, OadrElementNotFoundException {
		Ven ven = checkVen(venID);
		OtherReportCapability reportCapability = checkOtherReportCapability(reportSpecifierId);

		reportService.request(ven, reportCapability, null, start, end);
	}

	@RequestMapping(value = "/{venID}/report/requested", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportRequestDto> viewReportRequest(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = false) String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		List<OtherReportRequest> findBySource = null;
		if (reportSpecifierId == null) {
			findBySource = otherReportRequestService.findBySource(ven);
		} else {
			findBySource = otherReportRequestService.findBySourceAndReportSpecifierId(ven, reportSpecifierId);
		}

		return oadr20bDtoMapper.mapList(findBySource, ReportRequestDto.class);
	}

	@RequestMapping(value = "/{venID}/report/requested/cancelSubscription", method = RequestMethod.POST)
	@ResponseBody
	public void cancelSubscriptionReportRequest(@PathVariable("venID") String venID,
			@RequestParam("reportRequestId") String reportRequestId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);

		String[] split = reportRequestId.split(",");

		for (String s : split) {
			reportService.unsubscribe(ven, s);
		}
	}

	@RequestMapping(value = "/{venID}/report_action/requestRegister", method = RequestMethod.POST)
	@ResponseBody
	public void requestRegisterReport(@PathVariable("venID") String venID)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		LOGGER.debug("Request RegisterReport from VEN: " + venID);

		Ven ven = checkVen(venID);
		reportService.distributeRequestMetadataOadrCreateReportPayload(ven);
	}

	@RequestMapping(value = "/{venID}/report_action/sendRegister", method = RequestMethod.POST)
	@ResponseBody
	public void sendRegisterReport(@PathVariable("venID") String venID)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		LOGGER.debug("Send RegisterReport to VEN: " + venID);

		Ven ven = checkVen(venID);
		reportService.distributeOadrRegisterReport(ven);
	}

	@RequestMapping(value = "/{venID}/report_action/create", method = RequestMethod.POST)
	@ResponseBody
	public void createReport(@PathVariable("venID") String venID, @RequestBody String oadrReport)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		Object unmarshal = jaxbContext.unmarshal(oadrReport, false);
		if (unmarshal instanceof OadrReportRequestType) {
			OadrReportRequestType reportRequest = (OadrReportRequestType) unmarshal;
			OadrCreateReportType build = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder("", ven.getUsername())
					.addReportRequest(reportRequest).build();
			venDistributeService.distribute(ven, build);
		}

	}

	@RequestMapping(value = "/{venID}/report_action/cancel", method = RequestMethod.POST)
	@ResponseBody
	public void cancelReport(@PathVariable("venID") String venID,
			@RequestParam("reportRequestId") String reportRequestId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		boolean reportToFollow = false;
		List<String> reportId = Lists.newArrayList(reportRequestId);
		OadrCancelReportType build = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder("", ven.getUsername(), reportToFollow).addReportRequestId(reportId)
				.build();

		venDistributeService.distribute(ven, build);
	}

	@RequestMapping(value = "/{venID}/report_action/update", method = RequestMethod.POST)
	@ResponseBody
	public void updateReport(@PathVariable("venID") String venID, @RequestBody String oadrReport)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		Object unmarshal = jaxbContext.unmarshal(oadrReport, false);
		if (unmarshal instanceof OadrReportType) {
			OadrReportType report = (OadrReportType) unmarshal;
			OadrUpdateReportType build = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder("", ven.getUsername())
					.addReport(report).build();
			venDistributeService.distribute(ven, build);
		}

	}

	@RequestMapping(value = "/{venID}/opt", method = RequestMethod.GET)
	@ResponseBody
	public List<VenOptDto> venOpt(@PathVariable("venID") String venID,
			@RequestParam(value = "start", required = false) Instant start,
			@RequestParam(value = "end", required = false) Instant end,
			@RequestParam(value = "marketContext", required = false) String marketContextName)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		checkMarketContextExists(marketContextName);

		Long startTimestamp = (start != null) ? start.toEpochMilli() : null;
		Long endTimestamp = (end != null) ? end.toEpochMilli() : null;
		return oadr20bDtoMapper.mapList(
				venOptService.findScheduledOpt(ven.getUsername(), marketContextName, startTimestamp, endTimestamp),
				VenOptDto.class);
	}

	@RequestMapping(value = "/{venID}/opt/resource/{resourceName}", method = RequestMethod.GET)
	@ResponseBody
	public List<VenOptDto> venResourceOpt(@PathVariable("venID") String venID,
			@PathVariable("resourceName") String resourceName,
			@RequestParam(value = "start", required = false) Instant start,
			@RequestParam(value = "end", required = false) Instant end,
			@RequestParam(value = "marketContext", required = false) String marketContextName)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		checkMarketContextExists(marketContextName);
		checkResource(ven, resourceName);

		Long startTimestamp = (start != null) ? start.toEpochMilli() : null;
		Long endTimestamp = (end != null) ? end.toEpochMilli() : null;

		return oadr20bDtoMapper.mapList(venOptService.findResourceScheduledOpt(ven.getUsername(), marketContextName,
				resourceName, startTimestamp, endTimestamp), VenOptDto.class);
	}

	@RequestMapping(value = "/{venID}/report/data/{reportSpecifierId}", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDataDto> viewsReportData(@PathVariable("venID") String venID,
			@PathVariable("reportSpecifierId") String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapability(reportSpecifierId);

		List<OtherReportData> findByReportSpecifierId = otherReportDataService
				.findByReportSpecifierId(reportSpecifierId);
		return oadr20bDtoMapper.mapList(findByReportSpecifierId, ReportDataDto.class);
	}

	@RequestMapping(value = "/{venID}/report/data/{reportSpecifierId}/rid/{rid}", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDataDto> viewsReportData(@PathVariable("venID") String venID,
			@PathVariable("reportSpecifierId") String reportSpecifierId, @PathVariable("rid") String rid)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapabilityDescription(reportSpecifierId, rid);

		return oadr20bDtoMapper.mapList(otherReportDataService.findByReportSpecifierIdAndRid(reportSpecifierId, rid),
				ReportDataDto.class);
	}

	private void checkResource(Ven ven, String resourceName) throws OadrElementNotFoundException {
		VenResource resource = venResourceService.findByVenAndName(ven, resourceName);
		if (resource == null) {
			throw new OadrElementNotFoundException();
		}
	}

	private void checkMarketContextExists(String marketContextName) throws OadrElementNotFoundException {
		if (marketContextName != null) {
			VenMarketContext marketContext = venMarketContextService.findOneByName(marketContextName);
			if (marketContext == null) {
				throw new OadrElementNotFoundException();
			}
		}
	}

	private OtherReportCapability checkOtherReportCapability(String reportSpecifierId)
			throws OadrElementNotFoundException {
		OtherReportCapability otherReportCapability = otherReportCapabilityService
				.findByReportSpecifierId(reportSpecifierId);

		if (otherReportCapability == null) {
			throw new OadrElementNotFoundException();
		}

		return otherReportCapability;
	}

	private Ven checkVen(String venID) throws OadrElementNotFoundException {
		Ven ven = venService.findOneByUsername(venID);
		if (ven == null) {
			throw new OadrElementNotFoundException();
		}
		return ven;
	}

	private OtherReportCapability checkOtherReportCapabilityDescription(String reportSpecifierId, String rid)
			throws OadrElementNotFoundException {
		OtherReportCapability otherReportCapability = checkOtherReportCapability(reportSpecifierId);
		OtherReportCapabilityDescription otherReportCapabilityDescription = otherReportCapabilityDescriptionService
				.findByOtherReportCapabilityAndRid(otherReportCapability, rid);
		if (otherReportCapabilityDescription == null) {
			throw new OadrElementNotFoundException();
		}
		return otherReportCapability;
	}

}
