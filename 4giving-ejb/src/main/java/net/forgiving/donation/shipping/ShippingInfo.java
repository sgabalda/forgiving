/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation.shipping;

import java.io.Serializable;
import net.forgiving.common.donation.Donation;

/**
 *
 * @author gabalca
 */
public class ShippingInfo implements Serializable{
    private long donationId;
    private long userId;

    public ShippingInfo(Donation d) {
        this.donationId = d.getId();
        this.userId = d.getDonator().getId();
    }

    public long getDonationId() {
        return donationId;
    }

    public long getUserId() {
        return userId;
    }
    
    
}
