package org.github.rwynn.wellington.persistence;

import org.github.rwynn.wellington.properties.ConnectionPoolProperties;
import org.github.rwynn.wellington.properties.DatabaseProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceConfigTest {

    PersistenceConfig persistenceConfig = new PersistenceConfig();

    @Test
    public void testYamlMapFactoryBean() {
        YamlMapFactoryBean yamlMapFactoryBean = persistenceConfig.yamlMapFactoryBean();
        Map<String, Object> base = yamlMapFactoryBean.getObject();
        assertTrue(base.containsKey("unmapped"));
        assertTrue(base.containsKey("requests"));
        assertTrue(base.get("unmapped") instanceof Map);
        assertTrue(base.get("requests") instanceof Map);
    }

    @Test
    public void testDatasource() {
        DatabaseProperties databaseProperties = new DatabaseProperties();
        databaseProperties.setUsername("user");
        databaseProperties.setPassword("pass");
        databaseProperties.setUrl("jdbc:postgresql://localhost:5432/spring");
        databaseProperties.setDriverClassName("org.postgresql.Driver");
        ConnectionPoolProperties connectionPoolProperties = new ConnectionPoolProperties();
        connectionPoolProperties.setJmxEnabled(false);
        org.apache.tomcat.jdbc.pool.DataSource dataSource = (org.apache.tomcat.jdbc.pool.DataSource) persistenceConfig.dataSource(databaseProperties, connectionPoolProperties);
        assertEquals("user", dataSource.getUsername());
        //assertEquals("pass", dataSource.getPassword());
        assertEquals("jdbc:postgresql://localhost:5432/spring", dataSource.getUrl());
    }

    @Test
    public void testEntityManagerFactory() {
        DatabaseProperties databaseProperties = new DatabaseProperties();
        databaseProperties.setUsername("user");
        databaseProperties.setPassword("pass");
        databaseProperties.setUrl("jdbc:postgresql://localhost:5432/spring");
        databaseProperties.setDriverClassName("org.postgresql.Driver");
        databaseProperties.setPackagesToScan("foo");
        databaseProperties.setDatabaseName("POSTGRESQL");
        ConnectionPoolProperties connectionPoolProperties = new ConnectionPoolProperties();
        org.apache.tomcat.jdbc.pool.DataSource dataSource = (org.apache.tomcat.jdbc.pool.DataSource) persistenceConfig.dataSource(databaseProperties, connectionPoolProperties);
        JpaVendorAdapter jpaVendorAdapter = persistenceConfig.jpaVendorAdapter(databaseProperties);
        LocalContainerEntityManagerFactoryBean entityManagerFactory = persistenceConfig.entityManagerFactory(databaseProperties, dataSource, jpaVendorAdapter);
        assertEquals(dataSource, entityManagerFactory.getDataSource());
        assertEquals(jpaVendorAdapter, entityManagerFactory.getJpaVendorAdapter());

    }

    @Test
    public void testTransactionManager() {
        DataSource dataSource = Mockito.mock(DataSource.class);
        JpaTransactionManager transactionManager = (JpaTransactionManager) persistenceConfig.transactionManager(dataSource);
        assertEquals(dataSource, transactionManager.getDataSource());
    }
}
