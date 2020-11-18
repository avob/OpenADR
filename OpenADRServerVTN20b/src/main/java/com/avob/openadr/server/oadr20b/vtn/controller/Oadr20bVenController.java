package com.avob.openadr.server.oadr20b.vtn.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.user.AbstractUserDao;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOptDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloat;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloatDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyToken;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyTokenDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatus;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatusDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateSubscriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifier;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierSearchCriteria;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierSpecification;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.VenOptService;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataFloatService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataKeyTokenService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataPayloadResourceStatusService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.google.common.collect.Lists;

@CrossOrigin
@RestController
@RequestMapping("/Ven")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVICE_MANAGER')")
public class Oadr20bVenController {

	private static final String PAGINATION_SIZE_DEFAULT = "20";

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
	private OtherReportRequestSpecifierDao otherReportRequestSpecifierDao;

	@Resource
	private OtherReportDataFloatService otherReportDataService;

	@Resource
	private OtherReportDataPayloadResourceStatusService otherReportDataPayloadResourceStatusService;

	@Resource
	private OtherReportDataKeyTokenService otherReportDataKeyTokenService;

	@Resource
	private Oadr20bVTNEiReportService reportService;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private AbstractUserDao abstractUserDao;

	/**
	 * @param venID
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/registerparty/requestReregistration", method = RequestMethod.POST)
	@ResponseBody
	public void registerPartyRequestReregistration(@PathVariable("venID") String venID)
			throws Oadr20bException, OadrElementNotFoundException {

		LOGGER.debug("Request Reregistration VEN: " + venID);

		Ven ven = checkVen(venID);
		OadrRequestReregistrationType build = Oadr20bEiRegisterPartyBuilders
				.newOadr20bRequestReregistrationBuilder(ven.getUsername()).build();

		venDistributeService.distribute(ven, build);
	}

	/**
	 * @param venID
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/registerparty/cancelPartyRegistration", method = RequestMethod.POST)
	@ResponseBody
	public void registerPartyCancelPartyRegistration(@PathVariable("venID") String venID)
			throws Oadr20bException, OadrElementNotFoundException {

		LOGGER.debug("Cancel registration VEN: " + venID);

		Ven ven = checkVen(venID);
		OadrCancelPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders.newOadr20bCancelPartyRegistrationBuilder(
				UUID.randomUUID().toString(), ven.getRegistrationId(), ven.getUsername()).build();

		venDistributeService.distribute(ven, build);
	}

	/**
	 * @param venID
	 * @param reportSpecifierId
	 * @return
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/report/available", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportCapabilityDto> viewOtherReportCapability(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = false) String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		List<OtherReportCapability> findBySource = null;
		if (reportSpecifierId != null) {
			findBySource = otherReportCapabilityService.findByReportSpecifierId(reportSpecifierId);
		} else {
			findBySource = otherReportCapabilityService.findBySource(ven);
		}
		return oadr20bDtoMapper.mapList(findBySource, ReportCapabilityDto.class);
	}

	/**
	 * @param venID
	 * @param reportSpecifierId
	 * @return
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/report/available/description", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportCapabilityDescriptionDto> viewOtherReportCapabilityDescription(
			@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = true) String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		OtherReportCapability reportCapability = checkOtherReportCapability(venID, reportSpecifierId);

		List<OtherReportCapabilityDescription> desc = otherReportCapabilityDescriptionService
				.findByOtherReportCapability(reportCapability);

		return oadr20bDtoMapper.mapList(desc, ReportCapabilityDescriptionDto.class);
	}

	/**
	 * @param venID
	 * @param reportSpecifierId
	 * @param rid
	 * @param granularity
	 * @param reportBackDuration
	 * @param archivated
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/report/available/description/subscribe", method = RequestMethod.POST)
	@ResponseBody
	public void subscribeOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestBody List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions, HttpServletRequest r)
			throws Oadr20bMarshalException, OadrElementNotFoundException, Oadr20bApplicationLayerException {
		Ven ven = checkVen(venID);
		AbstractUser requestor = abstractUserDao.findOneByUsername(r.getUserPrincipal().getName());
		reportService.subscribe(requestor, ven, subscriptions);
	}

	/**
	 * @param venID
	 * @param reportSpecifierId
	 * @param rid
	 * @param start
	 * @param end
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/report/available/description/request", method = RequestMethod.POST)
	@ResponseBody
	public void requestOtherReportCapabilityDescriptionRid(@PathVariable("venID") String venID,
			@RequestBody List<OtherReportRequestDtoCreateRequestDto> requests, HttpServletRequest r)
			throws Oadr20bMarshalException, OadrElementNotFoundException, Oadr20bApplicationLayerException {
		Ven ven = checkVen(venID);
		AbstractUser requestor = abstractUserDao.findOneByUsername(r.getUserPrincipal().getName());
		reportService.request(requestor, ven, requests);
	}

	/**
	 * @param venID
	 * @param reportSpecifierId
	 * @param reportRequestId
	 * @return
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/report/requested", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportRequestDto> viewReportRequest(@PathVariable("venID") String venID,
			@RequestParam(value = "reportSpecifierId", required = false) String reportSpecifierId,
			@RequestParam(value = "reportRequestId", required = false) String reportRequestId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		List<OtherReportRequest> findBySource = null;
		if (reportSpecifierId == null && reportRequestId == null) {
			findBySource = otherReportRequestService.findBySource(ven);
		} else if (reportSpecifierId != null) {
			findBySource = otherReportRequestService.findBySourceAndReportSpecifierId(ven, reportSpecifierId);
		} else {
			findBySource = otherReportRequestService.findBySourceAndReportRequestId(ven, reportRequestId);
		}

		return oadr20bDtoMapper.mapList(findBySource, OtherReportRequestDto.class);
	}

	/**
	 * @param venID
	 * @param reportRequestId
	 * @return
	 * @throws Oadr20bMarshalException
	 * @throws OadrElementNotFoundException
	 */
	@RequestMapping(value = "/{venID}/report/requested/specifier", method = RequestMethod.POST)
	@ResponseBody
	public List<OtherReportRequestSpecifierDto> viewReportRequestSpecifier(@PathVariable("venID") String venID,
			@RequestBody OtherReportRequestSpecifierSearchCriteria criteria)
			throws Oadr20bMarshalException, OadrElementNotFoundException {
		checkVen(venID);
		Specification<OtherReportRequestSpecifier> search = OtherReportRequestSpecifierSpecification.search(criteria);
		List<OtherReportRequestSpecifier> findAll = otherReportRequestSpecifierDao.findAll(search);
		return oadr20bDtoMapper.mapList(findAll, OtherReportRequestSpecifierDto.class);
	}

