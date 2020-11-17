package com.avob.openadr.server.common.vtn.broker.activemq;

import javax.annotation.Resource;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.security.OadrSecurityRoleService;

@Profile({ "test", "standalone" })
@Service
public class ActiveMQAuthorizationPlugin implements BrokerPlugin {

	@Resource
	private OadrSecurityRoleService oadrSecurityRoleService;

	public Broker installPlugin(Broker broker) throws Exception {
		ActiveMQAuthorizationBroker activeMQAuthorizationBroker = new ActiveMQAuthorizationBroker(broker);
		activeMQAuthorizationBroker.setOadrSecurityRoleService(oadrSecurityRoleService);
		return activeMQAuthorizationBroker;
	}
}
