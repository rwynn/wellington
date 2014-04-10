package org.github.rwynn.wellington.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(value = "org.github.rwynn.wellington.jms")
public class JmsProperties {

    @NotNull
    private String brokerUrl;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String sessionAcknowledgeModeName;
    private boolean sessionTransacted;
    private boolean useExponentialBackOff;
    private double backOffMultiplier;
    private int maximumRedeliveries;
    private long redeliveryDelay;

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

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

    public String getSessionAcknowledgeModeName() {
        return sessionAcknowledgeModeName;
    }

    public void setSessionAcknowledgeModeName(String sessionAcknowledgeModeName) {
        this.sessionAcknowledgeModeName = sessionAcknowledgeModeName;
    }

    public boolean isSessionTransacted() {
        return sessionTransacted;
    }

    public void setSessionTransacted(boolean sessionTransacted) {
        this.sessionTransacted = sessionTransacted;
    }

    public boolean isUseExponentialBackOff() {
        return useExponentialBackOff;
    }

    public void setUseExponentialBackOff(boolean useExponentialBackOff) {
        this.useExponentialBackOff = useExponentialBackOff;
    }

    public double getBackOffMultiplier() {
        return backOffMultiplier;
    }

    public void setBackOffMultiplier(double backOffMultiplier) {
        this.backOffMultiplier = backOffMultiplier;
    }

    public int getMaximumRedeliveries() {
        return maximumRedeliveries;
    }

    public void setMaximumRedeliveries(int maximumRedeliveries) {
        this.maximumRedeliveries = maximumRedeliveries;
    }

    public long getRedeliveryDelay() {
        return redeliveryDelay;
    }

    public void setRedeliveryDelay(long redeliveryDelay) {
        this.redeliveryDelay = redeliveryDelay;
    }
}
