/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.petitions;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
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
    
    @GET
    public List<Petition> getPetitionsByDonation(@PathParam("don_id")Long donation_id){
        return petitionsDao.getPetitionsByDonation(donation_id, 0, 10);
    }
    
    @GET
    @Path("/winning")
    public Petition getWinningPetition(@PathParam("don_id")Long donation_id){
        //retorna la peticio amb més karma creada abans per a la donació
        return petitionsDao.getWinningPetition(donation_id);
    }
    
    @GET
    @Path("/{peti_id}")
    public Petition getPetition(@PathParam("peti_id")Long peti_id){
        return petitionsDao.getPetitionById(peti_id);
    }
    
    @DELETE
    @Path("/{peti_id}")
    public void deletePetition(@PathParam("peti_id")Long peti_id){
        
        //obtenir l'usuari logat
        Principal userPr = sCtx.getCallerPrincipal();
        User us = userDao.getUser(userPr.getName());
        //obtenir la petició
        Petition p = petitionsDao.getPetitionById(peti_id);
        //comprovar que l'usuari logat és el de la petició
        
        if(p.getPetitioner().getId().equals(us.getId())){
            //si ho és, eliminar-lo amb el DAO
            petitionsDao.removeEntity(p);
        }else{
            //si no ho és, SecurityException
            throw new SecurityException("No ets el propietari de la petició");
        }
    }
    
    @PUT
    @Path("/{peti_id}")
    public void updateKarmaPetition(@PathParam("peti_id")Long peti_id,@QueryParam("k") int karma){
        Principal userPr = sCtx.getCallerPrincipal();
        User us = userDao.getUser(userPr.getName());
        Petition p = petitionsDao.getPetitionById(peti_id);
        //comprovar que l'usuari logat és el de la petició
        
        if(p.getPetitioner().getId().equals(us.getId())){
            p.setMaxKarmaCost(karma);
            p.setCreated(Instant.now());
        }else{
            //si no ho és, SecurityException
            throw new SecurityException("No ets el propietari de la petició");
        }
        
    }
    
    
    
}
