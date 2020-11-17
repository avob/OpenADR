package com.avob.openadr.server.common.vtn.broker.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile({ "!test", "external"})
@RestController
@RequestMapping("/auth")
public class RabbitmqHTTPAuthController {

//	[
//	  {rabbit, [{auth_backends, [rabbit_auth_backend_http]}]},
//	  {rabbitmq_auth_backend_http,
//	   [{http_method,   post},
//	    {user_path,     "https://vtn.oadr.com:8181/testvtn/auth/user"},
//	    {vhost_path,    "https://vtn.oadr.com:8181/testvtn/auth/vhost"},
//	    {resource_path, "https://vtn.oadr.com:8181/testvtn/auth/resource"},
//	    {topic_path,    "https://vtn.oadr.com:8181/testvtn/auth/topic"}]}
//	].

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitmqHTTPAuthController.class);

	@RequestMapping("user")
	public String user(@RequestParam("username") String username, @RequestParam("password") String password) {
		LOGGER.info("Trying to authenticate user {}", username);
		return "allow administrator management";
	}

	@RequestMapping("vhost")
	public String vhost(VirtualHostCheck check) {
		LOGGER.info("Checking vhost access with {}", check);
		return "allow";
	}

	@RequestMapping("resource")
	public String resource(ResourceCheck check) {
		LOGGER.info("Checking resource access with {}", check);
		return "allow";
	}

	@RequestMapping("topic")
	public String topic(TopicCheck check) {
		LOGGER.info("Checking topic access with {}", check);
		return "allow";
	}

}
