package com.avob.openadr.server.common.vtn.models.demandresponseevent;

import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventActivePeriod;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.embedded.DemandResponseEventDescriptor;
import com.avob.openadr.server.common.vtn.models.vendemandresponseevent.VenDemandResponseEvent;

/**
 * An entity of DemandResponseEvent
 * 
 * @author bertrand
 *
 */
@Entity
@Table(name = "demandresponseevent")
public class DemandResponseEvent {

	/**
	 * Autogenerated unique id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private Long createdTimestamp = System.currentTimeMillis();

	private Long lastUpdateTimestamp;

	private boolean published = false;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	private List<VenDemandResponseEvent> venDemandResponseEvent;

	private DemandResponseEventDescriptor descriptor;

	private DemandResponseEventActivePeriod activePeriod;

	@OneToOne(fetch = FetchType.EAGER)
	private DemandResponseEventBaseline baseline;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
	private Set<DemandResponseEventSignal> signals;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Target> targets;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public DemandResponseEventDescriptor getDescriptor() {
		if (descriptor == null) {
			descriptor = new DemandResponseEventDescriptor();
		}
		return descriptor;
	}

	public void setDescriptor(DemandResponseEventDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public DemandResponseEventActivePeriod getActivePeriod() {
		if (activePeriod == null) {
			activePeriod = new DemandResponseEventActivePeriod();
		}
		return activePeriod;
	}

	public void setActivePeriod(DemandResponseEventActivePeriod activePeriod) {
		this.activePeriod = activePeriod;
	}

	public Set<DemandResponseEventSignal> getSignals() {
		return signals;
	}

	public void setSignals(Set<DemandResponseEventSignal> signals) {
		this.signals = signals;
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public DemandResponseEventBaseline getBaseline() {
		return baseline;
	}

	public void setBaseline(DemandResponseEventBaseline baseline) {
		this.baseline = baseline;
	}

}
