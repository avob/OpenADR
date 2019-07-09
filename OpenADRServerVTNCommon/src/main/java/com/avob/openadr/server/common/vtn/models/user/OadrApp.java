package com.avob.openadr.server.common.vtn.models.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oadr_app")
public class OadrApp extends AbstractUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2869155520774908886L;

}
