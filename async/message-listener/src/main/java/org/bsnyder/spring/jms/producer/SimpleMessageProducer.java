package org.bsnyder.spring.jms.producer;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jndi.JndiTemplate;

public class SimpleMessageProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleMessageProducer.class);

    protected JmsTemplate jmsTemplate;

    JndiTemplate jndiTemplate;

    public JndiTemplate getJndiTemplate() {
        return jndiTemplate;
    }

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }

    protected int numberOfMessages = 100;

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessages() throws JMSException {
        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < numberOfMessages; ++i) {
            
           
            buffer.setLength(0);
           // buffer.append("### Message '").append(i).append("' sent at: ").append(new Date());
            
           // for (int k = 0; k < 10000; k++){
             // buffer.append(" ");
            //}           

           
            

            String s = String.valueOf(i);
            int hash = 0;
            for (int j = 0; j < s.length(); j++) {
                hash = (31 * hash + s.charAt(j)) % 10;
            }

            //jmsTemplate.setDefaultDestination(d);
            final String q = "DEMO_QUEUE" + hash;
            
             buffer.append("<this>is xml. count:" + i + " q:" + q + "</this>");
            final int count = i;
            final String payload = buffer.toString();
            
            
            jmsTemplate.send(q, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(payload);
                    message.setIntProperty("messageCount", count);
                    LOG.info("Sending message number '{}' queue '{}'", count,q);
                    return message;
                }
            });
        }
    }

}
