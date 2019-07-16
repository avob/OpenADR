package com.avob.openadr.server.common.vtn.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.exception.GenerateX509VenException;
import com.avob.openadr.server.common.vtn.models.user.OadrApp;
import com.avob.openadr.server.common.vtn.models.user.OadrAppCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrAppDao;
import com.avob.openadr.server.common.vtn.security.DigestAuthenticationProvider;

@Service
public class OadrAppService extends AbstractUserService<OadrApp> {

	@Resource
	private OadrAppDao appDao;

	@Autowired(required = false)
	private GenerateX509CertificateService generateX509VenService;

	@Resource
	private DigestAuthenticationProvider digestAuthenticationProvider;

	public OadrApp prepare(String username, String password) {
		return super.prepare(new OadrApp(), username, password, digestAuthenticationProvider.getRealm());
	}

	public OadrApp prepare(String username) {
		return super.prepare(new OadrApp(), username);
	}

	public OadrApp prepare(OadrAppCreateDto app) {
		OadrApp prepare = super.prepare(new OadrApp(), app, digestAuthenticationProvider.getRealm());
		prepare.setRoles(app.getRoles());
		return prepare;

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

	public Optional<File> generateCertificateIfRequired(OadrAppCreateDto dto, OadrApp app)
			throws GenerateX509VenException {

		if (dto.getAuthenticationType() != null && !"no".equals(dto.getAuthenticationType())
				&& dto.getNeedCertificateGeneration() != null) {

			if (generateX509VenService != null) {
				File generateCredentials = generateX509VenService.generateCredentials(dto, app);
				return Optional.of(generateCredentials);
			} else {
				throw new GenerateX509VenException(
						"Client certificate feature require CA certificate to be provided to the vtn");
			}
		}

		return Optional.empty();
	}
}
