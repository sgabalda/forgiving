/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.petitions.persistence;

import java.util.List;
import net.forgiving.common.donation.Petition;

/**
 *
 * @author gabalca
 */
public interface PetitionsDao  {
    void savePetition(Petition p);
    void removeEntity(Petition p);
    Petition getPetitionById(Long id);
    List<Petition> getPetitionsByUser(Long user_id);
    List<Petition> getPetitionsByDonation(Long donation_id);
    
}
