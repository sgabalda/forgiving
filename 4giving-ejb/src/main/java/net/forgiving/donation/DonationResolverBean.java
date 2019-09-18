/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import net.forgiving.common.donation.Donation;

/**
 *
 * @author gabalca
 */
@Stateless
public class DonationResolverBean {
    
    private static final Logger LOG = Logger.getLogger(DonationResolverBean.class.getName());
    
    @Resource
    private TimerService timerService;
    
    public void setupResolution(Donation d){
        //configurem la resolució de la donació
        TimerConfig config = new TimerConfig(new DonationTimerInfo(d),true);
        
        Date finalitzacio = new Date(d.getResolvingDeadline().toEpochMilli());
        
        Timer t = timerService.createSingleActionTimer(finalitzacio, config);
        
        LOG.log(Level.INFO, "Programem el timer per a que estigui a: {0}", t.getNextTimeout());
        
    }
    
    public void removeScheduledResolution(Donation d){
        Collection<Timer> timers = timerService.getTimers();
        
        timers.stream().filter(t -> {
            DonationTimerInfo dti = (DonationTimerInfo)t.getInfo();
            return dti.getDonationId()==d.getId();
        }).forEach(t -> t.cancel());
    }

    @Timeout
    public void resolve(Timer t){
        //aqui resolem la donació
        DonationTimerInfo dti = (DonationTimerInfo)t.getInfo();
        //obtenir la donació del DAO
        //modificar l'estat
        //notificar al guanyador
        LOG.log(Level.INFO, "Resolent la donacio {0}", dti.getDonationId());
    }
    
}
class DonationTimerInfo implements Serializable{
    private long donationId;
    private long userId;
    
    public DonationTimerInfo (Donation d){
        this.donationId = d.getId();
        this.userId = d.getDonator().getId();
    }

    public long getDonationId() {
        return donationId;
    }

    public void setDonationId(long donationId) {
        this.donationId = donationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    
}
