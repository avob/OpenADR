package com.avob.openadr.server.common.vtn.controller.broker.activemq;

import static org.junit.Assert.assertTrue;

import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.broker.activemq.ActiveMQAuthorizationBroker;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.security.OadrSecurityRoleService;
import com.avob.openadr.server.common.vtn.service.OadrAppService;
import com.avob.openadr.server.common.vtn.service.OadrUserService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class ActiveMQAuthorizationBrokerTest {

	private static final String LOCAL_ACCESS = "tcp://localhost";

	private static final String EXTERNAL_ACCESS = "tcp://test.oadr";

	@Resource
	private VenService venService;

	@Resource
	private OadrUserService oadrUserService;

	@Resource
	private OadrAppService oadrAppService;

	@Resource
	private OadrSecurityRoleService oadrSecurityRoleService;

	private ActiveMQAuthorizationBroker broker = new ActiveMQAuthorizationBroker(Mockito.mock(Broker.class));

	@PostConstruct
	public void init() {
		broker.setOadrSecurityRoleService(oadrSecurityRoleService);
	}

	@Test
	public void testLocalhostNotSSL() throws Exception {

		ConnectionContext context = Mockito.mock(ConnectionContext.class, Answers.RETURNS_DEEP_STUBS);

		Mockito.when(context.getConnection().getConnector().getBrokerInfo().getBrokerURL())
				.thenReturn(LOCAL_ACCESS);

		ConnectionInfo info = Mockito.mock(ConnectionInfo.class);

		broker.addConnection(context, info);
	}

	@Test
	public void testExternalNotSSL() throws Exception {

		ConnectionContext context = Mockito.mock(ConnectionContext.class, Answers.RETURNS_DEEP_STUBS);

		Mockito.when(context.getConnection().getConnector().getBrokerInfo().getBrokerURL()).thenReturn(EXTERNAL_ACCESS);

		ConnectionInfo info = Mockito.mock(ConnectionInfo.class);
		boolean exception = false;
		try {
			broker.addConnection(context, info);
		} catch (Exception e) {
			exception = true;
		}

		assertTrue(exception);

	}

	@Test
	public void testExternalSSL_VENRoleNotGranted() throws Exception {

		X509Certificate cert = OadrPKISecurity.parseCertificate("src/test/resources/cert/test.crt");
		String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(cert);
		Ven prepare = venService.prepare(fingerprint);
		venService.save(prepare);

		ConnectionContext context = Mockito.mock(ConnectionContext.class, Answers.RETURNS_DEEP_STUBS);

		Mockito.when(context.getConnection().getConnector().getBrokerInfo().getBrokerURL()).thenReturn(EXTERNAL_ACCESS);

		Mockito.when(context.getSecurityContext()).thenReturn(null);

		ConnectionInfo info = Mockito.mock(ConnectionInfo.class, Answers.RETURNS_DEEP_STUBS);

		X509Certificate[] array = new X509Certificate[] { cert };
		Mockito.when(info.getTransportContext()).thenReturn(array);
		boolean exception = false;
		try {
			broker.addConnection(context, info);
		} catch (Exception e) {
			exception = true;
		}

		assertTrue(exception);

		venService.delete(prepare);

	}

	@Test
	public void testExternalSSL_UserRoleNotGranted() throws Exception {

		X509Certificate cert = OadrPKISecurity.parseCertificate("src/test/resources/cert/test.crt");
		String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(cert);
		OadrUser prepare = oadrUserService.prepare(fingerprint);
		oadrUserService.save(prepare);

		ConnectionContext context = Mockito.mock(ConnectionContext.class, Answers.RETURNS_DEEP_STUBS);

		Mockito.when(context.getConnection().getConnector().getBrokerInfo().getBrokerURL()).thenReturn(EXTERNAL_ACCESS);

		Mockito.when(context.getSecurityContext()).thenReturn(null);

		ConnectionInfo info = Mockito.mock(ConnectionInfo.class, Answers.RETURNS_DEEP_STUBS);

		X509Certificate[] array = new X509Certificate[] { cert };
		Mockito.when(info.getTransportContext()).thenReturn(array);
		boolean exception = false;
		try {
			broker.addConnection(context, info);
		} catch (Exception e) {
			exception = true;
		}

		assertTrue(exception);

		oadrUserService.delete(prepare);

	}

	@Test
	public void testExternalSSL_AppRoleGranted() throws Exception {

		X509Certificate cert = OadrPKISecurity.parseCertificate("src/test/resources/cert/test.crt");
		String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(cert);
		OadrApp prepare = oadrAppService.prepare(fingerprint);
		oadrAppService.save(prepare);

		ConnectionContext context = Mockito.mock(ConnectionContext.class, Answers.RETURNS_DEEP_STUBS);

		Mockito.when(context.getConnection().getConnector().getBrokerInfo().getBrokerURL()).thenReturn(EXTERNAL_ACCESS);

		Mockito.when(context.getSecurityContext()).thenReturn(null);

		ConnectionInfo info = Mockito.mock(ConnectionInfo.class, Answers.RETURNS_DEEP_STUBS);

		X509Certificate[] array = new X509Certificate[] { cert };
		Mockito.when(info.getTransportContext()).thenReturn(array);
		broker.addConnection(context, info);

		oadrAppService.delete(prepare);

	}

	@Test
	public void testExternalSSL_UserRoleVTNGranted() throws Exception {

		X509Certificate cert = OadrPKISecurity.parseCertificate("src/test/resources/cert/test.crt");
		String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(cert);
		OadrUser prepare = oadrUserService.prepare(fingerprint);
		prepare.setRoles(Lists.newArrayList("ROLE_VTN"));
		oadrUserService.save(prepare);

		ConnectionContext context = Mockito.mock(ConnectionContext.class, Answers.RETURNS_DEEP_STUBS);

		Mockito.when(context.getConnection().getConnector().getBrokerInfo().getBrokerURL()).thenReturn(EXTERNAL_ACCESS);

		Mockito.when(context.getSecurityContext()).thenReturn(null);

		ConnectionInfo info = Mockito.mock(ConnectionInfo.class, Answers.RETURNS_DEEP_STUBS);

		X509Certificate[] array = new X509Certificate[] { cert };
		Mockito.when(info.getTransportContext()).thenReturn(array);
		broker.addConnection(context, info);

		oadrUserService.delete(prepare);

	}

}
