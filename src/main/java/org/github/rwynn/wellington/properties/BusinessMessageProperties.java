package org.github.rwynn.wellington.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(value = "org.github.rwynn.wellington.business.message")
public class BusinessMessageProperties {

    @NotNull
    private String code;

    @NotNull
    private String userNotFound;

    public String getUserNotFound() {
        return userNotFound;
    }

    public void setUserNotFound(String userNotFound) {
        this.userNotFound = userNotFound;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
