package com.avob.openadr.server.common.vtn.service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.exception.GenerateX509VenException;
import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.ven.VenDao;
import com.avob.openadr.server.common.vtn.models.ven.VenSpecification;
import com.avob.openadr.server.common.vtn.models.ven.filter.VenFilter;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEventDao;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroupDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDao;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceDao;
import com.avob.openadr.server.common.vtn.security.DigestAuthenticationProvider;

@Service
public class VenService extends AbstractUserService<Ven> {

	private static final Integer DEFAULT_SEARCH_SIZE = 20;

	@Resource
	private VenDao venDao;

	@Resource
	private VenResourceDao venResourceDao;

	@Resource
	private VenDemandResponseEventDao venDemandResponseEventDao;
	
	@Resource
	private VenGroupDao venGroupDao;
	
	@Resource
	private VenMarketContextDao venMarketContextDao;

	@Autowired(required = false)
	private GenerateX509CertificateService generateX509VenService;

	@Resource
	private DigestAuthenticationProvider digestAuthenticationProvider;

	private Mapper mapper = new DozerBeanMapper();

	public Ven prepare(String username, String password) {
		return super.prepare(new Ven(), username, password, digestAuthenticationProvider.getRealm());
	}

	/**
	 * @param username
	 * @return
	 */
	public Ven prepare(String username) {
		return super.prepare(new Ven(), username);
	}

	/**
	 * @param username
	 * @return
	 */
	public Ven prepare(VenCreateDto dto) {
		Ven prepare;
		if (dto.getPassword() != null) {
			prepare = super.prepare(new Ven(), dto.getUsername(), dto.getPassword(),
					digestAuthenticationProvider.getRealm());
		} else {
			prepare = super.prepare(new Ven(), dto.getUsername());
		}

		mapper.map(dto, prepare);

		return prepare;
	}

	public Optional<File> generateCertificateIfRequired(VenCreateDto dto, Ven ven) throws GenerateX509VenException {

		if (dto.getAuthenticationType() != null && !"no".equals(dto.getAuthenticationType())
				&& dto.getNeedCertificateGeneration() != null) {

			if (generateX509VenService != null) {
				File generateCredentials = generateX509VenService.generateCredentials(dto, ven);
				return Optional.of(generateCredentials);
			} else {
				throw new GenerateX509VenException(
						"Client certificate feature require CA certificate to be provided to the vtn");
			}
		}

		return Optional.empty();

	}

	@Override
	@Transactional
	public void delete(Ven instance) {
		
		venResourceDao.deleteByVenId(instance.getId());
		venDemandResponseEventDao.deleteByVenId(instance.getId());
		venDao.delete(instance);
	}

	@Override
	public void delete(Iterable<Ven> instances) {
		instances.forEach(ven -> { this.delete(ven);});
	}

	@Override
	public Ven save(Ven instance) {
		return venDao.save(instance);
	}

	@Override
	public void save(Iterable<Ven> instances) {
		venDao.saveAll(instances);
	}

	public Ven findOneByUsername(String username) {
		return venDao.findOneByUsername(username);
	}

	public Ven findOneByRegistrationId(String registrationId) {
		return venDao.findOneByRegistrationId(registrationId);
	}

	public List<Ven> findByUsernameInAndVenMarketContextsContains(List<String> username,
			VenMarketContext venMarketContext) {
		return venDao.findByUsernameInAndVenMarketContextsContains(username, venMarketContext);
	}

	public List<Ven> findByGroupName(List<String> groupName) {
		return venDao.findByVenGroupsName(groupName);
	}

	public List<Ven> findByMarketContextName(List<String> groupName) {
		return venDao.findByVenMarketContextsName(groupName);
	}

	public Ven findOne(Long id) {
		return venDao.findById(id).get();
	}

	public Iterable<Ven> findAll() {
		return venDao.findAll();
	}

	public long count() {
		return venDao.count();
	}

	public void cleanRegistration(Ven ven) {
		ven.setRegistrationId(null);
		this.save(ven);
	}

	public Page<Ven> search(List<VenFilter> filters) {
		return search(filters, null, null);
	}

	public Page<Ven> search(List<VenFilter> filters, Integer page, Integer size) {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = DEFAULT_SEARCH_SIZE;
		}
		Sort sort = Sort.by(Sort.Order.desc("registrationId"), Sort.Order.asc("commonName"));
		PageRequest of = PageRequest.of(page, size, sort);
		return venDao.findAll(VenSpecification.search(filters), of);
	}
	
	
	public void addVenDemandResponseEvent(Ven ven, DemandResponseEvent event) {
		VenDemandResponseEvent el = venDemandResponseEventDao
				.findOneByEventIdAndVenUsername(event.getId(), ven.getUsername());
		
		if(el == null) {
			el = new VenDemandResponseEvent(event, ven);
		}
		venDemandResponseEventDao.save(el);
	}
	
	@Transactional
	public boolean isVenTargetedBy(Ven ven, DemandResponseEvent event) {
		
		boolean targeted = false;
		for(Target target : event.getTargets()) {
			if(TargetTypeEnum.VEN.equals(target.getTargetType())) {
				
				if( ven.getUsername().equals(target.getTargetId())) {
					targeted = true;
				}
				
			} else if(TargetTypeEnum.GROUP.equals(target.getTargetType())) {
				
				
				Set<VenGroup> venGroups = ven.getVenGroups();
				if(venGroups != null) {
					for(VenGroup group : venGroups) {
						if(group.getName().equals(target.getTargetId())) {
							targeted = true;
						}
					}
				}
				
				
			} 
			
			if(event.getDescriptor().getMarketContext() != null) {

				
				Set<VenMarketContext> venMarketContext =  ven.getVenMarketContexts();
				if(venMarketContext != null) {
					for(VenMarketContext marketContext : venMarketContext) {
						if(marketContext.getName().equals(event.getDescriptor().getMarketContext().getName())) {
							targeted = true;
						}
					}
				}
			}
			
		}

		return targeted;
	}

}
