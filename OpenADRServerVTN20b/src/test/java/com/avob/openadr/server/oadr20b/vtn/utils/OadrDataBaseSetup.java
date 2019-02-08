package com.avob.openadr.server.oadr20b.vtn.utils;

import javax.annotation.Resource;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.google.common.collect.Sets;

@Service
public class OadrDataBaseSetup {

    public static final String VEN = "ven1";
    public static final String VEN_REGISTRATION_ID = "ven1RegistrationId";
    public static final UserRequestPostProcessor VEN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors.user(VEN)
            .roles("VEN", "REGISTERED");

    public static final String VEN_RESOURCE_1 = "venResource1";
    public static final String VEN_RESOURCE_2 = "venResource2";
    
    public static final String ANOTHER_VEN = "ven2";
    public static final String ANOTHER_VEN_REGISTRATION_ID = "ven2RegistrationId";
    public static final UserRequestPostProcessor ANOTHER_VEN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors.user(ANOTHER_VEN)
            .roles("VEN", "REGISTERED");

    public static final String NOT_REGISTERED_VEN = "ven2";
    public static final UserRequestPostProcessor NOT_REGISTERED_VEN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
            .user(NOT_REGISTERED_VEN).roles("VEN");

    public static final String USER = "user1";
    public static final UserRequestPostProcessor USER_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors.user(USER)
            .roles("USER");

    public static final String ADMIN = "admin";
    public static final UserRequestPostProcessor ADMIN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
            .user(ADMIN).roles("ADMIN");

    public static final String MARKET_CONTEXT_NAME = "http://oadr.avob.com";
    public static final String ANOTHER_MARKET_CONTEXT_NAME = "http://another-oadr.avob.com";

    public static final String GROUP = "group1";
    public static final String ANOTHER_GROUP = "group2";

    @Resource
    private VenService venService;

    @Resource
    private VenResourceService venResourceService;

    @Resource
    private VenMarketContextService venMarketContextService;

    @Resource
    private VenGroupService venGroupService;

    @Resource
    private OadrUserService userService;

    public void init() {
        VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(MARKET_CONTEXT_NAME));
        venMarketContextService.save(marketContext);

        VenMarketContext anotherMarketContext = venMarketContextService
                .prepare(new VenMarketContextDto(ANOTHER_MARKET_CONTEXT_NAME));
        venMarketContextService.save(anotherMarketContext);

        VenGroup group = venGroupService.prepare(new VenGroupDto(GROUP));
        venGroupService.save(group);

        VenGroup anotherGroup = venGroupService.prepare(new VenGroupDto(ANOTHER_GROUP));
        venGroupService.save(anotherGroup);

        Ven ven = venService.prepare(VEN);
        ven.setRegistrationId(VEN_REGISTRATION_ID);
        ven.setVenMarketContexts(Sets.newHashSet(marketContext));
        ven.setVenGroup(Sets.newHashSet(group));
        venService.save(ven);

        venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_1)));
        venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_2)));

        OadrUser user = userService.prepare(USER);
        userService.save(user);

    }
}
