package org.bsnyder.spring.jms.producer;

import java.sql.Connection;
import java.sql.SQLException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import oracle.jms.AQjmsSession;
import oracle.xdb.XMLType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
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

    protected int numberOfMessages = 10;

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
            final String q = "DEMO_XQUEUE" + hash;
              buffer.append("<this>is xml. count:" + i + " q:" + q + "</this>");
            final int count = i;
            final String payload = buffer.toString();
   
            //jmsTemplate.convertAndSend(q, payload);
            jmsTemplate.send(q, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {

                    AQjmsSession aqsession = (AQjmsSession) session;
                    //            
                    //          java.util.Map map = aqsession.getTypeMap();
                    //        try
                    //      {
                    //        map.put("SYS.XMLTYPE", Class.forName("oracle.xdb.XMLType"));
                    //  }
                    //     catch (ClassNotFoundException cnf)
                    //   {
                    //     throw new JMSException(cnf.getMessage());
                    //  }

                    Connection conn = aqsession.getDBConnection();
                    try {
                        Message message = aqsession.createORAMessage(XMLType.createXML(conn, payload));
                        //TextMessage message = session.createTextMessage(payload);
                        // message.setIntProperty("messageCount", count);
                        LOG.info("Sending message number '{}' queue '{}'", count,q);
                        return message;
                    } catch (SQLException se) {
                        throw new JMSException(se.getMessage());
                    }

                }
            });
        }
    }

}
