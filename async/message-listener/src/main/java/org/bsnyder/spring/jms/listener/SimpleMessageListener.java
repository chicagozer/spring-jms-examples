package org.bsnyder.spring.jms.listener;

import java.io.StringReader;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.bsnyder.spring.jaxb.data.Person;
import org.bsnyder.spring.jaxb.data.SimplePerson;
import org.bsnyder.spring.jdbc.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMessageListener implements MessageListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleMessageListener.class);

    protected DAO dao;

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage)message).getText();
            LOG.info("Received message: {}", text);
            
           JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
           StringReader reader = new StringReader(text);
 
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                SimplePerson p = (SimplePerson)jaxbUnmarshaller.unmarshal(reader);
        
       dao.insert(p.getLASTNAME(), p.getFIRSTNAME(), p.getEMAIL());
            
        } catch (JMSException e) {
            LOG.error(e.getMessage(), e);
        } catch (JAXBException e) {
            LOG.error(e.getMessage(), e);
        }
        
        
    }

}
