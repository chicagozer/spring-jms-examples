package org.bsnyder.spring.jms.producer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiTemplate;

public class XMLSimpleMessageProducer {

    private static final Logger LOG = LoggerFactory.getLogger(XMLSimpleMessageProducer.class);

    protected JmsTemplate jmsTemplate;

    JndiTemplate jndiTemplate;

    public JndiTemplate getJndiTemplate() {
        return jndiTemplate;
    }

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }

    protected int numberOfMessages = 0;

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessages() throws JMSException, IOException, FileNotFoundException {
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

            final String q = "DEMO_XQUEUE" + hash;
            buffer.append("<SimplePerson><PKID>id</PKID><LAST_NAME>last</LAST_NAME>");
            buffer.append("<FIRST_NAME>first</FIRST_NAME><EMAIL>test@aol.com</EMAIL></SimplePerson>");
            final int count = i;
            //final String payload = buffer.toString();
            
            
            final String payload = new String(Files.readAllBytes(Paths.get("/Users/jmittler/Downloads/standard.xml")));
            jmsTemplate.convertAndSend(q, payload);
            //jmsTemplate.convertAndSend(q,is);
            LOG.info("Sending message number '{}' queue '{}'", count, q);

        }
    }

}
