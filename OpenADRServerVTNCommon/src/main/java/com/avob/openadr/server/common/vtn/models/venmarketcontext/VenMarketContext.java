package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.ven.Ven;

/**
 * VenMarketContext persistent object - database representation
 * 
 * @author bertrand
 *
 */
@Entity
@Table(name = "venmarketcontext")
public class VenMarketContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8652374934772768274L;

	/**
	 * Autogenerated unique id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@Column(unique = true)
	private String name;

	private String description;

	private String color;

	@ManyToMany(mappedBy = "venMarketContexts")
	private Set<Ven> vens;

	@NotNull
	private Long createdTimestamp;

	private Long lastUpdateTimestamp;

	private VenMarketContextDescriptor descriptor;

	private VenMarketContextActivePeriod activePeriod;

	@OneToOne(fetch = FetchType.EAGER)
	private VenMarketContextBaseline baseline;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "venMarketContext")
	private Set<VenMarketContextSignal> signals;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "venMarketContext")
	private Set<VenMarketContextReport> reports;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Target> targets;

	private VenMarketContextDemandResponseEventScheduleStrategy demandResponseEventScheduleStrategy;

	private VenMarketContextReportSubscriptionStrategy reportSubscriptionStrategy;

	public VenMarketContext() {
	}

	public VenMarketContext(String name, String description) {
		this.name = name;
		this.description = description;
		this.createdTimestamp = System.currentTimeMillis();
	}

	@PreRemove
	public void removeVenGroupFromMarketContext() {
		if (vens != null) {
			for (Ven ven : vens) {
				ven.removeMarketContext(this);
			}
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Ven> getVens() {
		return vens;
	}

	public void setVens(Set<Ven> vens) {
		this.vens = vens;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Long lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public VenMarketContextBaseline getBaseline() {
		return baseline;
	}

	public void setBaseline(VenMarketContextBaseline baseline) {
		this.baseline = baseline;
	}

	public Set<VenMarketContextSignal> getSignals() {
		return signals;
	}

	public void setSignals(Set<VenMarketContextSignal> signals) {
		this.signals = signals;
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	public VenMarketContextDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(VenMarketContextDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public VenMarketContextActivePeriod getActivePeriod() {
		return activePeriod;
	}

	public void setActivePeriod(VenMarketContextActivePeriod activePeriod) {
		this.activePeriod = activePeriod;
	}

	public Set<VenMarketContextReport> getReports() {
		return reports;
	}

	public void setReports(Set<VenMarketContextReport> reports) {
		this.reports = reports;
	}

	public VenMarketContextReportSubscriptionStrategy getReportSubscriptionStrategy() {
		return reportSubscriptionStrategy;
	}

	public void setReportSubscriptionStrategy(VenMarketContextReportSubscriptionStrategy reportSubscriptionStrategy) {
		this.reportSubscriptionStrategy = reportSubscriptionStrategy;
	}

	public VenMarketContextDemandResponseEventScheduleStrategy getDemandResponseEventScheduleStrategy() {
		return demandResponseEventScheduleStrategy;
	}

	public void setDemandResponseEventScheduleStrategy(
			VenMarketContextDemandResponseEventScheduleStrategy demandResponseEventScheduleStrategy) {
		this.demandResponseEventScheduleStrategy = demandResponseEventScheduleStrategy;
	}
}
