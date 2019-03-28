package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEvent;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.DemandResponseEventOptEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOpt;
import com.avob.openadr.server.oadr20b.vtn.models.venopt.VenOptDao;

@Service
public class VenOptService {

	@Resource
	private VenOptDao venOptDao;

	public void create(Ven ven, VenResource resource, VenMarketContext marketContext, String optId, Long start,
			Long end, DemandResponseEventOptEnum venOpt) {
		VenOpt opt = new VenOpt(ven, resource, marketContext, optId, start, end, venOpt);
		venOptDao.save(opt);
	}

	public void create(Ven ven, VenResource resource, VenMarketContext marketContext, DemandResponseEvent event,
			DemandResponseEventOptEnum venOpt) {
		VenOpt opt = new VenOpt(ven, resource, marketContext, event, venOpt);
		venOptDao.save(opt);
	}

	public List<VenOpt> findScheduledOpt(String venUsername, String marketContext, Long start, Long end) {

		if (start == null && end == null && marketContext == null) {
			return venOptDao.findScheduledOpt(venUsername);
		} else if (start == null && end != null && marketContext == null) {
			return venOptDao.findScheduledOptBefore(venUsername, end);
		} else if (start != null && end == null && marketContext == null) {
			return venOptDao.findScheduledOptAfter(venUsername, start);
		} else if (start != null && end != null && marketContext == null) {
			return venOptDao.findScheduledOptBetween(venUsername, start, end);
		} else if (start == null && end == null && marketContext != null) {
			return venOptDao.findScheduledOpt(venUsername, marketContext);
		} else if (start == null && end != null && marketContext != null) {
			return venOptDao.findScheduledOptBefore(venUsername, marketContext, end);
		} else if (start != null && end == null && marketContext != null) {
			return venOptDao.findScheduledOptAfter(venUsername, marketContext, start);
		} else if (start != null && end != null && marketContext != null) {
			return venOptDao.findScheduledOptBetween(venUsername, marketContext, start, end);
		}
		return Collections.emptyList();
	}

	public List<VenOpt> findResourceScheduledOpt(String venUsername, String marketContext, String resourceName,
			Long start, Long end) {
		if (start == null && end == null && marketContext == null) {
			return venOptDao.findResourceScheduledOpt(venUsername, resourceName);
		} else if (start == null && end != null && marketContext == null) {
			return venOptDao.findResourceScheduledOptBefore(venUsername, resourceName, end);
		} else if (start != null && end == null && marketContext == null) {
			return venOptDao.findResourceScheduledOptAfter(venUsername, resourceName, start);
		} else if (start != null && end != null && marketContext == null) {
			return venOptDao.findResourceScheduledOptBetween(venUsername, resourceName, start, end);
		}

		else if (start == null && end == null && marketContext != null) {
			return venOptDao.findResourceScheduledOpt(venUsername, marketContext, resourceName);
		} else if (start == null && end != null && marketContext != null) {
			return venOptDao.findResourceScheduledOptBefore(venUsername, marketContext, resourceName, end);
		} else if (start != null && end == null && marketContext != null) {
			return venOptDao.findResourceScheduledOptAfter(venUsername, marketContext, resourceName, start);
		} else if (start != null && end != null && marketContext != null) {
			return venOptDao.findResourceScheduledOptBetween(venUsername, marketContext, resourceName, start, end);
		}
		return Collections.emptyList();
	}

	public void deleteScheduledOpt(Ven ven, String optId) {
		venOptDao.deleteByVenAndoptId(ven, optId);
	}

}
