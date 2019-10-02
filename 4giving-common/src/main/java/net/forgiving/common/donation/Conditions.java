/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.common.donation;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import net.forgiving.common.user.Address;

/**
 *
 * @author gabalca
 */
@Embeddable
public class Conditions implements Serializable {

    private Instant pickingDeadline; 
    @Transient
    private String hoursAvailable;
    private Address pickingAdress;

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
