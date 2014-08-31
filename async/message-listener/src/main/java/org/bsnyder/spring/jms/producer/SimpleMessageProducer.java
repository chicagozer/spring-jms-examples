package org.bsnyder.spring.jms.producer;

import java.util.Date;
import javax.jms.Destination;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jndi.JndiTemplate;

public class SimpleMessageProducer implements ApplicationContextAware {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleMessageProducer.class);
    
    protected JmsTemplate jmsTemplate; 
    
    JndiTemplate jndiTemplate;
    ApplicationContext applicationContext;

    public JndiTemplate getJndiTemplate() {
        return jndiTemplate;
    }

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }
    
    protected int numberOfMessages = 5; 
    
    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMessages() throws JMSException , NamingException {
        final StringBuilder buffer = new StringBuilder(); 
        
        for (int i = 0; i < numberOfMessages; ++i) {
            buffer.setLength(0);
            buffer.append("Message '").append(i).append("' sent at: ").append(new Date());
            
            final int count = i;
            final String payload = buffer.toString();
            
            String s = String.valueOf(i);
            int hash = 0;
            for (int j = 0; j < s.length(); j++)
            hash = (31 * hash + s.charAt(j)) % 5;
            
            
            Destination d = (Destination)applicationContext.getBean("destination" + hash);
            jmsTemplate.setDefaultDestination(d);
            
            jmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(payload); 
                    message.setIntProperty("messageCount", count);
                    LOG.info("Sending message number '{}'", count);
                    return message;
                }
            });
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.applicationContext = ac;
     }
}
