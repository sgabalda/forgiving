/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.petitions.persistence;

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.forgiving.common.donation.Petition;

/**
 *
 * @author gabalca
 */
@Dependent
public class JPAPetitionsDao implements PetitionsDao{
    
    @PersistenceContext(unitName = "net.forgiving_4givingPU")
    private EntityManager em;

    @Override
    public void savePetition(Petition p) {
        em.persist(p);
    }

    @Override
    public void removeEntity(Petition p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Petition getPetitionById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Petition> getPetitionsByUser(Long user_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Petition> getPetitionsByDonation(Long donation_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
