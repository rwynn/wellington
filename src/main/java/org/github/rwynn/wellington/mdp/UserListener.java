package org.github.rwynn.wellington.mdp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class UserListener implements MessageListener {

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("Got a message about a user...");
            TextMessage textMessage = (TextMessage) message;
            logger.info(String.format("User <%s> was just created", textMessage.getText()));
            throw new RuntimeException("something bad happened. please retry.");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
