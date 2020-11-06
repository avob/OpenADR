package com.avob.openadr.server.common.vtn.service;

import static org.junit.Assert.assertEquals;

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
public class VenRequestCountServiceTest {

    @Resource
    private VenRequestCountService venRequestCountService;

    @Test
    public void getAndIncreaseTest() {
        String venId = "ven1";
        Long andIncrease = venRequestCountService.getAndIncrease(venId);
		assertEquals(Long.valueOf(0), andIncrease);
        andIncrease = venRequestCountService.getAndIncrease(venId);
        assertEquals(Long.valueOf(1), andIncrease);
        andIncrease = venRequestCountService.getAndIncrease(venId);
        assertEquals(Long.valueOf(2), andIncrease);

    }
}
