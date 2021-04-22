package com.avob.openadr.server.common.vtn.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.known.KnownReport;
import com.avob.openadr.server.common.vtn.models.known.KnownReportId;
import com.avob.openadr.server.common.vtn.models.known.KnownSignal;
import com.avob.openadr.server.common.vtn.models.known.KnownSignalId;
import com.avob.openadr.server.common.vtn.models.known.KnownUnit;
import com.avob.openadr.server.common.vtn.models.known.KnownUnitId;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextReport;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextReportDao;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextSignal;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextSignalDao;

@Service
public class VenMarketContextService {

	@Resource
	private VenMarketContextDao venMarketcontextDao;

	@Resource
	private VenMarketContextSignalDao venMarketcontextSignalDao;

	@Resource
	private VenMarketContextReportDao venMarketcontextReportDao;

	@Resource
	private KnownSignalService knownSignalService;

	@Resource
	private KnownReportService knownReportService;

	@Resource
	private KnownUnitService knownUnitService;

	public VenMarketContext prepare(VenMarketContextDto dto) {
		VenMarketContext context = new VenMarketContext(dto.getName(), dto.getDescription());
		context.setColor(dto.getColor());
		context.setCreatedTimestamp(System.currentTimeMillis());
		return context;
	}

	public VenMarketContext save(VenMarketContext entity) {

		Set<KnownUnit> units = new HashSet<>();
		Set<KnownSignal> signals = new HashSet<>();
		Set<KnownReport> reports = new HashSet<>();

		VenMarketContext save = venMarketcontextDao.save(entity);
		if (entity.getSignals() != null) {
			entity.getSignals().forEach(sig -> {
				sig.setVenMarketContext(save);
			});
			venMarketcontextSignalDao.saveAll(entity.getSignals());

			List<KnownSignal> collect = entity.getSignals().stream().map(s -> {
				return signalFrom(s);
			}).collect(Collectors.toList());

			signals.addAll(collect);
			units.addAll(signals.stream().map(s -> {
				return s.getKnownSignalId().getUnit();
			}).collect(Collectors.toList()));
		}

		if (entity.getReports() != null) {
			entity.getReports().forEach(report -> {
				report.setVenMarketContext(save);
			});
			venMarketcontextReportDao.saveAll(entity.getReports());

			List<KnownReport> collect = entity.getReports().stream().map(s -> {
				return reportFrom(s);
			}).collect(Collectors.toList());

			reports.addAll(collect);
			units.addAll(reports.stream().map(r -> {
				return r.getKnownReportId().getUnit();
			}).collect(Collectors.toList()));

		}

		knownUnitService.save(units);
		knownSignalService.save(signals);
		knownReportService.save(reports);

		return save;
	}

	public Iterable<VenMarketContext> findAll() {
		return venMarketcontextDao.findAll();
	}

	public VenMarketContext findOneByName(String name) {
		return venMarketcontextDao.findOneByName(name);
	}

	public Optional<VenMarketContext> findById(Long id) {
		return venMarketcontextDao.findById(id);
	}

	public List<VenMarketContext> findByNameIn(List<String> name) {
		return venMarketcontextDao.findByNameIn(name);
	}

	public void delete(VenMarketContext venMarketContext) {
		venMarketcontextDao.delete(venMarketContext);
	}

	public long count() {
		return venMarketcontextDao.count();
	}

	private KnownUnit unitFrom(ItemBase itemBase) {
		KnownUnit unit = new KnownUnit();
		KnownUnitId knownUnitId = new KnownUnitId();
		knownUnitId.setItemDescription(itemBase.getItemDescription());
		knownUnitId.setItemUnits(itemBase.getItemUnits());
		knownUnitId.setXmlType(itemBase.getXmlType());
		unit.setKnownUnitId(knownUnitId);
		unit.setAttributes(itemBase.getAttributes());
		return unit;
	}

	private KnownSignal signalFrom(VenMarketContextSignal signal) {
		KnownSignal sig = new KnownSignal();
		KnownSignalId knownSignalId = new KnownSignalId();
		knownSignalId.setSignalName(signal.getSignalName());
		knownSignalId.setSignalType(signal.getSignalType());
		knownSignalId.setUnit(unitFrom(signal.getItemBase()));
		sig.setKnownSignalId(knownSignalId);
		return sig;
	}

	private KnownReport reportFrom(VenMarketContextReport report) {
		KnownReport rep = new KnownReport();
		KnownReportId knownReportId = new KnownReportId();
		knownReportId.setReportName(report.getReportName());
		knownReportId.setReportType(report.getReportType());
		knownReportId.setReadingType(report.getReadingType());
		knownReportId.setPayloadBase(report.getPayloadBase());
		knownReportId.setUnit(unitFrom(report.getItemBase()));

		rep.setKnownReportId(knownReportId);
		return rep;
	}

}
