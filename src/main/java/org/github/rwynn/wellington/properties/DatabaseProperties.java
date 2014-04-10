package org.github.rwynn.wellington.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(value = "org.github.rwynn.wellington.database")
public class DatabaseProperties {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String url;
    @NotNull
    private String driverClassName;
    @NotNull
    private String packagesToScan;
    private boolean showSql;
    private boolean generatedDDL;
    @NotNull
    private String databaseName;
    @NotNull
    private String jooqDialect;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(String packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public boolean isGeneratedDDL() {
        return generatedDDL;
    }

    public void setGeneratedDDL(boolean generatedDDL) {
        this.generatedDDL = generatedDDL;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getJooqDialect() {
        return jooqDialect;
    }

    public void setJooqDialect(String jooqDialect) {
        this.jooqDialect = jooqDialect;
    }
}