	@RequestMapping(value = "/{venID}/report/requested/cancelSubscription", method = RequestMethod.POST)
	@ResponseBody
	public void cancelSubscriptionReportRequest(@PathVariable("venID") String venID,
			@RequestParam("reportRequestId") String reportRequestId)
			throws Oadr20bApplicationLayerException, OadrElementNotFoundException {

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

	@RequestMapping(value = "/{venID}/report_action/cancel", method = RequestMethod.POST)
	@ResponseBody
	public void cancelReport(@PathVariable("venID") String venID,
			@RequestParam("reportRequestId") String reportRequestId)
			throws Oadr20bException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		boolean reportToFollow = false;
		List<String> reportId = Lists.newArrayList(reportRequestId);
		OadrCancelReportType build = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder("", ven.getUsername(), reportToFollow).addReportRequestId(reportId)
				.build();

		venDistributeService.distribute(ven, build);
	}

	@RequestMapping(value = "/{venID}/opt", method = RequestMethod.GET)
	@ResponseBody
	public List<VenOptDto> venOpt(@PathVariable("venID") String venID,
			@RequestParam(value = "start", required = false) Instant start,
			@RequestParam(value = "end", required = false) Instant end,
			@RequestParam(value = "marketContext", required = false) String marketContextName)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		if (marketContextName != null) {
			checkMarketContextExists(marketContextName);
		}

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

