package com.avob.openadr.server.common.vtn.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.models.venrequestcount.VenRequestCount;
import com.avob.openadr.server.common.vtn.models.venrequestcount.VenRequestCountDao;

/**
 * manage per venId counter atomicity
 * 
 * @author bertrand
 *
 */
@Service
public class VenRequestCountService {

    @Resource
    private VenRequestCountDao dao;

    @Transactional
    public Long getAndIncrease(String venId) {
        VenRequestCount findOneAndLock = dao.findOneAndLock(venId);
        long count = 0L;
        if (findOneAndLock == null) {
            findOneAndLock = new VenRequestCount(venId);
        } else {
            count = findOneAndLock.getRequestCount();
        }
        findOneAndLock.setRequestCount(count + 1);
        dao.save(findOneAndLock);
        return count;
    }

}
