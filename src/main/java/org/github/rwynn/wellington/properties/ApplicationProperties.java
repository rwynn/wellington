package org.github.rwynn.wellington.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(value = "info.app")
@Component("appProps")
public class ApplicationProperties {

    private String name;
    private String description;
    private String version;

    @NotNull
    private boolean development;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isDevelopment() {
        return development;
    }

    public void setDevelopment(boolean development) {
        this.development = development;
    }
}
