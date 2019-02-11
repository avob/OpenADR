package com.avob.openadr.server.common.vtn.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.user.OadrUserDao;

@Service
public class OadrUserService extends AbstractUserService<OadrUser> {

    @Resource
    private OadrUserDao userdao;

    public OadrUser prepare(String username, String password) {
        return super.prepare(new OadrUser(), username, password);
    }

    public OadrUser prepare(String username) {
        return super.prepare(new OadrUser(), username);
    }

    @Override
    public void delete(OadrUser instance) {
        userdao.delete(instance);
    }

    @Override
    public void delete(Iterable<OadrUser> instances) {
        userdao.deleteAll(instances);
    }

    @Override
    public OadrUser save(OadrUser instance) {
        return userdao.save(instance);
    }

    @Override
    public void save(Iterable<OadrUser> instances) {
        userdao.saveAll(instances);
    }

    public OadrUser findByUsername(String username) {
        return userdao.findOneByUsername(username);
    }

}
