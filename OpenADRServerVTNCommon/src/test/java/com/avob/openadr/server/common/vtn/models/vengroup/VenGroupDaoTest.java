package com.avob.openadr.server.common.vtn.models.vengroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
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
public class VenGroupDaoTest {

    @Resource
    private VenService venService;

    @Resource
    private VenGroupDao venGroupDao;

    @Test
    public void crudTest() {
        String username = "ven1";
        String password = "";
        Ven ven = venService.prepare(username, password);
        Ven ven2 = venService.prepare(username + "2", password);

        venService.save(ven);
        venService.save(ven2);

        String groupName = "myubergroup";
        VenGroup venGroup = new VenGroup(groupName);

        VenGroup venGroup2 = new VenGroup(groupName + "2");

        venGroupDao.save(venGroup);
        venGroupDao.save(venGroup2);

        ven.setVenGroup(Sets.newHashSet(venGroup));
        venService.save(ven);

        ven2.setVenGroup(Sets.newHashSet(venGroup, venGroup2));
        venService.save(ven2);

        venGroup = venGroupDao.findOne(venGroup.getId());
        venGroup2 = venGroupDao.findOne(venGroup2.getId());

        Iterable<Ven> findByVenGroupIterable = venService.findAll();
        List<Ven> findByVenGroup = Lists.newArrayList(findByVenGroupIterable);
        assertNotNull(findByVenGroup);
        assertEquals(2, findByVenGroup.size());
        assertEquals(username, findByVenGroup.get(0).getUsername());
        //
        findByVenGroup = venService.findByGroupName(Lists.newArrayList(venGroup.getName()));
        assertNotNull(findByVenGroup);
        assertEquals(2, findByVenGroup.size());

        findByVenGroup = venService.findByGroupName(Lists.newArrayList(venGroup2.getName()));
        assertNotNull(findByVenGroup);
        assertEquals(1, findByVenGroup.size());

        long count = venGroupDao.count();
        assertEquals(2, count);

        venService.delete(ven);

        count = venGroupDao.count();
        assertEquals(2, count);

        venGroupDao.delete(venGroup);

        findByVenGroupIterable = venService.findAll();
        findByVenGroup = Lists.newArrayList(findByVenGroupIterable);
        assertNotNull(findByVenGroup);
        assertEquals(1, findByVenGroup.size());

        ven2 = venService.findOne(ven2.getId());
        venService.delete(ven2);
        venGroupDao.delete(venGroup2);

    }
}
