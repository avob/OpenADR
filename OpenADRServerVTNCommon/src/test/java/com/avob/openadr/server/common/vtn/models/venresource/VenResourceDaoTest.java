package com.avob.openadr.server.common.vtn.models.venresource;

import javax.annotation.Resource;

import org.junit.Test;

import com.avob.openadr.server.common.vtn.service.VenService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { ApplicationTest.class })
//@WebAppConfiguration
//@ActiveProfiles("test")
public class VenResourceDaoTest {

	@Resource
	private VenService venService;

	@Resource
	private VenResourceDao venResourceDao;

	@Test
	public void crudTest() {
//		String username = "ven1";
//		Ven ven = venService.prepare(username, "myuberplaintextpassword");
//		venService.save(ven);
//
//		VenResource a = new VenResource("a", ven);
//		venResourceDao.save(a);
//		VenResource b = new VenResource("b", ven);
//		venResourceDao.save(b);
//
//		List<VenResource> findByVenId = venResourceDao.findByVenId(ven.getId());
//		assertNotNull(findByVenId);
//		assertEquals(2, findByVenId.size());
//
//		VenResource findOneByVenAndName = venResourceDao.findOneByVenAndName(ven, "a");
//		assertNotNull(findOneByVenAndName);
//		assertEquals("a", findOneByVenAndName.getName());
//
//		List<VenResource> findByVenIdAndName = venResourceDao.findByVenIdAndName(Lists.newArrayList("ven1"),
//				Lists.newArrayList("b"));
//		assertNotNull(findByVenIdAndName);
//		assertEquals("b", findByVenIdAndName.get(0).getName());
//
//		long countByVen_Id = venResourceDao.countByVenId(ven.getId());
//		assertEquals(2, countByVen_Id);
//
//		venResourceDao.delete(a);
//
//		countByVen_Id = venResourceDao.countByVenId(ven.getId());
//		assertEquals(1, countByVen_Id);
//
//		venService.delete(ven);
//
//		long count = venResourceDao.count();
//		assertEquals(0, count);
	}
}
