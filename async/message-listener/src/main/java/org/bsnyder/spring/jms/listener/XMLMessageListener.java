package org.bsnyder.spring.jms.listener;

import java.sql.SQLException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import oracle.jms.AQjmsAdtMessage;
import oracle.xdb.XMLType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLMessageListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(XMLMessageListener.class);

    public void onMessage(Message message) {
        try {
            if (message instanceof AQjmsAdtMessage)
                {
                    AQjmsAdtMessage adt = (AQjmsAdtMessage)message;
                    XMLType payload = (XMLType)adt.getAdtPayload();
                    LOG.info("Received payload: {}", payload.getStringVal());
                }
            }
         catch (JMSException e) {
            LOG.error(e.getMessage(), e);
        }
        catch (SQLException se) {
            LOG.error(se.getMessage(), se);
        }
    }

}
