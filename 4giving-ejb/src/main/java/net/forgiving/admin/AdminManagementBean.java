/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.admin;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author gabalca
 */
@Stateless
@Path("/admin")
//TODO securitzar
public class AdminManagementBean {

    private static final Logger LOG = Logger.getLogger(AdminManagementBean.class.getName());
    
    @Inject
    @JMSConnectionFactory("jms/4givingConnectionFactory")
    private JMSContext jmsContext;
    
    @Resource(name = "jms/4givingNotifications")
    private Destination dest;
    
    @POST
    public void notifyMantenianceShutdown(@QueryParam("h") int hores){
        try {
            //publiquem a la cua un missatge. Incorpora el temps previst de desconexió
            //MapMessage m = jmsContext.createMapMessage();
            //m.setInt("hores", hores);
            //ObjectMessage m = jmsContext.createObjectMessage();
            //m.setObject(hores);
            //TODO fer-ho amb properties
            Message m = jmsContext.createMessage();
            m.setIntProperty("hores", hores);
            m.setJMSPriority(9);
            
            jmsContext.createProducer().send(dest, m);
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
}