	@RequestMapping(value = "/{venID}/report/data/float/{reportSpecifierId}", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportDataFloatDto> viewsFloatReportData(@PathVariable("venID") String venID,
			@PathVariable("reportSpecifierId") String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapability(venID, reportSpecifierId);

		Iterable<OtherReportDataFloat> findAll = otherReportDataService.findAll();
		List<OtherReportDataFloat> list = new ArrayList<>();
		findAll.forEach(el -> list.add(el));

		List<OtherReportDataFloat> findByReportSpecifierId = otherReportDataService.findByReportSpecifierId(venID,
				reportSpecifierId);
		return oadr20bDtoMapper.mapList(findByReportSpecifierId, OtherReportDataFloatDto.class);
	}

	@RequestMapping(value = "/{venID}/report/data/float/{reportSpecifierId}/rid/{rid}", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportDataFloatDto> viewsFloatReportData(@PathVariable("venID") String venID,
			@PathVariable("reportSpecifierId") String reportSpecifierId, @PathVariable("rid") String rid)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapabilityDescription(venID, reportSpecifierId, rid);

		return oadr20bDtoMapper.mapList(
				otherReportDataService.findByReportSpecifierIdAndRid(venID, reportSpecifierId, rid),
				OtherReportDataFloatDto.class);
	}

	@RequestMapping(value = "/{venID}/report/data/resourcestatus/{reportSpecifierId}", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportDataPayloadResourceStatusDto> viewsResourceStatusReportData(
			@PathVariable("venID") String venID, @PathVariable("reportSpecifierId") String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapability(venID, reportSpecifierId);

		List<OtherReportDataPayloadResourceStatus> findByReportSpecifierId = otherReportDataPayloadResourceStatusService
				.findByReportSpecifierId(venID, reportSpecifierId);
		return oadr20bDtoMapper.mapList(findByReportSpecifierId, OtherReportDataPayloadResourceStatusDto.class);
	}

	@RequestMapping(value = "/{venID}/report/data/resourcestatus/{reportSpecifierId}/rid/{rid}", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportDataPayloadResourceStatusDto> viewsResourceStatusReportData(
			@PathVariable("venID") String venID, @PathVariable("reportSpecifierId") String reportSpecifierId,
			@PathVariable("rid") String rid) throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapabilityDescription(venID, reportSpecifierId, rid);

		return oadr20bDtoMapper.mapList(otherReportDataPayloadResourceStatusService.findByReportSpecifierIdAndRid(venID,
				reportSpecifierId, rid), OtherReportDataPayloadResourceStatusDto.class);
	}

	@RequestMapping(value = "/{venID}/report/data/keytoken/{reportSpecifierId}", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportDataKeyTokenDto> viewsKeyTokenReportData(@PathVariable("venID") String venID,
			@PathVariable("reportSpecifierId") String reportSpecifierId)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapability(venID, reportSpecifierId);

		List<OtherReportDataKeyToken> findByReportSpecifierId = otherReportDataKeyTokenService
				.findByReportSpecifierId(venID, reportSpecifierId);
		return oadr20bDtoMapper.mapList(findByReportSpecifierId, OtherReportDataKeyTokenDto.class);
	}

	@RequestMapping(value = "/{venID}/report/data/keytoken/{reportSpecifierId}/rid/{rid}", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportDataKeyTokenDto> viewsKeyTokenReportData(@PathVariable("venID") String venID,
			@PathVariable("reportSpecifierId") String reportSpecifierId, @PathVariable("rid") String rid)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		checkVen(venID);
		checkOtherReportCapabilityDescription(venID, reportSpecifierId, rid);

		return oadr20bDtoMapper.mapList(
				otherReportDataKeyTokenService.findByReportSpecifierIdAndRid(venID, reportSpecifierId, rid),
				OtherReportDataKeyTokenDto.class);
	}

	@RequestMapping(value = "/{venID}/report/available/search", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportCapabilityDto> pageOtherReportCapability(@PathVariable("venID") String venID,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = Oadr20bVenController.PAGINATION_SIZE_DEFAULT) Integer size,
			HttpServletResponse response) throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		Page<OtherReportCapability> pageBySource = otherReportCapabilityService.findBySource(ven, page, size);
		response.addHeader("X-total-page", String.valueOf(pageBySource.getTotalPages()));
		response.addHeader("X-total-count", String.valueOf(pageBySource.getTotalElements()));
		response.addHeader("mouaiccool", String.valueOf(pageBySource.getTotalElements()));
		return oadr20bDtoMapper.mapList(pageBySource.getContent(), OtherReportCapabilityDto.class);
	}

	@RequestMapping(value = "/{venID}/report/requested/search", method = RequestMethod.GET)
	@ResponseBody
	public List<OtherReportRequestDto> pageReportRequest(@PathVariable("venID") String venID,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = Oadr20bVenController.PAGINATION_SIZE_DEFAULT) Integer size,
			HttpServletResponse response) throws Oadr20bMarshalException, OadrElementNotFoundException {

		Ven ven = checkVen(venID);
		Page<OtherReportRequest> pageBySource = otherReportRequestService.findBySource(ven, page, size);
		response.addHeader("X-total-page", String.valueOf(pageBySource.getTotalPages()));
		response.addHeader("X-total-count", String.valueOf(pageBySource.getTotalElements()));
		return oadr20bDtoMapper.mapList(pageBySource.getContent(), OtherReportRequestDto.class);
	}

