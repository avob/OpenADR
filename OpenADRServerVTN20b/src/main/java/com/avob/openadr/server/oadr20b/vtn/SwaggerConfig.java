package com.avob.openadr.server.oadr20b.vtn;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.VenReportDto;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ConditionalOnProperty(name = "vtn.swagger")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final String securitySchemaOAuth2 = "oauth2schema";
	public static final String authorizationScopeGlobal = "global";
	public static final String authorizationScopeGlobalDesc = "accessEverything";

	@Autowired
	private TypeResolver typeResolver;

	private ApiInfo apiInfo() {
		String name = "Bertrand Zanni";
		String url = "http://avob.com";
		String email = "bzanni@avob.com";
		Contact contact = new Contact(name, url, email);

//		Collection<VendorExtension> vendors = new ArrayList<>();
		ApiInfo apiInfo = new ApiInfo("Avob VTN Rest API", "Description", "API TOS", "Terms of service", contact,
				"License of API", "API license URL", new ArrayList<>());

		return apiInfo;
	}

	private Predicate<String> internalPath() {
		return Predicates.not(PathSelectors.regex(".*OpenADR2.*|.*error.*|.*manage.*"));
	}

//	private BasicAuth apiKey() {
//		return new BasicAuth("basicAuth");
//	}
//
//	private List<SecurityContext> securityContext() {
//		AuthorizationScope[] authScopes = new AuthorizationScope[0];
//		SecurityReference securityReference = SecurityReference.builder().reference("basicAuth").scopes(authScopes)
//				.build();
//
//		return Arrays.asList(SecurityContext.builder().securityReferences(Arrays.asList(securityReference)).build());
//	}

	@Bean
	public Docket api() {
		Docket build = new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(internalPath()).build();
//				.securitySchemes(Arrays.asList(apiKey()));
//				.securityContexts(securityContext());

		build.protocols(Sets.newHashSet("https"));
		build.apiInfo(apiInfo());
		build.additionalModels(typeResolver.resolve(VenReportDto.class));
		return build;
	}

}
