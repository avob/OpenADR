package com.avob.openadr.server.common.vtn.service.dtomapper;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;

@Service
public class AbstractUserMapper extends DozerConverter<AbstractUser, String> {

	public AbstractUserMapper() {
		super(AbstractUser.class, String.class);
	}

	@Override
	public String convertTo(AbstractUser source, String destination) {
		if (source == null) {
			return null;
		}
		return source.getUsername();
	}

	@Override
	public Ven convertFrom(String source, AbstractUser destination) {
		return null;
	}

}
