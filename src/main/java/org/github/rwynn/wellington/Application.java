package org.github.rwynn.wellington;

import org.github.rwynn.wellington.jms.JMSConfig;
import org.github.rwynn.wellington.persistence.PersistenceConfig;
import org.github.rwynn.wellington.properties.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
@EnableJpaRepositories
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {JpaBaseConfiguration.class, HibernateJpaAutoConfiguration.class, JmsTemplateAutoConfiguration.class})
@EnableConfigurationProperties({ApplicationProperties.class, DatabaseProperties.class, ConnectionPoolProperties.class, JmsProperties.class, BusinessMessageProperties.class })
@ComponentScan
@Import({ PersistenceConfig.class, JMSConfig.class })
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<Application> applicationClass = Application.class;

}
