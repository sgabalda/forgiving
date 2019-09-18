/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.forgiving.common.donation.Donation;
import net.forgiving.common.donation.DonationStatus;
import net.forgiving.common.user.User;
import net.forgiving.donation.persistence.DonationStorageException;
import net.forgiving.donation.persistence.DonationsDao;
import net.forgiving.donation.persistence.ItemStorageException;
import net.forgiving.user.persistence.UserDao;

/**
 *
 * @author gabalca
 */
@Stateless
@Path("/donations")
public class DonationsBean{
    @Inject
    private DonationsDao donationsDao;
    @Inject
    private UserDao userDao;
    
    @EJB
    private ItemBean itemBean;
    
    @EJB
    private ItemBeanAdminProxy itemAdminBean;
    
    @EJB
    private DonationLogBean donationLogBean;
    
    @EJB
    private DonationResolverBean donationResolver;
    
    @Resource
    private SessionContext sessionCtx;
    
    @Resource
    private TimerService timerService;
    
    
    @GET
    @Path("/{id}")
    public Donation getDonation(@PathParam("id") long id) 
            throws DonationStorageException{
        
       
        return donationsDao.getDonationById(id);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveDonation(Donation d) throws DonationStorageException{
        d.setCreated(Instant.now());
        d.setStatus(DonationStatus.OPENED);
        d.setKarmaCost(2);
        if(d.getResolvingDeadline()==null){
            d.setResolvingDeadline(Instant.now().plus(7,ChronoUnit.SECONDS));
        }
        
        if(d.getItem()!=null){
            try{
                itemBean.createItem(d.getItem());
                if(!sessionCtx.getRollbackOnly()){
                    donationsDao.storeDonation(d);
                    donationLogBean.donationAdded(d);
                    donationResolver.setupResolution(d);
                }
            }catch(ItemStorageException ex){
                System.out.println("Exception saving Item"); 
            }
            
        }else{
            //no item, abort
            System.out.println("No item, aborting creation of item");
            throw new DonationStorageException("Can "
                    + "not create Donation without Item");
        }
        
    }
    
    @DELETE
    @Path("/{id}")
    public void deleteDonation(@PathParam("id") long id)throws DonationStorageException, ItemStorageException{
        
        System.out.println("L'usuari "
                +sessionCtx.getCallerPrincipal().getName()+
                " vol eiliminar la donacio "+id+
                ". Is admin? "+sessionCtx.isCallerInRole("Admin"));
        
        Donation d=donationsDao.getDonationById(id);
        if(d!=null){
            User u = userDao.getUser(d.getDonator().getId());
            if(sessionCtx.isCallerInRole("Admin") || 
                    (u!=null && 
                        u.getUsername().equals(sessionCtx.getCallerPrincipal().getName()))){
                System.out.println("Aqui eliminariem la donation "+id);
                itemAdminBean.deleteItem(id);
            }
            
        }
        
        
    }
    
    @Timeout
    public void processaFinalDonacio(Timer t){
        
    }
    
}
