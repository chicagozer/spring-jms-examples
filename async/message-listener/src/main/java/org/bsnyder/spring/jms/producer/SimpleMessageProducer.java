package org.bsnyder.spring.jms.producer;

import java.lang.reflect.InvocationTargetException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.bsnyder.spring.utils.DebugUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.Transactional;


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

    protected int numberOfMessages = 1;
    protected int pad = 500000;
    protected int groupSize = 1;

    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    public void sendGroup() throws JMSException, InvocationTargetException
    {
        for (int i=0;i<groupSize;i++)
            sendMessages();
    }

    @Transactional(rollbackFor=JMSException.class)
    public void sendMessages() throws JMSException, InvocationTargetException {
        
         //DebugUtils.transactionRequired("SimpleMessageProducer.sendMessages");
         
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

            /*
            
            <xs:element name="Person">
    <xs:complexType>
      <xs:sequence>
        <xs:element type="xs:string" name="PKID"/>
        <xs:element type="xs:string" name="LAST_NAME"/>
        <xs:element type="xs:string" name="FIRST_NAME"/>
        <xs:element type="xs:string" name="EMAIL"/>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
            */
            
            
            buffer.append("<SimplePerson><PKID>id</PKID><LAST_NAME>last</LAST_NAME>");
            buffer.append("<FIRST_NAME>first</FIRST_NAME>");
            for (int k=0;k<pad;k++)
                buffer.append(" ");
            buffer.append("<EMAIL>test@aol.com</EMAIL></SimplePerson>");
            final int count = i;
            final String payload = buffer.toString();

            jmsTemplate.send(q, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(payload);
                    message.setIntProperty("messageCount", count);
                    LOG.debug("Sending message number '{}' queue '{}'", count, q);
                    return message;
                }
            });
        }
        //let's barf
       //throw new JMSException("barf");
    }

}
