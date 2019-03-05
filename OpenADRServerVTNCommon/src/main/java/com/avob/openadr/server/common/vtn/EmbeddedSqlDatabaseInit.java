package com.avob.openadr.server.common.vtn;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextDto;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.google.common.collect.Sets;

@Profile("test-functional")
@Configuration
public class EmbeddedSqlDatabaseInit {

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenService venService;

	@PostConstruct
	public void init() {
		String marketContextName = "http://oadr.avob.com";
		String marketContextDescription = "Avob Test Market Context";
		String marketcontextColor = "#90CAF9";
		VenMarketContext marketContext = venMarketContextService
				.prepare(new VenMarketContextDto(marketContextName, marketContextDescription, marketcontextColor));
		venMarketContextService.save(marketContext);
		// rsa test ven
		VenCreateDto dto = new VenCreateDto();
		dto.setUsername("2E:55:12:81:B9:EE:9C:46:72:1D");
		dto.setAuthenticationType("x509");
		dto.setCommonName("test-rsa.oadr.com");
		dto.setOadrProfil("20b");
		Ven prepare = venService.prepare(dto);
		prepare.setVenMarketContexts(Sets.newHashSet(marketContext));
		venService.save(prepare);
		
		// ecc test ven
		dto = new VenCreateDto();
		dto.setUsername("15:97:7B:DE:1C:1F:C6:D2:64:84");
		dto.setAuthenticationType("x509");
		dto.setCommonName("test-ecc.oadr.com");
		dto.setOadrProfil("20b");
		prepare = venService.prepare(dto);
		prepare.setVenMarketContexts(Sets.newHashSet(marketContext));
		venService.save(prepare);
		

	}
}
