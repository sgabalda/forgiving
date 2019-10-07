/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.petitions;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.forgiving.common.donation.Petition;
import net.forgiving.petitions.persistence.PetitionsDao;

/**
 *
 * @author gabalca
 */
@Stateless
@Path("/peticions/user/{user_id}")
public class PetitionsUserBean {
    
    @Inject
    private PetitionsDao petitionsDao;
    
    @GET
    public List<Petition> getPetitionsByUser(@PathParam("user_id") Long user_id){
        return petitionsDao.getPetitionsByUser(user_id,0, 10);
    }
    
    @GET
    @Path("/last")
    public List<Petition> 
        getPetitionsForLastDonations(@PathParam("user_id") Long user_id){
        
            //obtenim les peticions per les darreres 5 donacions de l'usuari.
            return petitionsDao.getPetitionsForUserLastDonations(user_id, 5);
    }
    
}
