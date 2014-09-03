package org.bsnyder.spring.jms.listener;

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
import org.bsnyder.spring.jaxb.data.Person;
import org.bsnyder.spring.jdbc.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class XMLMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(XMLMessageListener.class);

    protected DAO dao;

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public void handleMessage(Document xmlDoc) throws JAXBException, TransformerConfigurationException, TransformerException {

        DOMSource domSource = new DOMSource(xmlDoc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);

        LOG.info("Received xml payload:" + writer.toString());

        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Person p = (Person) jaxbUnmarshaller.unmarshal(domSource);

        dao.insert(p.getLASTNAME(), p.getFIRSTNAME(), p.getEMAIL());
    }
}
