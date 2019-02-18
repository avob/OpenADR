package com.avob.openadr.server.oadr20a.vtn.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.ei.EiEventType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aApplicationLayerException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.xcal.Properties;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOadrProfileEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aCreatedEventApplicationLayerException;
import com.avob.openadr.server.oadr20a.vtn.exception.Oadr20aRequestEventApplicationLayerException;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/OadrEvent")
public class Oadr20aEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aVTNEiEventController.class);

	@Resource
	private Oadr20aJAXBContext jaxbContext;

	private Mapper mapper = new DozerBeanMapper();

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private VenResourceService venResourceService;

	@Resource
	private VenService venService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public DemandResponseEventDto create(@RequestBody String payload, Principal principal, HttpServletResponse response)
			throws Oadr20aUnmarshalException, Oadr20aMarshalException, Oadr20aApplicationLayerException,
			Oadr20aCreatedEventApplicationLayerException, Oadr20aRequestEventApplicationLayerException {

		Object unmarshal = jaxbContext.unmarshal(payload, false);

		String username = principal.getName();

		if (unmarshal instanceof JAXBElement) {
			JAXBElement<?> el = (JAXBElement<?>) unmarshal;

			if (el.getDeclaredType().equals(EiEventType.class)) {

				LOGGER.info(username + " - OadrCreatedEvent");

				EiEventType event = (EiEventType) el.getValue();

				LOGGER.info(event.getEventDescriptor().getEventID());

				String eventId = event.getEventDescriptor().getEventID();

				Properties properties = event.getEiActivePeriod().getProperties();

				Date start = properties.getDtstart().getDateTime().toGregorianCalendar().getTime();

				String durationXml = properties.getDuration().getDuration();

				String notificationDurationXml = properties.getXEiNotification().getDuration();

				String marketContext = event.getEventDescriptor().getEiMarketContext().getMarketContext();

				VenMarketContext findOneByName = venMarketContextService.findOneByName(marketContext);

				if (findOneByName == null) {
					LOGGER.warn("Unknown MarketContext: " + marketContext);
					response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
					return null;
				}

				List<String> listVenId = Lists.newArrayList();

				if (event.getEiTarget().getVenID() != null && !event.getEiTarget().getVenID().isEmpty()) {
					List<Ven> vens = venService.findByUsernameInAndVenMarketContextsContains(
							event.getEiTarget().getVenID(), findOneByName);
					Set<String> s1 = vens.stream().map(Ven::getUsername).collect(Collectors.toSet());
					Set<String> s2 = new HashSet<String>(event.getEiTarget().getVenID());
					if (!s1.equals(s2)) {
						s2.removeAll(s1);
						LOGGER.warn("Unknown Ven: " + s2.toString() + " in marketcontext: " + findOneByName.getName());
						response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
						return null;
					}

					listVenId.addAll(event.getEiTarget().getVenID());
				}

				if (event.getEiTarget().getGroupID() != null && !event.getEiTarget().getGroupID().isEmpty()) {
					List<VenGroup> findByName = venGroupService.findByName(event.getEiTarget().getGroupID());
					Set<String> s1 = findByName.stream().map(VenGroup::getName).collect(Collectors.toSet());
					Set<String> s2 = new HashSet<String>(event.getEiTarget().getGroupID());

					if (!s1.equals(s2)) {
						s2.removeAll(s1);
						LOGGER.warn("Unknown Group: " + s2.toString());
						response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
						return null;
					}

					List<Ven> vens = venService.findByGroupName(event.getEiTarget().getGroupID());
					Set<String> s3 = vens.stream().filter(ven -> ven.getVenMarketContexts().contains(findOneByName))
							.map(Ven::getUsername).collect(Collectors.toSet());
					Set<String> s4 = vens.stream().map(Ven::getUsername).collect(Collectors.toSet());
					if (!s3.equals(s4)) {
						s4.removeAll(s3);
						LOGGER.warn("Ven: " + s4.toString() + " not belonging to marketcontext: "
								+ findOneByName.getName());
						response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
						return null;
					}
					for (Ven ven : vens) {
						listVenId.add(ven.getUsername());
					}
				}

				if (listVenId.isEmpty()) {
					LOGGER.warn("No VEN targeted by processed event");
					response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
					return null;
				}

				if (event.getEiTarget().getResourceID() != null && !event.getEiTarget().getResourceID().isEmpty()) {
					List<VenResource> resources = venResourceService.findByVenIdAndName(listVenId,
							event.getEiTarget().getResourceID());
					Set<String> s1 = resources.stream().map(VenResource::getName).collect(Collectors.toSet());
					Set<String> s2 = new HashSet<String>(event.getEiTarget().getResourceID());
					if (!s1.equals(s2)) {
						s2.removeAll(s1);
						LOGGER.warn("Unknown Resource(s): " + s2.toString());
						response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
						return null;
					}
				}

				DemandResponseEvent create = demandResponseEventService.create(eventId, start.getTime(), durationXml,
						notificationDurationXml, listVenId, findOneByName, DemandResponseEventStateEnum.ACTIVE, payload,
						DemandResponseEventOadrProfileEnum.OADR20A);

				response.setStatus(HttpStatus.CREATED_201);

				return mapper.map(create, DemandResponseEventDto.class);
			}
		}

		response.setStatus(HttpStatus.NOT_ACCEPTABLE_406);

		return null;
	}
}
