/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.common.donation;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import net.forgiving.common.user.Address;

/**
 *
 * @author gabalca
 */
@Embeddable
public class Conditions implements Serializable {

    @Transient
    private Instant pickingDeadline; 
    @Transient
    private String hoursAvailable;
    
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address pickingAdress;
    
    @Access(AccessType.PROPERTY)
    @Column(name = "picking_time")
    public Date getPickingDate(){
        return pickingDeadline==null ? null : new Date(pickingDeadline.toEpochMilli());
    }
    public void setPickingDate(Date d){
        pickingDeadline = d ==null? null : d.toInstant();
    }

    public Instant getPickingDeadline() {
        return pickingDeadline;
    }

    public void setPickingDeadline(Instant pickingDeadline) {
        this.pickingDeadline = pickingDeadline;
    }

    public String getHoursAvailable() {
        return hoursAvailable;
    }

    public void setHoursAvailable(String hoursAvailable) {
        this.hoursAvailable = hoursAvailable;
    }

    @Override
    public String toString() {
        return "Conditions{" + "pickingDeadline=" + pickingDeadline + ", hoursAvailable=" + hoursAvailable + ", pickingAdress=" + pickingAdress + '}';
    }
    
    

}
