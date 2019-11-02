package com.avob.openadr.server.oadr20b.vtn.utils;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushListener;

@Service
public class MockVenDistributeService extends VenDistributeService {

	@Resource
	private Oadr20bPushListener oadr20bPushListener;

	@Override
	protected void send(String command) {
		oadr20bPushListener.receiveCommand(command);
	}

}
