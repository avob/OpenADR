package com.avob.openadr.server.oadr20b.vtn;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
	private Oadr20bX509AuthenticatedUserDetailsService oadr20bX509AuthenticatedUserDetailsService;

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

		http.cors().and().csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().regexMatchers(HttpMethod.OPTIONS, ".*").permitAll();

		http.authorizeRequests().anyRequest().authenticated().and().x509().subjectPrincipalRegex("CN=(.*?)(?:,|$)")
				.authenticationUserDetailsService(oadr20bX509AuthenticatedUserDetailsService);

		http.addFilter(digestAuthenticationFilter).authorizeRequests().anyRequest().authenticated().and()
				.addFilter(basicAuthenticationFilter).authorizeRequests().anyRequest().authenticated();

		http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest arg0, HttpServletResponse arg1, AuthenticationException arg2)
					throws IOException, ServletException {
				if (arg0.getServletPath().contains("/Ven") || arg0.getServletPath().contains("swagger")
						|| arg0.getServletPath().contains("swagger-resources") || arg0.getServletPath().contains("v2")
						|| arg0.getServletPath().contains("swagger-ui")) {
					arg1.setStatus(HttpStatus.UNAUTHORIZED_401);
					arg1.addHeader("WWW-Authenticate", "Basic");
				} else {
					arg1.setStatus(HttpStatus.FORBIDDEN_403);

				}

			}

		});

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		
		.regexMatchers("/health");
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "OPTIONS", "HEAD", "DELETE"));
        configuration.setAllowedOrigins(Arrays.asList("http://vtn.oadr.com:3000", "http://localhost:3000"));
        configuration.applyPermitDefaultValues();
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	


}
