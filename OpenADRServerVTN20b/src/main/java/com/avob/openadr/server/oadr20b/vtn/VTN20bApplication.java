package com.avob.openadr.server.oadr20b.vtn;

import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketMessagingAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketRequesterAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketStrategiesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.rsocket.RSocketSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VTNEmbeddedServletContainerCustomizer;
import com.avob.openadr.server.common.vtn.VtnConfig;

@SpringBootApplication
@EnableJms
@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class,
		AopAutoConfiguration.class,
		ArtemisAutoConfiguration.class,
		BatchAutoConfiguration.class,
		CacheAutoConfiguration.class,
		CacheAutoConfiguration.class,
		CassandraAutoConfiguration.class,
		CassandraDataAutoConfiguration.class,
		CassandraReactiveDataAutoConfiguration.class,
		CassandraReactiveRepositoriesAutoConfiguration.class,
		CassandraRepositoriesAutoConfiguration.class,
		ClientHttpConnectorAutoConfiguration.class,
		CodecsAutoConfiguration.class,
		CouchbaseAutoConfiguration.class,
		CouchbaseDataAutoConfiguration.class,
		CouchbaseReactiveDataAutoConfiguration.class,
		CouchbaseReactiveRepositoriesAutoConfiguration.class,
		CouchbaseRepositoriesAutoConfiguration.class,
		ElasticsearchDataAutoConfiguration.class,
		ElasticsearchRepositoriesAutoConfiguration.class,
		ElasticsearchRestClientAutoConfiguration.class,
		EmbeddedLdapAutoConfiguration.class,
		EmbeddedMongoAutoConfiguration.class,
		ErrorWebFluxAutoConfiguration.class,
		FlywayAutoConfiguration.class,
		FreeMarkerAutoConfiguration.class,
		GroovyTemplateAutoConfiguration.class,
		GsonAutoConfiguration.class,
		H2ConsoleAutoConfiguration.class,
		HazelcastAutoConfiguration.class,
		HazelcastJpaDependencyAutoConfiguration.class,
		HttpHandlerAutoConfiguration.class,
		HypermediaAutoConfiguration.class,
		InfluxDbAutoConfiguration.class,
		IntegrationAutoConfiguration.class,
		JdbcRepositoriesAutoConfiguration.class,
		JerseyAutoConfiguration.class,
		JndiConnectionFactoryAutoConfiguration.class,
		JndiDataSourceAutoConfiguration.class,
		JooqAutoConfiguration.class,
		JsonbAutoConfiguration.class,
		KafkaAutoConfiguration.class,
		LdapAutoConfiguration.class,
		LdapRepositoriesAutoConfiguration.class,
		LiquibaseAutoConfiguration.class,
		MailSenderAutoConfiguration.class,
		MailSenderValidatorAutoConfiguration.class,
		MessageSourceAutoConfiguration.class,
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class,
		MongoReactiveAutoConfiguration.class,
		MongoReactiveDataAutoConfiguration.class,
		MongoReactiveRepositoriesAutoConfiguration.class,
		MongoRepositoriesAutoConfiguration.class,
		MustacheAutoConfiguration.class,
		Neo4jDataAutoConfiguration.class,
		Neo4jRepositoriesAutoConfiguration.class,
		OAuth2ClientAutoConfiguration.class,
		OAuth2ResourceServerAutoConfiguration.class,
		ProjectInfoAutoConfiguration.class,
		QuartzAutoConfiguration.class,
		R2dbcAutoConfiguration.class,
		R2dbcDataAutoConfiguration.class,
		R2dbcRepositoriesAutoConfiguration.class,
		R2dbcTransactionManagerAutoConfiguration.class,
		RSocketMessagingAutoConfiguration.class,
		RSocketRequesterAutoConfiguration.class,
		RSocketSecurityAutoConfiguration.class,
		RSocketServerAutoConfiguration.class,
		RSocketStrategiesAutoConfiguration.class,
		RabbitAutoConfiguration.class,
		ReactiveElasticsearchRepositoriesAutoConfiguration.class,
		ReactiveElasticsearchRestClientAutoConfiguration.class,
		ReactiveOAuth2ClientAutoConfiguration.class,
		ReactiveOAuth2ResourceServerAutoConfiguration.class,
		ReactiveSecurityAutoConfiguration.class,
		ReactiveUserDetailsServiceAutoConfiguration.class,
		ReactiveWebServerFactoryAutoConfiguration.class,
		RedisAutoConfiguration.class,
		RedisReactiveAutoConfiguration.class,
		RedisRepositoriesAutoConfiguration.class,
		RepositoryRestMvcAutoConfiguration.class,
		Saml2RelyingPartyAutoConfiguration.class,
		SendGridAutoConfiguration.class,
		SessionAutoConfiguration.class,
		SolrAutoConfiguration.class,
		SolrRepositoriesAutoConfiguration.class,
		TaskExecutionAutoConfiguration.class,
		ThymeleafAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class,
		WebClientAutoConfiguration.class,
		WebFluxAutoConfiguration.class,
		WebMvcAutoConfiguration.class,
		WebServiceTemplateAutoConfiguration.class,
		WebServicesAutoConfiguration.class,
		WebSocketMessagingAutoConfiguration.class,
		WebSocketReactiveAutoConfiguration.class,
		WebSocketServletAutoConfiguration.class,
		XADataSourceAutoConfiguration.class,
 })
@ComponentScan(basePackages = { "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
@EnableJpaRepositories({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
@EntityScan({ "com.avob.openadr.server.common.vtn", "com.avob.openadr.server.oadr20b.vtn" })
public class VTN20bApplication {

	@Resource
	private VtnConfig vtnConfig;

	private VTNEmbeddedServletContainerCustomizer vtnEmbeddedServletContainerCustomizer;

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> servletContainerCustomizer() {

		vtnEmbeddedServletContainerCustomizer = new VTNEmbeddedServletContainerCustomizer(vtnConfig.getPort(),
				vtnConfig.getContextPath(), vtnConfig.getSslContext(),
				Oadr20bSecurity.getProtocols(), Oadr20bSecurity.getCiphers());

		return vtnEmbeddedServletContainerCustomizer;
	}

	@Bean
	@Profile({ "!test" })
	public Oadr20bJAXBContext jaxbContextProd() throws OadrSecurityException, JAXBException {
		if (vtnConfig.getValidateOadrPayloadAgainstXsd()
				&& vtnConfig.getValidateOadrPayloadAgainstXsdFilePath() != null) {
			return Oadr20bJAXBContext.getInstance(vtnConfig.getValidateOadrPayloadAgainstXsdFilePath());
		}
		return Oadr20bJAXBContext.getInstance();
	};

	@Bean
	@Profile({ "test" })
	public Oadr20bJAXBContext jaxbContextTest() throws JAXBException, SAXException {
		return Oadr20bJAXBContext.getInstance();
	};

	public static void main(String[] args) throws IOException {
		SpringApplication.run(VTN20bApplication.class, args);
	}
}
