package org.github.rwynn.wellington.jms;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.github.rwynn.wellington.properties.JmsProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;

public class JMSConfig {

    @Bean
    public RedeliveryPolicy redeliveryPolicy(JmsProperties jmsProperties) {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(jmsProperties.isUseExponentialBackOff());
        redeliveryPolicy.setBackOffMultiplier(jmsProperties.getBackOffMultiplier());
        redeliveryPolicy.setMaximumRedeliveries(jmsProperties.getMaximumRedeliveries());
        redeliveryPolicy.setRedeliveryDelay(jmsProperties.getRedeliveryDelay());
        return redeliveryPolicy;
    }

    @Bean(name = "jmsRaw")
    public ConnectionFactory jmsRawConnectionFactory(JmsProperties jmsProperties, RedeliveryPolicy redeliveryPolicy) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(jmsProperties.getBrokerUrl());
        activeMQConnectionFactory.setUserName(jmsProperties.getUsername());
        activeMQConnectionFactory.setPassword(jmsProperties.getPassword());
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return activeMQConnectionFactory;
    }

    @Bean(name = "jmsCached")
    public ConnectionFactory jmsConnectionFactory(@Qualifier("jmsRaw") ConnectionFactory connectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(connectionFactory);
        return cachingConnectionFactory;
    }

    @Bean
    public ActiveMQQueue userQueue() {
        return new ActiveMQQueue("users");
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQQueue queue, @Qualifier("jmsCached") ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestination(queue);
        return jmsTemplate;
    }

    @Bean
    public DefaultMessageListenerContainer messageListenerContainer(@Qualifier("jmsRaw") ConnectionFactory connectionFactory, ActiveMQQueue queue, MessageListener userListener, JmsProperties jmsProperties, JMSErrorHandler errorHandler) {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setErrorHandler(errorHandler);
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
        defaultMessageListenerContainer.setDestination(queue);
        defaultMessageListenerContainer.setMessageListener(userListener);
        defaultMessageListenerContainer.setSessionTransacted(jmsProperties.isSessionTransacted());
        defaultMessageListenerContainer.setSessionAcknowledgeModeName(jmsProperties.getSessionAcknowledgeModeName());
        return defaultMessageListenerContainer;
    }
}
