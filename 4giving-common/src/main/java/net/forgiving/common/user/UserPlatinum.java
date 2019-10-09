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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author gabalca
 */
@Entity
@DiscriminatorValue("1")
public class UserPlatinum extends User{
    @Column(name = "last_subs_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSubscription;
    
    @Column(name = "years")
    private int subscriptionYears;

    public Date getLastSubscription() {
        return lastSubscription;
    }

    public void setLastSubscription(Date lastSubscription) {
        this.lastSubscription = lastSubscription;
    }

    public int getSubscriptionYears() {
        return subscriptionYears;
    }

    public void setSubscriptionYears(int subscriptionYears) {
        this.subscriptionYears = subscriptionYears;
    }
}
