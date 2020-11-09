package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class VenMarketContextDaoTest {

	@Resource
	private VenService venService;

	@Resource
	private VenMarketContextDao venMarketContextDao;

	@Test
	public void crudTest() {
		String username = "ven1";
		Ven ven = venService.prepare(username, "myuberplaintextpassword");
		Ven ven2 = venService.prepare(username + "2", "myuberplaintextpassword");

		venService.save(ven);
		venService.save(ven2);

		String groupName = "myubergroup";
		VenMarketContext venGroup = new VenMarketContext(groupName, "");

		VenMarketContext venGroup2 = new VenMarketContext(groupName + "2", "");

		venMarketContextDao.save(venGroup);
		venMarketContextDao.save(venGroup2);

		ven.setVenMarketContexts(Sets.newHashSet(venGroup));
		venService.save(ven);

		ven2.setVenMarketContexts(Sets.newHashSet(venGroup, venGroup2));
		venService.save(ven2);

		venGroup = venMarketContextDao.findById(venGroup.getId()).get();
		venGroup2 = venMarketContextDao.findById(venGroup2.getId()).get();

		Iterable<Ven> findByVenGroupIterable = venService.findAll();
		List<Ven> findByVenGroup = Lists.newArrayList(findByVenGroupIterable);
		assertNotNull(findByVenGroup);
		assertEquals(2, findByVenGroup.size());
		assertEquals(username, findByVenGroup.get(0).getUsername());
		//
		findByVenGroup = venService.findByMarketContextName(Lists.newArrayList(venGroup.getName()));
		assertNotNull(findByVenGroup);
		assertEquals(2, findByVenGroup.size());

		findByVenGroup = venService.findByMarketContextName(Lists.newArrayList(venGroup2.getName()));
		assertNotNull(findByVenGroup);
		assertEquals(1, findByVenGroup.size());

		long count = venMarketContextDao.count();
		assertEquals(2, count);

		venService.delete(ven);

		count = venMarketContextDao.count();
		assertEquals(2, count);

		

		findByVenGroupIterable = venService.findAll();
		findByVenGroup = Lists.newArrayList(findByVenGroupIterable);
		assertNotNull(findByVenGroup);
		assertEquals(1, findByVenGroup.size());

		ven2 = venService.findOne(ven2.getId());
		venService.delete(ven2);
		venMarketContextDao.delete(venGroup);
		venMarketContextDao.delete(venGroup2);

	}
}
