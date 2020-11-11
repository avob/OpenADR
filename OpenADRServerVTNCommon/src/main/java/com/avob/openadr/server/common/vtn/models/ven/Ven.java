package com.avob.openadr.server.common.vtn.models.ven;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.vengroup.VenGroup;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;

/**
 * Ven persistent object - database representation
 * 
 * @author bertrand
 *
 */
@Entity
@Table(name = "ven")
public class Ven extends AbstractUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4462874293418115268L;

	/**
	 * Ven registered name
	 */
	private String oadrName;

	/**
	 * 20a/20b profil
	 */
	private String oadrProfil;

	/**
	 * http/xmpp transport
	 */
	private String transport;

	/**
	 * push mode url
	 */
	private String pushUrl;

	/**
	 * registrationId
	 */
	@Column(name = "registrationId", unique = true)
	private String registrationId;

	/**
	 * is http pull model
	 */
	private Boolean httpPullModel = true;

	/**
	 * report only
	 */
	private Boolean reportOnly = false;

	/**
	 * support xml signature
	 */
	private Boolean xmlSignature = false;

	private Long pullFrequencySeconds;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ven_vengroup", joinColumns = @JoinColumn(name = "ven_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "vengroup_id", referencedColumnName = "id"))
	private Set<VenGroup> venGroups;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ven_venmarketcontext", joinColumns = @JoinColumn(name = "ven_id"), inverseJoinColumns = @JoinColumn(name = "venmarketcontext_id"))
	private Set<VenMarketContext> venMarketContexts;

	@OneToMany(mappedBy = "ven", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<VenResource> venResources;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ven")
	private List<VenDemandResponseEvent> venDemandResponseEvent;

	private Long lastUpdateDatetime;

	public String getOadrName() {
		return oadrName;
	}

	public void setOadrName(String name) {
		this.oadrName = name;
	}

	public String getOadrProfil() {
		return oadrProfil;
	}

	public void setOadrProfil(String oadrProfil) {
		this.oadrProfil = oadrProfil;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public Set<VenGroup> getVenGroups() {
		return venGroups;
	}

	public void setVenGroup(Set<VenGroup> venGroups) {
		this.venGroups = venGroups;
	}

	/**
	 * @return the pushUrl
	 */
	public String getPushUrl() {
		return pushUrl;
	}

	/**
	 * @param pushUrl the pushUrl to set
	 */
	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public Boolean getHttpPullModel() {
		return httpPullModel;
	}

	public void setHttpPullModel(Boolean httpPullModel) {
		this.httpPullModel = httpPullModel;
	}

	public Boolean getReportOnly() {
		return reportOnly;
	}

	public void setReportOnly(Boolean reportOnly) {
		this.reportOnly = reportOnly;
	}

	public Boolean getXmlSignature() {
		return xmlSignature;
	}

	public void setXmlSignature(Boolean xmlSignature) {
		this.xmlSignature = xmlSignature;
	}

	public Set<VenMarketContext> getVenMarketContexts() {
		return venMarketContexts;
	}

	public void setVenMarketContexts(Set<VenMarketContext> venMarketContexts) {
		this.venMarketContexts = venMarketContexts;
	}

	public void removeGroup(VenGroup venGroup) {

		venGroups.remove(venGroup);

	}

	public void removeMarketContext(VenMarketContext venMarketContext) {

		venMarketContexts.remove(venMarketContext);

	}

	public Set<VenResource> getVenResources() {
		return venResources;
	}

	public void setVenResources(Set<VenResource> venResources) {
		this.venResources = venResources;
	}

	public void removeResource(VenResource venResource) {
		venResources.remove(venResource);

	}

	public Long getPullFrequencySeconds() {
		return pullFrequencySeconds;
	}

	public void setPullFrequencySeconds(Long pullFrequencySeconds) {
		this.pullFrequencySeconds = pullFrequencySeconds;
	}

	public Long getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Long lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public List<VenDemandResponseEvent> getVenDemandResponseEvent() {
		return venDemandResponseEvent;
	}

	public void setVenDemandResponseEvent(List<VenDemandResponseEvent> venDemandResponseEvent) {
		this.venDemandResponseEvent = venDemandResponseEvent;
	}
	
	@Override
	public String toString() {
		return "Ven [oadrName=" + oadrName + ", oadrProfil=" + oadrProfil + ", transport=" + transport + ", pushUrl="
				+ pushUrl + ", registrationId=" + registrationId + ", httpPullModel=" + httpPullModel + ", reportOnly="
				+ reportOnly + ", xmlSignature=" + xmlSignature + ", pullFrequencySeconds=" + pullFrequencySeconds
				+ ", lastUpdateDatetime=" + lastUpdateDatetime + "]";
	}

}
