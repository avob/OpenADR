package com.avob.openadr.server.oadr20b.vtn.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.server.common.vtn.VtnConfig;
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

	private static final String REGISTERED = "REGISTERED";
	public static final String VEN_HTTP_PULL_DSIG = "ven1";
	public static final String VEN_HTTP_PULL_DSIG_REGISTRATION_ID = "ven1RegistrationId";
	public static final UserRequestPostProcessor VEN_HTTP_PULL_DSIG_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
			.user(VEN_HTTP_PULL_DSIG).roles("VEN", REGISTERED);

	public static final String VEN_HTTP_PULL = "ven2";
	public static final String VEN_HTTP_PULL_REGISTRATION_ID = "ven2RegistrationId";
	public static final UserRequestPostProcessor ANOTHER_VEN_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
			.user(VEN_HTTP_PULL).roles("VEN", REGISTERED);

	public static final String VEN_XMPP = "ven3";
	public static final String VEN_XMPP_REGISTRATION_ID = "ven3RegistrationId";
	public static final UserRequestPostProcessor VEN_XMPP_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
			.user(VEN_HTTP_PULL).roles("VEN", REGISTERED);

	public static final String VEN_XMPP_DSIG = "ven4";
	public static final String VEN_XMPP_DSIG_REGISTRATION_ID = "ven4RegistrationId";
	public static final UserRequestPostProcessor VEN_XMPP_DSIG_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
			.user(VEN_HTTP_PULL).roles("VEN", REGISTERED);

	public static final String VEN_HTTP_PUSH_DSIG = "ven5";
	public static final String VEN_HTTP_PUSH_DSIG_REGISTRATION_ID = "ven5RegistrationId";
	public static final UserRequestPostProcessor VEN_HTTP_PUSH_DSIG_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
			.user(VEN_HTTP_PUSH_DSIG).roles("VEN", REGISTERED);

	public static final String VEN_HTTP_PUSH = "ven6";
	public static final String VEN_HTTP_PUSH_REGISTRATION_ID = "ven6RegistrationId";
	public static final UserRequestPostProcessor VEN_HTTP_PUSH_SECURITY_SESSION = SecurityMockMvcRequestPostProcessors
			.user(VEN_HTTP_PUSH).roles("VEN", REGISTERED);

	public static final String VEN_RESOURCE_1 = "venResource1";
	public static final String VEN_RESOURCE_2 = "venResource2";

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

	private static final Map<String, UserRequestPostProcessor> VEN_TEST_LIST = new HashMap<>();
	static {
		VEN_TEST_LIST.put(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, VEN_HTTP_PULL_DSIG_SECURITY_SESSION);
		VEN_TEST_LIST.put(OadrDataBaseSetup.VEN_HTTP_PULL, ANOTHER_VEN_SECURITY_SESSION);
		VEN_TEST_LIST.put(OadrDataBaseSetup.VEN_XMPP, VEN_XMPP_SECURITY_SESSION);
		VEN_TEST_LIST.put(OadrDataBaseSetup.VEN_XMPP_DSIG, VEN_XMPP_DSIG_SECURITY_SESSION);
		VEN_TEST_LIST.put(OadrDataBaseSetup.VEN_HTTP_PUSH_DSIG, VEN_HTTP_PUSH_DSIG_SECURITY_SESSION);
		VEN_TEST_LIST.put(OadrDataBaseSetup.VEN_HTTP_PUSH, VEN_HTTP_PUSH_SECURITY_SESSION);
	}

	public static Map<String, UserRequestPostProcessor> getTestVen() {
		return VEN_TEST_LIST;
	}

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

	@Resource
	private VtnConfig vtnConfig;

	@PostConstruct
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

		Ven ven = venService.prepare(VEN_HTTP_PULL_DSIG);
		ven.setOadrName(VEN_HTTP_PULL_DSIG);
		ven.setOadrProfil(SchemaVersionEnumeratedType.OADR_20B.value());
		ven.setRegistrationId(VEN_HTTP_PULL_DSIG_REGISTRATION_ID);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setVenGroup(Sets.newHashSet(group));
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		ven.setXmlSignature(true);
		ven.setHttpPullModel(true);
		venService.save(ven);

		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_1)));
		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_2)));

		ven = venService.prepare(VEN_HTTP_PULL);
		ven.setOadrName(VEN_HTTP_PULL);
		ven.setOadrProfil(SchemaVersionEnumeratedType.OADR_20B.value());
		ven.setRegistrationId(VEN_HTTP_PULL_REGISTRATION_ID);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setVenGroup(Sets.newHashSet(group));
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		ven.setHttpPullModel(true);
		ven.setXmlSignature(false);
		venService.save(ven);

		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_1)));
		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_2)));

		ven = venService.prepare(VEN_XMPP);
		ven.setOadrName(VEN_XMPP);
		ven.setOadrProfil(SchemaVersionEnumeratedType.OADR_20B.value());
		ven.setRegistrationId(VEN_XMPP_REGISTRATION_ID);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setVenGroup(Sets.newHashSet(group));
		ven.setTransport(OadrTransportType.XMPP.value());
		ven.setXmlSignature(false);
		ven.setHttpPullModel(false);
		ven.setPushUrl(VEN_XMPP + "@" + vtnConfig.getXmppDomain() + "/client");
		venService.save(ven);

		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_1)));
		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_2)));

		ven = venService.prepare(VEN_XMPP_DSIG);
		ven.setOadrName(VEN_XMPP_DSIG);
		ven.setOadrProfil(SchemaVersionEnumeratedType.OADR_20B.value());
		ven.setRegistrationId(VEN_XMPP_DSIG_REGISTRATION_ID);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setVenGroup(Sets.newHashSet(group));
		ven.setTransport(OadrTransportType.XMPP.value());
		ven.setXmlSignature(true);
		ven.setHttpPullModel(false);
		ven.setPushUrl(VEN_XMPP_DSIG + "@" + vtnConfig.getXmppDomain() + "/client");
		venService.save(ven);

		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_1)));
		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_2)));

		ven = venService.prepare(VEN_HTTP_PUSH);
		ven.setOadrName(VEN_HTTP_PUSH);
		ven.setOadrProfil(SchemaVersionEnumeratedType.OADR_20B.value());
		ven.setRegistrationId(VEN_HTTP_PUSH_REGISTRATION_ID);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setVenGroup(Sets.newHashSet(group));
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		ven.setXmlSignature(false);
		ven.setHttpPullModel(false);
		ven.setPushUrl("https://" + VEN_HTTP_PUSH + ".oadr.com");
		venService.save(ven);

		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_1)));
		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_2)));

		ven = venService.prepare(VEN_HTTP_PUSH_DSIG);
		ven.setOadrName(VEN_HTTP_PUSH_DSIG);
		ven.setOadrProfil(SchemaVersionEnumeratedType.OADR_20B.value());
		ven.setRegistrationId(VEN_HTTP_PUSH_DSIG_REGISTRATION_ID);
		ven.setVenMarketContexts(Sets.newHashSet(marketContext));
		ven.setVenGroup(Sets.newHashSet(group));
		ven.setTransport(OadrTransportType.SIMPLE_HTTP.value());
		ven.setXmlSignature(true);
		ven.setHttpPullModel(false);
		ven.setPushUrl("https://" + VEN_HTTP_PUSH_DSIG + ".oadr.com");
		venService.save(ven);

		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_1)));
		venResourceService.save(venResourceService.prepare(ven, new VenResourceDto(VEN_RESOURCE_2)));

		OadrUser user = userService.prepare(USER);
		userService.save(user);

	}
}
