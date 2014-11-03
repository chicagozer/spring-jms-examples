/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsnyder.spring.jms.listener;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author jmittler
 */
public class XMLMessageConverter extends SimpleMessageConverter {

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {

        if (object instanceof Document) {
            Document doc = (Document) object;
            DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
            LSSerializer lsSerializer = domImplementation.createLSSerializer();
            object = lsSerializer.writeToString(doc);
        }
        return super.toMessage(object, session);

    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {

        try {
            if (message instanceof TextMessage) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;

                builder = factory.newDocumentBuilder();

                TextMessage txtmsg = (TextMessage) message;
                Document doc = builder.parse(new InputSource(new StringReader(txtmsg.getText())));
                return doc;
            } 
        } catch (ParserConfigurationException ex) {
           throw new MessageConversionException("Cannot convert to JMS message.",ex);
        } catch (SAXException ex) {
           throw new MessageConversionException("Cannot convert to JMS message.",ex);
        } catch (IOException ex) {
            throw new MessageConversionException("Cannot convert to JMS message.",ex);
        }
         return super.fromMessage(message);
    }

}
