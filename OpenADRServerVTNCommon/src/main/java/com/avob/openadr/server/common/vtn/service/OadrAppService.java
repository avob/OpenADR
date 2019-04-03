package com.avob.openadr.server.common.vtn.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrAppCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrAppDao;

@Service
public class OadrAppService extends AbstractUserService<OadrApp> {

	@Resource
	private OadrAppDao appDao;

	public OadrApp prepare(String username, String password) {
		return super.prepare(new OadrApp(), username, password);
	}

	public OadrApp prepare(String username) {
		return super.prepare(new OadrApp(), username);
	}

	@Override
	public void delete(OadrApp instance) {
		appDao.delete(instance);
	}

	@Override
	public void delete(Iterable<OadrApp> instances) {
		appDao.deleteAll(instances);
	}

	@Override
	public OadrApp save(OadrApp instance) {
		return appDao.save(instance);
	}

	@Override
	public void save(Iterable<OadrApp> instances) {
		appDao.saveAll(instances);
	}

	public OadrApp findByUsername(String username) {
		return appDao.findOneByUsername(username);
	}

	public List<OadrApp> findAll() {
		return appDao.findAll();
	}

	public OadrApp create(OadrAppCreateDto app) {
		OadrApp prepare = this.prepare(app.getUsername(), app.getPassword());
		prepare.setRoles(app.getRoles());
		OadrApp save = appDao.save(prepare);
		return save;
	}
}
