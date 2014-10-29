/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsnyder.spring.jms;

import java.util.Hashtable;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import oracle.jms.AQjmsFactory;

/**
 *
 * @author jmittler
 */
public class SetupJNDI {

    public static void main(String[] args) throws Exception {
        // Set up environment for creating the initial context
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.fscontext.RefFSContextFactory");
        env.put(Context.PROVIDER_URL, "file:/var/tmp/aq.jndi");
        Context ctx = new InitialContext(env);
        // Perform the bind

        String host = "dbh6";
        String ora_sid = "dev2o11";
        String driver = "thin";
        int port = 1521;
        QueueConnectionFactory qcf;

        qcf = AQjmsFactory.getQueueConnectionFactory(host, ora_sid, port, driver);

        ctx.bind("QueueConnectionFactory", qcf);
       // ctx.bind("DEMO_QUEUE1", q1);

    }
}
