package org.bsnyder.spring.jms;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class drives the example from the consumer side. It loads the Spring 
 * {@link ApplicationContext} and sends messages. The entire configuration for 
 * this app is held in <tt>src/main/resources/jms-context.xml</tt>. 
 * 
 * @author bsnyder
 *
 */
public class BridgeApp {
	
	private static final Logger LOG = LoggerFactory.getLogger(BridgeApp.class);
    
    /**
     * Run the app and tell the producer to send its messages. 
     * 
     * @param args
     * @throws java.lang.InterruptedException
     * @throws JMSException
     */
    public static void main(String[] args) throws InterruptedException,JMSException {
    	ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("/META-INF/spring/bridge-context.xml", BridgeApp.class);
    	
        Thread.sleep(300000);
        ((ConfigurableApplicationContext)ac).close();
        // No need to actually do anything beyond loading the app context because
    	// this starts up the Spring message listener container. 
    }
    
}
