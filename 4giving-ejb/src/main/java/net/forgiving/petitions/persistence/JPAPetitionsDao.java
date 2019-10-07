/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.petitions.persistence;

import java.util.Collection;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import net.forgiving.common.donation.Donation;
import net.forgiving.common.donation.Petition;
import net.forgiving.common.user.User;

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
        //em.persist(p);
        em.merge(p);
        
    }

    @Override
    public void removeEntity(Petition p) {
        em.remove(p);
    }

    @Override
    public Petition getPetitionById(Long id) {
        return em.find(Petition.class, id);
    }

    @Override
    public List<Petition> getPetitionsByUser(Long user_id, int offset, int results) {

        TypedQuery<Petition> query = em.createNamedQuery("findPetitionsByUser",Petition.class);
        
        query.setParameter("userid", user_id);
        query.setFirstResult(offset);
        query.setMaxResults(results);
        return query.getResultList();
    
    }

    @Override
    public List<Petition> getPetitionsByDonation(Long donation_id, int offset, int results) {
        TypedQuery<Petition> query = em.createNamedQuery("findPetitionsByDonation",Petition.class);
        
        query.setParameter("donationid", donation_id);
        query.setFirstResult(offset);
        query.setMaxResults(results);
        
        return query.getResultList();
        
    }

    @Override
    public Petition getWinningPetition(Long donationId) {
        /*
        TypedQuery<Petition> query = em.createNamedQuery("findPetitionsWinning",Petition.class);
        
        query.setParameter("donationid", donationId);
        query.setMaxResults(1);
        return query.getSingleResult();
        */
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Petition> query = builder.createQuery(Petition.class);
        
        Root<Petition> root = query.from(Petition.class);

        query.select(root);
        
        //filtrem per donació
        Path<Donation> donationPath = root.get("donation");
        
        Predicate donationIdCondition = 
                builder.equal(donationPath.get("id"), donationId);
        
        query.where(donationIdCondition);
        
        Order orderKarma = builder.desc(root.get("maxKarmaCost"));
        Order orderCreated = builder.asc(root.get("createdDate"));
        
        query.orderBy(orderKarma,orderCreated);
        
        TypedQuery<Petition> tq = em.createQuery(query);
        
        List<Petition> result = tq.setMaxResults(1).getResultList();
        return result.isEmpty()? null : result.get(0);
        
    }

    /**
     * Retorna les peticions per les ultimes numDonations que ha fet l'usuari
     * @param user_id
     * @param numDonations
     * @return 
     */
    @Override
    public List<Petition> getPetitionsForUserLastDonations(Long user_id, int numDonations) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Donation> queryLastDonations = cb.createQuery(Donation.class);
        
        Root<Donation> rootDonations = queryLastDonations.from(Donation.class);
        
        queryLastDonations.select(rootDonations);
        queryLastDonations.where(
                cb.equal(rootDonations.get("donator").get("id"),
                user_id));
        queryLastDonations.orderBy(cb.desc(rootDonations.get("createdDate")));
        
        List<Donation> donations = em.createQuery(queryLastDonations)
                .setMaxResults(numDonations)
                .getResultList();
        
        CriteriaQuery<Petition> query = cb.createQuery(Petition.class);

        Root<Petition> root = query.from(Petition.class);
        
        Path<User> userPath = root.get("donation").get("donator");
        
        query.select(root);
        
        Predicate donatorCondition = cb.equal(userPath.get("id"),user_id);

        //ParameterExpression<Collection> donationsInParam =
        Predicate inDonations = root.get("donation").in(donations);
        
        Predicate allConditions = cb.and(donatorCondition,inDonations);
        
        query.where(allConditions);
        
        Order donationCreated = cb.desc(root.get("donation").get("createdDate"));
        
        query.orderBy(donationCreated);
        
        TypedQuery<Petition> tq = em.createQuery(query);

        return tq.getResultList();
        
    }
    
    
    
}
