/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.petitions;

import java.security.Principal;
import java.time.Instant;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.forgiving.common.donation.Donation;
import net.forgiving.common.donation.Petition;
import net.forgiving.common.user.User;
import net.forgiving.donation.persistence.DonationStorageException;
import net.forgiving.donation.persistence.DonationsDao;
import net.forgiving.petitions.persistence.PetitionsDao;
import net.forgiving.user.persistence.UserDao;

/**
 *
 * @author gabalca
 */
@Stateless
@Path("/donations/{don_id}/petitions")
@DeclareRoles("User")
@RolesAllowed("User")
public class PetitionsBean {
    
    /*
    //podriem atacar directament en entityManager, però ho farem a través d'un DAO
    @PersistenceContext(unitName = "net.forgiving_4givingPU")
    private EntityManager em;
    */
    @Inject
    private PetitionsDao petitionsDao;
    
    @Inject
    private DonationsDao donationsDao;
    
    @Inject
    private UserDao userDao;
    
    @Resource
    private SessionContext sCtx;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createNewPetition(@PathParam("don_id")Long donation_id, 
            Petition p) 
            throws DonationStorageException{
        
        //en base a l'usuari logat, obtenim el user id
        Principal userPr = sCtx.getCallerPrincipal();
        User us = userDao.getUser(userPr.getName());
        p.setPetitioner(us);
        
        //en base a l'id de donació, l'obtenim i la posem a la Petition
        Donation d = donationsDao.getDonationById(donation_id);
        p.setDonation(d);
        
        //guardem la petition
        p.setCreated(Instant.now());
        petitionsDao.savePetition(p);
        
    }
}
