package com.avob.openadr.server.common.vtn.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.exception.GenerateX509VenException;
import com.avob.openadr.server.common.vtn.models.user.OadrUser;
import com.avob.openadr.server.common.vtn.models.user.OadrUserCreateDto;
import com.avob.openadr.server.common.vtn.models.user.OadrUserDao;
import com.avob.openadr.server.common.vtn.security.DigestAuthenticationProvider;

@Service
public class OadrUserService extends AbstractUserService<OadrUser> {

	@Resource
	private OadrUserDao userDao;

	@Autowired(required = false)
	private GenerateX509CertificateService generateX509VenService;

	@Resource
	private DigestAuthenticationProvider digestAuthenticationProvider;

	public OadrUser prepare(String username, String password) {
		return super.prepare(new OadrUser(), username, password, digestAuthenticationProvider.getRealm());
	}

	public OadrUser prepare(String username) {
		return super.prepare(new OadrUser(), username);
	}

	public OadrUser prepare(OadrUserCreateDto user) {
		OadrUser prepare = super.prepare(new OadrUser(), user, digestAuthenticationProvider.getRealm());
		prepare.setRoles(user.getRoles());
		return prepare;
	}

	@Override
	public void delete(OadrUser instance) {
		userDao.delete(instance);
	}

	@Override
	public void delete(Iterable<OadrUser> instances) {
		userDao.deleteAll(instances);
	}

	@Override
	public OadrUser save(OadrUser instance) {
		return userDao.save(instance);
	}

	@Override
	public void save(Iterable<OadrUser> instances) {
		userDao.saveAll(instances);
	}

	public OadrUser findByUsername(String username) {
		return userDao.findOneByUsername(username);
	}

	public List<OadrUser> findAll() {
		return userDao.findAll();
	}

	public Optional<File> generateCertificateIfRequired(OadrUserCreateDto dto, OadrUser user)
			throws GenerateX509VenException {

		if (dto.getAuthenticationType() != null && !"login".equals(dto.getAuthenticationType()) && dto.getNeedCertificateGeneration() != null) {

			if (generateX509VenService != null) {
				File generateCredentials = generateX509VenService.generateCredentials(dto, user);
				return Optional.of(generateCredentials);
			} else {
				throw new GenerateX509VenException(
						"Client certificate feature require CA certificate to be provided to the vtn");
			}
		}

		return Optional.empty();
	}

}
