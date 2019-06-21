package com.avob.openadr.server.oadr20b.ven.xmpp;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.ven.VenConfig;
import com.avob.openadr.server.oadr20b.ven.exception.Oadr20bXmppException;

@Service
public class XmppConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppConnector.class);

	@Resource
	private VenConfig venConfig;
	@EventListener(ApplicationReadyEvent.class)
	public void init() throws Oadr20bXmppException {
		
		

	}
}
