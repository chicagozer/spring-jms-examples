package org.bsnyder.spring.jms;

import javax.jms.JMSException;

import org.bsnyder.spring.jms.producer.SimpleMessageProducer;
import org.bsnyder.spring.jms.producer.XMLSimpleMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class drives the example from the producer side. It loads the Spring
 * {@link ApplicationContext} and sends messages. The entire configuration for
 * this app is held in <tt>src/main/resources/jms-context.xml</tt>.
 *
 * @author bsnyder
 *
 */
public class ProducerApp {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerApp.class);

    /**
     * Run the app and tell the producer to send its messages.
     *
     * @param args
     * @throws JMSException
     */
    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/producer-jms-context.xml", ProducerApp.class);
        SimpleMessageProducer producer = (SimpleMessageProducer) context.getBean("messageProducer");

        producer.setGroupSize(1);
          producer.setPad(50000);
          producer.setNumberOfMessages(10);

                    producer.sendGroup();
      

        XMLSimpleMessageProducer xproducer = (XMLSimpleMessageProducer) context.getBean("xmlMessageProducer");
        xproducer.sendMessages();
    }

}
