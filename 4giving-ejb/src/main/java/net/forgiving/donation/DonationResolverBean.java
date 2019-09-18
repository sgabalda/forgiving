/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import net.forgiving.common.donation.Donation;
import net.forgiving.donation.persistence.DonationStorageException;
import net.forgiving.donation.persistence.DonationsDao;
import net.forgiving.donation.shipping.ShippingBean;

/**
 *
 * @author gabalca
 */
@Stateless
@Path("/resolutions")
public class DonationResolverBean {
    
    private static final Logger LOG = Logger.getLogger(DonationResolverBean.class.getName());
    
    @Inject
    private DonationsDao donationsDao;
    
    @EJB
    private ShippingBean shippingBean;
    
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
    
    @GET
    public List<DonationScheduledResolution> getScheduledResolutions(){
        return timerService.getAllTimers().stream()
                .map((Timer t) -> {
                    
                    DonationScheduledResolution dsr = new DonationScheduledResolution();
                    if(t.getInfo() instanceof DonationTimerInfo){
                        DonationTimerInfo dti = (DonationTimerInfo)t.getInfo();
                        dsr.setDonationId(dti.getDonationId());
                    }

                    dsr.setScheduledDate(t.getNextTimeout());
                    
                    return dsr;
                })
                .collect(Collectors.toList());

    }

    @Timeout
    public void resolve(Timer t) throws DonationStorageException{
        //aqui resolem la donació
        DonationTimerInfo dti = (DonationTimerInfo)t.getInfo();
        //obtenir la donació del DAO
        Donation d = donationsDao.getDonationById(dti.getDonationId());
        //TODO modificar l'estat
        
        //TODO notificar al guanyador
        
        //enviar el producte
        
        shippingBean.processShipping(d);
        
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
