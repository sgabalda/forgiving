/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.common.user;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author gabalca
 */
@Entity
@DiscriminatorValue("2")
public class UserGold extends User{
    
    @Column(name = "gold_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date becomingGoldDate;

    public Date getBecomingGoldDate() {
        return becomingGoldDate;
    }

    public void setBecomingGoldDate(Date becomingGoldDate) {
        this.becomingGoldDate = becomingGoldDate;
    }
    
    
    
}
