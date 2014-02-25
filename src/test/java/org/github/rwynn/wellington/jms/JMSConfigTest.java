package org.github.rwynn.wellington.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.github.rwynn.wellington.mdp.UserListener;
import org.github.rwynn.wellington.properties.JmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class JMSConfigTest {
    
    JMSConfig jmsConfig = new JMSConfig();

    @Test
    public void testRedeliveryPolicy() {
        JmsProperties jmsProperties = new JmsProperties();
        jmsProperties.setRedeliveryDelay(10L);
        jmsProperties.setUseExponentialBackOff(false);
        jmsProperties.setBackOffMultiplier(10.0);
        jmsProperties.setMaximumRedeliveries(5);
        RedeliveryPolicy redeliveryPolicy = jmsConfig.redeliveryPolicy(jmsProperties);
        assertEquals(10L, redeliveryPolicy.getRedeliveryDelay());
        assertFalse(redeliveryPolicy.isUseExponentialBackOff());
        assertEquals(10.0, redeliveryPolicy.getBackOffMultiplier(), 0.0);
        assertEquals(5, redeliveryPolicy.getMaximumRedeliveries());
    }

    @Test
    public void testJmsRawConnectionFactory() {
        JmsProperties jmsProperties = new JmsProperties();
        jmsProperties.setUsername("user");
        jmsProperties.setPassword("pass");
        jmsProperties.setBrokerUrl("tcp://localhost:61616");
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) jmsConfig.jmsRawConnectionFactory(jmsProperties, redeliveryPolicy);
        assertEquals("user", activeMQConnectionFactory.getUserName());
        assertEquals("pass", activeMQConnectionFactory.getPassword());
        assertEquals("tcp://localhost:61616", activeMQConnectionFactory.getBrokerURL());
        assertEquals(redeliveryPolicy, activeMQConnectionFactory.getRedeliveryPolicy());
    }

    @Test
    public void testJmsConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        CachingConnectionFactory cachingConnectionFactory = (CachingConnectionFactory) jmsConfig.jmsConnectionFactory(activeMQConnectionFactory);
        assertEquals(activeMQConnectionFactory, cachingConnectionFactory.getTargetConnectionFactory());
    }

    @Test
    public void testUserQueue() throws Exception {
        ActiveMQQueue userQueue = jmsConfig.userQueue();
        assertEquals("users", userQueue.getQueueName());
    }

    @Test
    public void testJmsTemplate() {
        ActiveMQQueue userQueue = jmsConfig.userQueue();
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        JmsTemplate jmsTemplate = jmsConfig.jmsTemplate(userQueue, activeMQConnectionFactory);
        assertEquals(activeMQConnectionFactory, jmsTemplate.getConnectionFactory());
        assertEquals(userQueue, jmsTemplate.getDefaultDestination());
    }

    @Test
    public void testMessageListenerContainer() {
        UserListener userListener = new UserListener();
        JMSErrorHandler errorHandler = new JMSErrorHandler();
        JmsProperties jmsProperties = new JmsProperties();
        jmsProperties.setSessionTransacted(true);
        jmsProperties.setSessionAcknowledgeModeName("CLIENT_ACKNOWLEDGE");
        ActiveMQQueue userQueue = jmsConfig.userQueue();
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        DefaultMessageListenerContainer defaultMessageListenerContainer = jmsConfig.messageListenerContainer(activeMQConnectionFactory, userQueue, userListener, jmsProperties, errorHandler);
        assertEquals(userListener, defaultMessageListenerContainer.getMessageListener());
        assertEquals(userQueue, defaultMessageListenerContainer.getDestination());
        assertTrue(defaultMessageListenerContainer.isSessionTransacted());

    }
}
