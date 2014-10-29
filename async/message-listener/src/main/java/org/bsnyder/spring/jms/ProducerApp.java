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

        for (int i = 1; i <= 501; i += 50) {
            producer.setGroupSize(i);
            for (int j = 0; j <= 500000; j += 100000) {
                producer.setPad(j);
                for (int k = 1; k <= 101; k += 20) {
                    producer.setNumberOfMessages(k);

                    producer.sendGroup();
                }
            }
        }

        XMLSimpleMessageProducer xproducer = (XMLSimpleMessageProducer) context.getBean("xmlMessageProducer");
        xproducer.sendMessages();
    }

}
