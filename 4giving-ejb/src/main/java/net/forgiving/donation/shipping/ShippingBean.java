/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation.shipping;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import net.forgiving.common.donation.Donation;

/**
 *
 * @author gabalca
 */
@Stateless
public class ShippingBean {
    
    private static final Logger LOG = Logger.getLogger(ShippingBean.class.getName());
    
    @Inject
    @JMSConnectionFactory("jms/4givingConnectionFactory")
    private JMSContext jmsContext;
    
    @Resource(name = "jms/4givingDelivery")
    private Destination dest;
    
    public void processShipping(Donation d){
        try {
            //enviem a la cua d'enviaments
            
            ObjectMessage om = jmsContext.createObjectMessage(new ShippingInfo(d));
            boolean urgent = d.getId()%2==0;
            boolean internacional = d.getId()%3==0;
            om.setBooleanProperty("urgent", urgent);
            om.setBooleanProperty("internacional", internacional);
            
            LOG.info("Enviant a missatgeria la petició "+d.getId()+
                    " urgent/int : "+urgent+"/"+internacional);
            
            JMSProducer producer = jmsContext.createProducer();
            producer.send(dest, om);
        } catch (JMSException ex) {
            Logger.getLogger(ShippingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
}
