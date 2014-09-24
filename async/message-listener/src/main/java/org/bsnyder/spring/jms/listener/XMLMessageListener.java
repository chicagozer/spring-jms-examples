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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import oracle.net.aso.r;
import org.bsnyder.spring.jaxb.data.Person;
import org.bsnyder.spring.jaxb.data.SimplePerson;
import org.bsnyder.spring.jaxb.data.Site;
import org.bsnyder.spring.jdbc.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;
import org.w3c.dom.Document;

public class XMLMessageListener implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(XMLMessageListener.class);

    protected DAO dao;

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public void handleMessage(Document xmlDoc) throws Exception, JAXBException, TransformerConfigurationException, TransformerException {

        DOMSource domSource = new DOMSource(xmlDoc);
        LOG.debug("DOMSource:" + domSource.getClass().getName());
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);

        LOG.info("Received xml payload:" + writer.toString());

        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
        LOG.debug("JAXBContext:" + jaxbContext.getClass().getName());

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
       // Request r = (Request) jaxbUnmarshaller.unmarshal(domSource);
        SimplePerson p = (SimplePerson)jaxbUnmarshaller.unmarshal(domSource);

        LOG.info("request is " + p.getLASTNAME());
        dao.insert(p.getLASTNAME(), p.getFIRSTNAME(), p.getEMAIL());
        
        // im not happy - leave!
        throw new Exception("forced");
    }
    
    
    public void handleMessage(InputStream stream) throws Exception, JAXBException, TransformerConfigurationException, TransformerException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Site.class);
        LOG.debug("JAXBContext:" + jaxbContext.getClass().getName() + " Stream is:" + stream.getClass().getName());

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
       // Request r = (Request) jaxbUnmarshaller.unmarshal(domSource);
       // SimplePerson p = (SimplePerson)jaxbUnmarshaller.unmarshal(stream);
        Site s = (Site)jaxbUnmarshaller.unmarshal(stream);
        LOG.info("unmarshalled it");
        //LOG.info("request is " + p.getLASTNAME());
        //dao.insert(p.getLASTNAME(), p.getFIRSTNAME(), p.getEMAIL());
        
        // im not happy - leave!
        //throw new Exception("forced out of handleMessage:stream");
    }

    @Override
    public void handleError(Throwable thrwbl) {
        LOG.error("couldn't handle it", thrwbl);
        throw new UnsupportedOperationException("TODO: Implement some error handling."); //To change body of generated methods, choose Tools | Templates.
    }
}
