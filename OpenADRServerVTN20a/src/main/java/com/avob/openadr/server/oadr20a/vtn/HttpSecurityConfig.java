package com.avob.openadr.server.oadr20a.vtn;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import com.avob.openadr.server.common.vtn.security.BasicAuthenticationManager;
import com.avob.openadr.server.common.vtn.security.DigestAuthenticationProvider;
import com.avob.openadr.server.common.vtn.security.DigestUserDetailsService;

/**
 * Spring security configuration
 * 
 * @author bertrand
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private Oadr20aX509AuthenticatedUserDetailsService oadr20aX509AuthenticatedUserDetailsService;

	@Resource
	private BasicAuthenticationManager basicAuthenticationManager;

	@Resource
	private DigestUserDetailsService digestUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		DigestAuthenticationEntryPoint authenticationEntryPoint = new DigestAuthenticationEntryPoint();
		authenticationEntryPoint.setKey(DigestAuthenticationProvider.DIGEST_KEY);
		authenticationEntryPoint.setRealmName(DigestAuthenticationProvider.DIGEST_REALM);

		DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
		digestAuthenticationFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
		digestAuthenticationFilter.setUserDetailsService(digestUserDetailsService);
		digestAuthenticationFilter.setPasswordAlreadyEncoded(true);

		BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
		basicAuthenticationEntryPoint.setRealmName(BasicAuthenticationManager.BASIC_REALM);

		BasicAuthenticationFilter basicAuthenticationFilter = new BasicAuthenticationFilter(basicAuthenticationManager);

		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().anyRequest().authenticated().and().x509().subjectPrincipalRegex("CN=(.*?)(?:,|$)")
				.authenticationUserDetailsService(oadr20aX509AuthenticatedUserDetailsService);

		http.addFilter(digestAuthenticationFilter).authorizeRequests().anyRequest().authenticated().and()
				.addFilter(basicAuthenticationFilter).authorizeRequests().anyRequest().authenticated();

	}

}
