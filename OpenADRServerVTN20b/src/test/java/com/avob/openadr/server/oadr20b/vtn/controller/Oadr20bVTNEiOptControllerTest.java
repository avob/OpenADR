package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.dto.VenOptDto;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiEventType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrSignedObject;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventStateEnum;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.converter.OptConverter;
import com.avob.openadr.server.oadr20b.vtn.service.VenOptService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Oadr20bVTNEiOptControllerTest {

    private static final String VEN_URL = "/Ven/";

    @Resource
    private VenOptService venOptService;

    @Resource
    private DemandResponseEventService demandResponseEventService;

    @Resource
    private XmlSignatureService xmlSignatureService;

    @Resource
    private OadrMockMvc oadrMockMvc;

    private MultiValueMap<String, String> createParams(Long start, Long end, String marketContextName) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        if (start != null) {
            params.set("start", String.valueOf(start));
        }
        if (end != null) {
            params.set("end", String.valueOf(end));
        }
        if (OadrDataBaseSetup.MARKET_CONTEXT_NAME != null) {
            params.set("marketContext", marketContextName);
        }
        return params;
    }

    @Test
    public void test() throws Exception {
        // test no opt configured
        this.oadrMockMvc
                .perform(MockMvcRequestBuilders.get(VEN_URL + OadrDataBaseSetup.VEN + "/opt")
                        .with(OadrDataBaseSetup.VEN_SECURITY_SESSION))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

        this.oadrMockMvc
                .perform(MockMvcRequestBuilders.get(VEN_URL + OadrDataBaseSetup.VEN + "/opt")
                        .with(OadrDataBaseSetup.USER_SECURITY_SESSION))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN_403));

        List<VenOptDto> findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(
                OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200,
                VenOptDto.class);
        assertNotNull(findScheduledOpt);
        assertTrue(findScheduledOpt.isEmpty());

        // test create valid vavailability opt on ven and marketContext using
        // xml signature
        String requestId = "requestId";
        Long createdDatetime = System.currentTimeMillis();
        VavailabilityType vavailabilityType = Oadr20bEiOptBuilders.newOadr20bVavailabilityBuilder()
                .addPeriod(createdDatetime, "PT1S").build();
        String optId = "optId";
        OptTypeType optType = OptTypeType.OPT_OUT;
        OptReasonEnumeratedType optReason = OptReasonEnumeratedType.NOT_PARTICIPATING;
        OadrCreateOptType oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId,
                OadrDataBaseSetup.VEN, createdDatetime, vavailabilityType, optId, optType, optReason)
                .withMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();
        
        String signed = xmlSignatureService.sign(oadrCreateOptType);
        OadrPayload oadrPayload = this.oadrMockMvc.postEiOptAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, signed,
                HttpStatus.OK_200, OadrPayload.class);

        assertEquals(String.valueOf(HttpStatus.OK_200),
                oadrPayload.getOadrSignedObject().getOadrCreatedOpt().getEiResponse().getResponseCode());
        assertEquals(requestId, oadrPayload.getOadrSignedObject().getOadrCreatedOpt().getEiResponse().getRequestID());
        assertEquals(optId, oadrPayload.getOadrSignedObject().getOadrCreatedOpt().getOptID());

        // test ven not found
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + "mouaiccool" + "/opt", HttpStatus.NOT_ACCEPTABLE_406, null,
                createParams(createdDatetime - 1000, createdDatetime + 1000, null));

        // test opt configured
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, createdDatetime + 1000, null));

        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());
        assertEquals(OadrDataBaseSetup.VEN, findScheduledOpt.get(0).getVenID());
        assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, findScheduledOpt.get(0).getMarketContextName());

        // test opt configured on OadrDataBaseSetup.MARKET_CONTEXT_NAME: between
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, createdDatetime + 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));

        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());
        assertEquals(OadrDataBaseSetup.VEN, findScheduledOpt.get(0).getVenID());
        assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, findScheduledOpt.get(0).getMarketContextName());

        // test opt configured on OadrDataBaseSetup.MARKET_CONTEXT_NAME: after
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, null, OadrDataBaseSetup.MARKET_CONTEXT_NAME));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());
        assertEquals(OadrDataBaseSetup.VEN, findScheduledOpt.get(0).getVenID());
        assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, findScheduledOpt.get(0).getMarketContextName());

        // test opt configured: after
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, null, null));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());
        assertEquals(OadrDataBaseSetup.VEN, findScheduledOpt.get(0).getVenID());
        assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, findScheduledOpt.get(0).getMarketContextName());

        // test opt configured on OadrDataBaseSetup.MARKET_CONTEXT_NAME: before
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(null, createdDatetime + 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));

        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());
        assertEquals(OadrDataBaseSetup.VEN, findScheduledOpt.get(0).getVenID());
        assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, findScheduledOpt.get(0).getMarketContextName());

        // test opt configured: before
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(null, createdDatetime + 1000, null));

        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());
        assertEquals(OadrDataBaseSetup.VEN, findScheduledOpt.get(0).getVenID());
        assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, findScheduledOpt.get(0).getMarketContextName());

        // test opt configured on OadrDataBaseSetup.MARKET_CONTEXT_NAME
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(null, null, OadrDataBaseSetup.MARKET_CONTEXT_NAME));

        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());
        assertEquals(OadrDataBaseSetup.VEN, findScheduledOpt.get(0).getVenID());
        assertEquals(OadrDataBaseSetup.MARKET_CONTEXT_NAME, findScheduledOpt.get(0).getMarketContextName());

        // test opt configured on OadrDataBaseSetup.MARKET_CONTEXT_NAME: outside
        // opt window frame
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(null, createdDatetime - 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));

        assertNotNull(findScheduledOpt);
        assertEquals(0, findScheduledOpt.size());

        // test no opt configured on
        // OadrDataBaseSetup.ANOTHER_MARKET_CONTEXT_NAME
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class, createParams(
                        createdDatetime - 1000, createdDatetime + 1000, OadrDataBaseSetup.ANOTHER_MARKET_CONTEXT_NAME));

        assertNotNull(findScheduledOpt);
        assertEquals(0, findScheduledOpt.size());

        // test unknown marketContext
        oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.NOT_ACCEPTABLE_406, null,
                createParams(createdDatetime - 1000, createdDatetime + 1000, "mouaiccool"));

        // test cannot set opt message about another VEN
        oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, OadrDataBaseSetup.VEN,
                createdDatetime, vavailabilityType, optId, optType, optReason)
                .withMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build();

        OadrCreatedOptType oadrCreatedOptType = this.oadrMockMvc.postEiOptAndExpect(
                OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, oadrCreateOptType, HttpStatus.OK_200,
                OadrCreatedOptType.class);

        assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
                oadrCreatedOptType.getEiResponse().getResponseCode());
        assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
        assertEquals(optId, oadrCreatedOptType.getOptID());

        // test create invalid vavailability opt on unknown ven resource
        oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, OadrDataBaseSetup.VEN,
                createdDatetime, vavailabilityType, optId, optType, optReason).addTargetedResource("fakeResource")
                .build();

        oadrCreatedOptType = this.oadrMockMvc.postEiOptAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
                oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);
        assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_ID_452),
                oadrCreatedOptType.getEiResponse().getResponseCode());

        // test create valid vavailability opt on ven resource without
        // marketcontext
        oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, OadrDataBaseSetup.VEN,
                createdDatetime, vavailabilityType, optId, optType, optReason)
                .addTargetedResource(OadrDataBaseSetup.VEN_RESOURCE_1).build();

        oadrCreatedOptType = this.oadrMockMvc.postEiOptAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
                oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);

        assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedOptType.getEiResponse().getResponseCode());
        assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
        assertEquals(optId, oadrCreatedOptType.getOptID());

        // test that NULL marketContext are grabbed when opt of a specific
        // marketContext are required (because NULL marketContext opt applied to
        // every marketContext)
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, createdDatetime + 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));

        assertNotNull(findScheduledOpt);
        assertEquals(2, findScheduledOpt.size());

        // test previously created opt is linked to resource
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/fakeResourceName", HttpStatus.NOT_ACCEPTABLE_406, null,
                createParams(createdDatetime - 1000, createdDatetime + 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, createdDatetime + 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class,
                createParams(null, createdDatetime + 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, null, OadrDataBaseSetup.MARKET_CONTEXT_NAME));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class, createParams(null, null, OadrDataBaseSetup.MARKET_CONTEXT_NAME));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class, createParams(createdDatetime - 1000, createdDatetime + 1000, null));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class, createParams(null, createdDatetime + 1000, null));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class, createParams(createdDatetime - 1000, null, null));
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt/resource/" + OadrDataBaseSetup.VEN_RESOURCE_1,
                HttpStatus.OK_200, VenOptDto.class);
        assertNotNull(findScheduledOpt);
        assertEquals(1, findScheduledOpt.size());

        // create a dr event
        EiTargetType target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(OadrDataBaseSetup.VEN).build();
        oadrMockMvc.postOadrEventAndExpect(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                createPayload("eventId1", target, OadrDataBaseSetup.MARKET_CONTEXT_NAME), HttpStatus.CREATED_201);

        List<DemandResponseEvent> find = demandResponseEventService.find(OadrDataBaseSetup.VEN,
                DemandResponseEventStateEnum.ACTIVE);
        assertNotNull(find);
        assertEquals(1, find.size());

        Long eventId = find.get(0).getId();
        Long modificationNumber = find.get(0).getModificationNumber();

        // test no opt for this ven and this event
        DemandResponseEvent findById = demandResponseEventService.findById(eventId).get();
        assertNotNull(findById);
        assertFalse(demandResponseEventService.hasResponded(OadrDataBaseSetup.VEN, findById));

        // test create opt related to specific event with different optId
        String eventOptId = "eventOptId";
        oadrCreateOptType = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, OadrDataBaseSetup.VEN,
                createdDatetime, String.valueOf(eventId), modificationNumber, eventOptId, optType, optReason).build();

        oadrCreatedOptType = this.oadrMockMvc.postEiOptAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
                oadrCreateOptType, HttpStatus.OK_200, OadrCreatedOptType.class);

        assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedOptType.getEiResponse().getResponseCode());
        assertEquals(requestId, oadrCreatedOptType.getEiResponse().getRequestID());
        assertEquals(eventOptId, oadrCreatedOptType.getOptID());

        // test than last opt is not considered as vavailability opt
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, createdDatetime + 1000, OadrDataBaseSetup.MARKET_CONTEXT_NAME));

        assertNotNull(findScheduledOpt);
        assertEquals(2, findScheduledOpt.size());

        // test than last opt has been created: as it's a event related opt on a
        // whole ven (without specifying resource), it does not create a VenOpt
        // but change VenDemandResponseEvent object related to ven and event
        // (override OadrCreatedEvent ven response)
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class);

        assertNotNull(findScheduledOpt);
        assertEquals(2, findScheduledOpt.size()); // no VenOpt created
        assertTrue(demandResponseEventService.hasResponded(OadrDataBaseSetup.VEN, findById));
        assertEquals(OptConverter.convert(optType),
                demandResponseEventService.getVenOpt(eventId, OadrDataBaseSetup.VEN));

        // cancel both opt
        OadrCancelOptType oadrCancelOptType = Oadr20bEiOptBuilders
                .newOadr20bCancelOptBuilder(requestId, optId, OadrDataBaseSetup.VEN).build();

        // invalid mismatch payload venID and username auth session
        OadrCanceledOptType oadrCanceledOptType = oadrMockMvc.postEiOptAndExpect(
                OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, xmlSignatureService.sign(oadrCancelOptType),
                HttpStatus.OK_200, OadrCanceledOptType.class);
        assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
                oadrCanceledOptType.getEiResponse().getResponseCode());

        oadrCanceledOptType = this.oadrMockMvc.postEiOptAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
                oadrCancelOptType, HttpStatus.OK_200, OadrCanceledOptType.class);

        assertEquals(String.valueOf(HttpStatus.OK_200), oadrCanceledOptType.getEiResponse().getResponseCode());
        assertEquals(requestId, oadrCanceledOptType.getEiResponse().getRequestID());
        assertEquals(optId, oadrCanceledOptType.getOptID());

        // test no opt configured
        findScheduledOpt = oadrMockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
                VEN_URL + OadrDataBaseSetup.VEN + "/opt", HttpStatus.OK_200, VenOptDto.class,
                createParams(createdDatetime - 1000, createdDatetime + 1000, null));

        assertNotNull(findScheduledOpt);
        assertEquals(0, findScheduledOpt.size());

        demandResponseEventService.delete(eventId);

    }

    private JAXBElement<EiEventType> createPayload(String eventId, EiTargetType target, String marketContextName) {
        long timestampStart = System.currentTimeMillis();
        String eventXmlDuration = "PT1H";
        String toleranceXmlDuration = "PT5M";
        String notificationXmlDuration = "P1D";
        EiActivePeriodType period = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
                eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

        long start = System.currentTimeMillis();
        float currentValue = 3f;
        String xmlDuration = "PT1H";
        String signalId = "0";
        String signalName = "simple";
        SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
        String intervalId = "0";
        EiEventSignalType eiEventSignal = Oadr20bEiEventBuilders
                .newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
                .addInterval(Oadr20bEiBuilders.newOadr20bSignalIntervalTypeBuilder(intervalId, start, xmlDuration,
                        Lists.newArrayList(currentValue)).build())
                .build();

        long datetime = System.currentTimeMillis();
        Long priority = 0L;
        EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
        String comment = "";
        EventDescriptorType descriptor = Oadr20bEiEventBuilders
                .newOadr20bEventDescriptorTypeBuilder(datetime, eventId, 0L, marketContextName, status)
                .withPriority(priority).withVtnComment(comment).build();

        OadrEvent event = Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder().withActivePeriod(period)
                .withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal).build();

        return Oadr20bFactory.createEiEvent(event.getEiEvent());
    }

}
