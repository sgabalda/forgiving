/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.web.messaging;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

/**
 *
 * @author gabalca
 */
@Named
@RequestScoped
public class InspectBean {
    
    @Inject
    private JMSContext jmsContext;
    
    @Resource(name = "java:comp/jms/webappMessagingQueue")
    private Queue queue;
    
    public void getMessages(){
        
        QueueBrowser browser = jmsContext.createBrowser(queue);

        try {
            Enumeration<Message> messages = browser.getEnumeration();
            int numMessages = 0;
            while(messages.hasMoreElements()){
                numMessages++;
                Message m = messages.nextElement();
                
                FacesMessage facesMessage=
                        new FacesMessage("Missatge "+numMessages+
                                ": "+m.getBody(String.class));
                FacesContext.getCurrentInstance()
                        .addMessage(null, facesMessage);
                
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(InspectBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
