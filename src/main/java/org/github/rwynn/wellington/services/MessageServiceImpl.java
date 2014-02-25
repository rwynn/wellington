package org.github.rwynn.wellington.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public void registrationCompleted(final String username) {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(username);
                return textMessage;
            }
        });
    }
}
