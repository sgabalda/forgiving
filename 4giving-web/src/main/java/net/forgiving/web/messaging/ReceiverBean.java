/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.web.messaging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;

/**
 *
 * @author gabalca
 */
@Named
@RequestScoped
public class ReceiverBean {
    
    @Inject
    private JMSContext jmsContext;
    
    @Resource(name="java:comp/jms/webappMessagingQueue")
    private Queue queue;
    
    public void getMessage(){
        
        String text=null;
        
        JMSConsumer receiver = jmsContext.createConsumer(queue);
        
        // consumidor síncron
        text = receiver.receiveBody(String.class, 10000);
        
        //consumidor asícnron
        /*
        receiver.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                FacesMessage facesMessage;
                try {
                    facesMessage = new FacesMessage("Rebut: "+msg.getBody(String.class));
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                } catch (JMSException ex) {
                    Logger.getLogger(ReceiverBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        */

    }
    
}
