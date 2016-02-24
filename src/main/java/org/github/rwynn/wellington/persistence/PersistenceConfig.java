package org.github.rwynn.wellington.persistence;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.github.rwynn.wellington.properties.ConnectionPoolProperties;
import org.github.rwynn.wellington.properties.DatabaseProperties;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public class PersistenceConfig {

    @Bean
    public YamlMapFactoryBean yamlMapFactoryBean() {
        YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
        yamlMapFactoryBean.setResources(new Resource[] { new ClassPathResource("dataErrors.yml") });
        return yamlMapFactoryBean;
    }

    @Bean
    public DataSource dataSource(DatabaseProperties databaseProperties, ConnectionPoolProperties connectionPoolProperties) {
        PoolProperties p = new PoolProperties();
        p.setUrl(databaseProperties.getUrl());
        p.setDriverClassName(databaseProperties.getDriverClassName());
        p.setUsername(databaseProperties.getUsername());
        p.setPassword(databaseProperties.getPassword());
        p.setJmxEnabled(connectionPoolProperties.isJmxEnabled());
        p.setTestWhileIdle(connectionPoolProperties.isTestWhileIdle());
        p.setTestOnBorrow(connectionPoolProperties.isTestOnBorrow());
        p.setValidationQuery(connectionPoolProperties.getValidationQuery());
        p.setTestOnReturn(connectionPoolProperties.isTestOnReturn());
        p.setValidationInterval(connectionPoolProperties.getValidationInterval());
        p.setTimeBetweenEvictionRunsMillis(connectionPoolProperties.getTimeBetweenEvictionRunsMillis());
        p.setMaxActive(connectionPoolProperties.getMaxActive());
        p.setInitialSize(connectionPoolProperties.getInitialSize());
        p.setMaxWait(connectionPoolProperties.getMaxWait());
        p.setRemoveAbandonedTimeout(connectionPoolProperties.getRemoveAbandonedTimeout());
        p.setMinEvictableIdleTimeMillis(connectionPoolProperties.getMinEvictableIdleTimeMillis());
        p.setMinIdle(connectionPoolProperties.getMinIdle());
        p.setLogAbandoned(connectionPoolProperties.isLogAbandoned());
        p.setRemoveAbandoned(connectionPoolProperties.isRemoveAbandoned());
        org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setPoolProperties(p);
        return datasource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DatabaseProperties databaseProperties, DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan(databaseProperties.getPackagesToScan());
        return lef;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(DatabaseProperties databaseProperties) {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(databaseProperties.isShowSql());
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        hibernateJpaVendorAdapter.setDatabase(Database.valueOf(databaseProperties.getDatabaseName()));
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
