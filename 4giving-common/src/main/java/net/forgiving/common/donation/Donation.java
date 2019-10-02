/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.common.donation;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import net.forgiving.common.user.User;

/**
 *
 * @author gabalca
 */
@Entity
public class Donation implements Serializable {

    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User donator;
    
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;
    
    @Embedded
    private Conditions conditions;  /* Es un embedded: està a la mateixa taula, però són classes diferents*/
    
    @Column(name = "karma")
    private int karmaCost;
    
    @Transient
    private Instant created;
    @Transient
    private Instant resolvingDeadline;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvingDate;
    
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status;      /* Com que es un enum, podem 
                                        configurar-lo perque es persisteixi 
                                        amb el ordinal, o amb el nom  */
    @OneToMany(fetch = FetchType.LAZY, 
            targetEntity = Petition.class, mappedBy = "donation")
    private List<Petition> petitons;    /*  */
    
    @PrePersist @PreUpdate
    private void prepareDatesForPersist(){
        createdDate = created == null ? null : new Date(created.toEpochMilli());
        resolvingDate = resolvingDeadline == null ? null :new Date(resolvingDeadline.toEpochMilli());
    }
    
    @PostLoad
    private void prepareInstantsAfterRetrieve(){
        created = createdDate==null? null: createdDate.toInstant();
        resolvingDeadline = resolvingDate==null? null:resolvingDate.toInstant();
    }

    public Instant getResolvingDeadline() {
        return resolvingDeadline;
    }

    public void setResolvingDeadline(Instant resolvingDeadline) {
        this.resolvingDeadline = resolvingDeadline;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getDonator() {
        return donator;
    }

    public void setDonator(User donator) {
        this.donator = donator;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Conditions getConditions() {
        return conditions;
    }

    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
    }

    public int getKarmaCost() {
        return karmaCost;
    }

    public void setKarmaCost(int karmaCost) {
        this.karmaCost = karmaCost;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public List<Petition> getPetitons() {
        return petitons;
    }

    public void setPetitons(List<Petition> petitons) {
        this.petitons = petitons;
    }

    @Override
    public String toString() {
        return "Donation{" + "id=" + id + ", donator=" + donator + ", item=" + item + ", conditions=" + conditions + ", karmaCost=" + karmaCost + ", created=" + created + ", status=" + status + ", petitons=" + petitons + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Donation other = (Donation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
}
