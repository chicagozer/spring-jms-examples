package org.bsnyder.spring.jms.listener;

import java.io.StringReader;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.bsnyder.spring.jaxb.data.Person;
import org.bsnyder.spring.jaxb.data.SimplePerson;
import org.bsnyder.spring.jdbc.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.Transactional;

public class BridgeListener implements MessageListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(BridgeListener.class);

    protected JmsTemplate jmsTemplate;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public JndiTemplate getJndiTemplate() {
        return jndiTemplate;
    }

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }

    JndiTemplate jndiTemplate;
    @Override
     @Transactional
    public void onMessage(Message message) {
        try {
            final String text = ((TextMessage)message).getText();
            //LOG.info("Received message: {}", text);
            
           JAXBContext jaxbContext = JAXBContext.newInstance(SimplePerson.class);
           StringReader reader = new StringReader(text);
 
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                SimplePerson p = (SimplePerson)jaxbUnmarshaller.unmarshal(reader);
        
     
                 jmsTemplate.send("DEMO_QUEUE1", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(text);
                    
                    LOG.debug("Sending message to queue '{}'",  "DEMO_QUEUE1");
                    return message;
                }
            });
                
        } catch (JMSException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (JAXBException e) {
            LOG.error(e.getMessage(), e);
             throw new RuntimeException(e);
        }
        
        
    }

}
