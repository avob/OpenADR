package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venpoll.VenPoll;
import com.avob.openadr.server.oadr20b.vtn.models.venpoll.VenPollDao;

@Service
public class VenPollService {

	@Resource
	private VenPollDao venPollDao;

	@Transactional
	public String retrievePollForVenUsername(String venUsername) {

		Pageable page = PageRequest.of(0, 1, Sort.Direction.ASC, "createdTimestamp");

		List<VenPoll> list = venPollDao.findByVenUsername(venUsername, page);

		if (list == null || list.isEmpty()) {
			return null;
		}

		VenPoll venPoll = list.get(0);

		venPollDao.deleteById(venPoll.getId());

		return venPoll.getMessage();

	}

	@Transactional
	public void create(Ven ven, String message) {

		VenPoll poll = new VenPoll(ven, message);
		venPollDao.save(poll);
	}

	@Transactional
	public void deleteByVenUsername(String venUsername) {

		venPollDao.deleteByVenUsername(venUsername);
	}

	@Transactional
	public void deleteAll() {

		venPollDao.deleteAll();
	}

	@Transactional
	public Long countAll() {
		return venPollDao.count();
	}
}
