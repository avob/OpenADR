package com.avob.openadr.server.common.vtn.models.ven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class VenDaoTest {

	@Resource
	private VenDao venDao;

	@Test
	public void crudTest() {
		String name = "name";

		String oadrProfil = "20a";
		String transport = "http";
		String pushUrl = "http://localhost/";

		String username = "username";

		Ven ven = new Ven();
		ven.setUsername(username);
		ven.setBasicPassword("basicmyuberplaintextpassword");
		ven.setDigestPassword("digestmyuberplaintextpassword");
		ven.setOadrName(name);
		ven.setOadrProfil(oadrProfil);
		ven.setTransport(transport);
		ven.setPushUrl(pushUrl);

		// test create
		Ven saved = venDao.save(ven);

		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(ven.getPushUrl(), saved.getPushUrl());
		assertEquals(ven.getOadrName(), saved.getOadrName());
		assertEquals(ven.getOadrProfil(), saved.getOadrProfil());
		assertEquals(ven.getDigestPassword(), saved.getDigestPassword());
		assertEquals(ven.getBasicPassword(), saved.getBasicPassword());
		assertEquals(ven.getTransport(), saved.getTransport());
		assertEquals(ven.getUsername(), saved.getUsername());

		// test find by id
		Ven findOne = venDao.findById(saved.getId()).get();

		assertEquals(saved.getId(), findOne.getId());
		assertEquals(saved.getPushUrl(), findOne.getPushUrl());
		assertEquals(saved.getOadrName(), findOne.getOadrName());
		assertEquals(saved.getOadrProfil(), findOne.getOadrProfil());
		assertEquals(saved.getDigestPassword(), findOne.getDigestPassword());
		assertEquals(saved.getTransport(), findOne.getTransport());
		assertEquals(saved.getUsername(), findOne.getUsername());

		// test findonebyusername
		Ven findOneByUsername = venDao.findOneByUsername(saved.getUsername());

		assertEquals(saved.getId(), findOneByUsername.getId());
		assertEquals(saved.getPushUrl(), findOneByUsername.getPushUrl());
		assertEquals(saved.getOadrName(), findOneByUsername.getOadrName());
		assertEquals(saved.getOadrProfil(), findOneByUsername.getOadrProfil());
		assertEquals(saved.getDigestPassword(), findOneByUsername.getDigestPassword());
		assertEquals(saved.getTransport(), findOneByUsername.getTransport());
		assertEquals(saved.getUsername(), findOneByUsername.getUsername());

		// test update
		String updatedName = "myuberupdatedname";
		findOne.setOadrName(updatedName);

		venDao.save(findOne);
		Ven findAnotherOne = venDao.findById(saved.getId()).get();
		assertEquals(findOne.getOadrName(), findAnotherOne.getOadrName());

		// test find all
		Iterable<Ven> findAll = venDao.findAll();
		Iterator<Ven> iterator = findAll.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			Ven next = iterator.next();
			assertEquals(findAnotherOne.getId(), next.getId());
			assertEquals(findAnotherOne.getPushUrl(), next.getPushUrl());
			assertEquals(findAnotherOne.getOadrName(), next.getOadrName());
			assertEquals(findAnotherOne.getOadrProfil(), next.getOadrProfil());
			assertEquals(findAnotherOne.getDigestPassword(), next.getDigestPassword());
			assertEquals(findAnotherOne.getTransport(), next.getTransport());
			assertEquals(findAnotherOne.getUsername(), next.getUsername());
		}
		assertEquals(1, count);

		// test count
		long count2 = venDao.count();
		assertEquals(1, count2);

		// test delete
		venDao.delete(findAnotherOne);
		count2 = venDao.count();
		assertEquals(0, count2);

	}
}
