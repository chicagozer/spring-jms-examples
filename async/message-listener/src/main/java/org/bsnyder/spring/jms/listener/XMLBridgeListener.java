/*
	 File: $HeadURL$
	 
	 Version: $Revision$
	 Description: This program is intended to be used for .......
	 Author: $Author$
	 Last Updated: $Date$
	 
	 
*/

package org.bsnyder.spring.jms.listener;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import oracle.net.aso.r;
import org.apache.commons.io.IOUtils;
import org.bsnyder.spring.jaxb.data.Person;
import org.bsnyder.spring.jaxb.data.SimplePerson;
import org.bsnyder.spring.jaxb.data.Site;
import org.bsnyder.spring.jdbc.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ErrorHandler;
import org.w3c.dom.Document;

public class XMLBridgeListener implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(XMLBridgeListener.class);

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

    @Transactional
    public void handleMessage(Document xmlDoc)  {

        try
        {
        DOMSource domSource = new DOMSource(xmlDoc);
        LOG.debug("DOMSource:" + domSource.getClass().getName());
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);

        LOG.info("Received xml payload:" + writer.toString());

        JAXBContext jaxbContext = JAXBContext.newInstance(SimplePerson.class);
        LOG.debug("JAXBContext:" + jaxbContext.getClass().getName());

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
       // Request r = (Request) jaxbUnmarshaller.unmarshal(domSource);
        SimplePerson p = (SimplePerson)jaxbUnmarshaller.unmarshal(domSource);

        LOG.info("request is " + p.getLASTNAME());
        
        // JEM TODO get the converter to handle Documents
        
        String msg  = asString(jaxbContext,p);
        // jmsTemplate.convertAndSend("DEMO_XQUEUE1", msg);
          jmsTemplate.convertAndSend("DEMO_XQUEUE1", xmlDoc);
         
          // im not happy - leave!
        //throw new Exception("forced");
        }
        catch (TransformerException e)
        {
              LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (JAXBException e) {
              LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
       
        catch (JmsException e) {
              LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
       
        
    }
    
    public String asString(
                        JAXBContext pContext, 
                        Object pObject)
                            throws 
                                JAXBException {

    java.io.StringWriter sw = new StringWriter();

    Marshaller marshaller = pContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.marshal(pObject, sw);

    return sw.toString();
}
    
    public void handleMessage(InputStream stream) throws Exception, JAXBException, TransformerConfigurationException, TransformerException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Site.class);
        LOG.debug("JAXBContext:" + jaxbContext.getClass().getName() + " Stream is:" + stream.getClass().getName());

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
       // Request r = (Request) jaxbUnmarshaller.unmarshal(domSource);
       SimplePerson p = (SimplePerson)jaxbUnmarshaller.unmarshal(stream);
        //Site s = (Site)jaxbUnmarshaller.unmarshal(stream);
        LOG.info("unmarshalled it");
        //LOG.info("request is " + p.getLASTNAME());
        //dao.insert(p.getLASTNAME(), p.getFIRSTNAME(), p.getEMAIL());
        String msg  = asString(jaxbContext,p);
         jmsTemplate.convertAndSend("DEMO_QUEUE1", msg);
         
        
        // im not happy - leave!
        //throw new Exception("forced out of handleMessage:stream");
    }

    @Override
    public void handleError(Throwable thrwbl) {
        LOG.error("couldn't handle it", thrwbl);
        throw new UnsupportedOperationException("TODO: Implement some error handling."); //To change body of generated methods, choose Tools | Templates.
    }
}