	private void checkResource(Ven ven, String resourceName) throws OadrElementNotFoundException {
		VenResource resource = venResourceService.findByVenAndName(ven, resourceName);
		if (resource == null) {
			throw new OadrElementNotFoundException("Resource: " + resourceName + " not foudn for ven: " + ven.getId());
		}
	}

	private void checkMarketContextExists(String marketContextName) throws OadrElementNotFoundException {
		if (marketContextName != null) {
			VenMarketContext marketContext = venMarketContextService.findOneByName(marketContextName);
			if (marketContext == null) {
				throw new OadrElementNotFoundException("MarketContext: " + marketContextName + " not found");
			}
		}
	}

	private OtherReportCapability checkOtherReportCapability(String venId, String reportSpecifierId)
			throws OadrElementNotFoundException {
		OtherReportCapability otherReportCapability = otherReportCapabilityService
				.findOneBySourceUsernameAndReportSpecifierId(venId, reportSpecifierId);

		if (otherReportCapability == null) {
			throw new OadrElementNotFoundException(
					"ReportCapability with reportSpecifierId: " + reportSpecifierId + " not found for venID: " + venId);
		}

		return otherReportCapability;
	}

	private Ven checkVen(String venID) throws OadrElementNotFoundException {
		Ven ven = venService.findOneByUsername(venID);
		if (ven == null) {
			throw new OadrElementNotFoundException("ven: " + venID + " not found");
		}
		return ven;
	}

	private OtherReportCapability checkOtherReportCapabilityDescription(String venID, String reportSpecifierId,
			String rid) throws OadrElementNotFoundException {
		OtherReportCapability otherReportCapability = checkOtherReportCapability(venID, reportSpecifierId);
		OtherReportCapabilityDescription otherReportCapabilityDescription = otherReportCapabilityDescriptionService
				.findByOtherReportCapabilityAndRid(otherReportCapability, rid);
		if (otherReportCapabilityDescription == null) {
			throw new OadrElementNotFoundException("ReportCapabilityDescription with reportSpecifierId: "
					+ reportSpecifierId + " and rid: " + rid + " not found for venID: " + venID);
		}
		return otherReportCapability;
	}

}
