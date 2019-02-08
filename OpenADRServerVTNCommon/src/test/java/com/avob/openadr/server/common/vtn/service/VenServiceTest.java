package com.avob.openadr.server.common.vtn.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.avob.openadr.server.common.vtn.ApplicationTest;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationTest.class })
@WebAppConfiguration
public class VenServiceTest {

    @Resource
    private VenService venService;

    @Resource
    private VenResourceService venResourceService;

    @Resource
    private VenMarketContextService venMarketContextService;

    @Resource
    private VenGroupService venGroupService;

    @Test
    public void prepareTest() {
        String username = "ven1";
        Ven prepare = venService.prepare(username);
        assertNotNull(prepare);
        assertNull(prepare.getId());
        assertNull(prepare.getBasicPassword());
        assertNull(prepare.getDigestPassword());
        assertEquals(username, prepare.getUsername());

        venService.save(prepare);
        assertNotNull(prepare);
        assertNotNull(prepare.getId());
        assertNull(prepare.getBasicPassword());
        assertNull(prepare.getDigestPassword());
        assertEquals(username, prepare.getUsername());

        venService.delete(prepare);

        String password = "ven1";
        prepare = venService.prepare(username, password);
        assertNotNull(prepare);
        assertNull(prepare.getId());
        assertNotNull(prepare.getBasicPassword());
        assertNotNull(prepare.getDigestPassword());
        assertEquals(username, prepare.getUsername());

        String profil = "20a";
        String transport = "http";
        VenCreateDto dto = new VenCreateDto();
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setOadrProfil(profil);
        dto.setTransport(transport);

        prepare = venService.prepare(dto);
        assertNotNull(prepare);
        assertNull(prepare.getId());
        assertNotNull(prepare.getBasicPassword());
        assertNotNull(prepare.getDigestPassword());
        assertEquals(username, prepare.getUsername());
        assertEquals(profil, prepare.getOadrProfil());
        assertEquals(transport, prepare.getTransport());

    }

    @Test
    public void venMetadataTest() {

        String groupName = "group1";
        VenGroup group = venGroupService.prepare(new VenGroupDto(groupName));
        venGroupService.save(group);

        String marketContextName = "http://oadr.avob.com";
        VenMarketContext marketContext = venMarketContextService.prepare(new VenMarketContextDto(marketContextName));
        venMarketContextService.save(marketContext);

        String venID = "ven1";
        Ven ven = venService.prepare(new VenCreateDto(venID));
        String registrationId = "registrationId";
        ven.setRegistrationId(registrationId);
        ven.setVenGroup(Sets.newHashSet(group));
        ven.setVenMarketContexts(Sets.newHashSet(marketContext));
        ven = venService.save(ven);

        Ven ven2 = venService.save(venService.prepare("ven2"));
        Ven ven3 = venService.save(venService.prepare("ven3"));

        VenResource res1 = venResourceService.prepare(ven, new VenResourceDto(venID + "_res0"));
        venResourceService.save(res1);
        VenResource res2 = venResourceService.prepare(ven, new VenResourceDto(venID + "_res1"));
        venResourceService.save(res2);

        List<VenResource> resources = venResourceService.findByVen(ven);
        assertEquals(2, resources.size());

        List<Ven> vens = venService.findByGroupName(Lists.newArrayList(group.getName()));
        assertEquals(1, vens.size());

        vens = venService.findByMarketContextName(Lists.newArrayList(marketContext.getName()));
        assertEquals(1, vens.size());

        vens = venService.findByUsernameInAndVenMarketContextsContains(Lists.newArrayList("ven1", "ven2", "ven4"),
                marketContext);
        assertEquals(1, vens.size());

        Ven findOne = venService.findOneByRegistrationId(registrationId);
        assertNotNull(findOne);
        assertEquals(venID, findOne.getUsername());

        findOne = venService.findOneByUsername(venID);
        assertNotNull(findOne);
        assertEquals(venID, findOne.getUsername());

        assertEquals(3L, venService.count());

        venService.delete(ven);
        venService.delete(ven2);
        venService.delete(ven3);
        venGroupService.delete(group);
        venMarketContextService.delete(marketContext);

        assertEquals(0L, venService.count());
        assertEquals(0L, venResourceService.count());
        assertEquals(0L, venGroupService.count());
        assertEquals(0L, venMarketContextService.count());

    }
}
