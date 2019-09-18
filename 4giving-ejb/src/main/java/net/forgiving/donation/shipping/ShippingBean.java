/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation.shipping;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import net.forgiving.common.donation.Donation;

/**
 *
 * @author gabalca
 */
@Stateless
public class ShippingBean {
    
    @Inject
    @JMSConnectionFactory("jms/4givingConnectionFactory")
    private JMSContext jmsContext;
    
    @Resource(name = "jms/4givingDelivery")
    private Destination dest;
    
    public void processShipping(Donation d){
        //enviem a la cua d'enviaments
        
        ObjectMessage om = jmsContext.createObjectMessage(new ShippingInfo(d));
        
        JMSProducer producer = jmsContext.createProducer();
        producer.send(dest, om);
        
    }
    
}
