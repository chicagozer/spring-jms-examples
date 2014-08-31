/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsnyder.spring.jms;

import java.util.Hashtable;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

/**
 *
 * @author jim
 */
public class Launch {
    
    public static void main(String[] args) throws Exception
    {
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put("java.naming.factory.initial","oracle.jms.AQjmsInitialContextFactory");
        env.put("db_url","jdbc:oracle:thin:@oel64:1521:xe");
         env.put("java.naming.security.principal","aqadmin");
         env.put("java.naming.security.credentials","aqadmin");
        
       InitialContext ctx = new InitialContext(env);
NamingEnumeration<NameClassPair> list = ctx.list("Queues/");
while (list.hasMore()) {
  System.out.println(list.next().getName());
}
    }
    
}
