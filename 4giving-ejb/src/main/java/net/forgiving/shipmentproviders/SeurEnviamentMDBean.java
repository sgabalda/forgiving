/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.shipmentproviders;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import net.forgiving.donation.shipping.ShippingInfo;

/**
 *
 * @author gabalca
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"
    ),
    @ActivationConfigProperty(
            propertyName = "destinationLookup",
            propertyValue = "jms/4givingDelivery"
    ),
    @ActivationConfigProperty(
            propertyName = "messageSelector",
            propertyValue = "urgent = FALSE AND internacional = TRUE"
    )
})
public class SeurEnviamentMDBean implements MessageListener {

    private static final Logger LOG = Logger.getLogger(SeurEnviamentMDBean.class.getName());

    @Resource
    private MessageDrivenContext mdCtx;
    //els session Beans tenen SessionContext, el MDB MessageDrivenContext.
    //les dues son subclasses de EJBContext

    @Override
    public void onMessage(Message msg) {
        try {
            int numTries = msg.getIntProperty("JMSXDeliveryCount");
            
            LOG.log(Level.INFO, "Aquest missatge s''ha intentat {0} vegades, "
                    + "\u00e9s redelivered {1} i est\u00e0 a la cua desde {2}", 
                    new Object[]{numTries, msg.getJMSRedelivered(), msg.getJMSTimestamp()});
            if(Math.random()>0.5){
                mdCtx.setRollbackOnly();
                return;
            }
            if (msg instanceof ObjectMessage) {

                ShippingInfo info = (ShippingInfo) ((ObjectMessage) msg).getObject();

                LOG.log(Level.INFO, "Enviant a SEUR la donacio: {0}", info.getDonationId());

            } else {
                LOG.log(Level.SEVERE, "No se processar el missatge: {0}", msg);
                mdCtx.setRollbackOnly();
            }
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, "No puc extreure la info del missatge ", ex);
            mdCtx.setRollbackOnly();
        }
    }

}
