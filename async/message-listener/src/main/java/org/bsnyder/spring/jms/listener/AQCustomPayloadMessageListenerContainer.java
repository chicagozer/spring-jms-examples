/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsnyder.spring.jms.listener;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import oracle.jms.AQjmsSession;
import oracle.xdb.XMLType;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 *
 * @author jim
 */
public class AQCustomPayloadMessageListenerContainer extends
		DefaultMessageListenerContainer {

	@Override
	protected MessageConsumer createConsumer(Session session,
			Destination destination) throws JMSException {

		return ((AQjmsSession) session).createConsumer(destination,
				getMessageSelector(),
				XMLType.getORADataFactory(), null,
				isPubSubNoLocal());

	}
}
