/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.common.donation;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import net.forgiving.common.user.User;

/**
 *
 * @author gabalca
 */
@Entity
@NamedQueries({
      @NamedQuery(
              name = "findPetitionsByUser",
              query = "Select p from Petition p "
                + "where p.petitioner.id = :userid"
      ),
      @NamedQuery(
              name = "findPetitionsByDonation",
              query = "select p from Petition p where "
                      + "p.donation.id = :donationid order by p.maxKarmaCost desc"
      ),
      @NamedQuery(
              name = "findPetitionsWinning",
              query = "select p from Petition p where "
                      + "p.donation.id = :donationid "
                      + "order by p.maxKarmaCost desc, p.createdDate asc"
      )
})
@Access(AccessType.PROPERTY)
public class Petition implements Serializable {

    //@IdClass
    //@EmbeddedId
    private Long id;

    private User petitioner;
    private Donation donation;
    private Instant created;

    @Access(AccessType.FIELD)
    @Column(name = "karma")
    @Positive
    private int maxKarmaCost;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(User petitioner) {
        this.petitioner = petitioner;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent
    public Date getCreatedDate() {
        return created == null ? null : new Date(created.toEpochMilli());
    }

    public void setCreatedDate(Date d) {
        this.created = d.toInstant();
    }

    @Transient
    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public int getMaxKarmaCost() {
        return maxKarmaCost;
    }

    public void setMaxKarmaCost(int maxKarmaCost) {
        this.maxKarmaCost = maxKarmaCost;
    }

    @Override
    public String toString() {
        return "Petition{" + "id=" + id + ", petitioner=" + petitioner + ", donation=" + donation + ", created=" + created + ", maxKarmaCost=" + maxKarmaCost + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Petition other = (Petition) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
