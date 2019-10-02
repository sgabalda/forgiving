/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.common.donation;

/**
 *
 * @author gabalca
 */
public enum DonationStatus {
    OPENED(1),
    TIMED_OUT(2),
    FINISHED(3),
    DELETED(4);
    
    int codi;

    private DonationStatus(int codi) {
        this.codi = codi;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }
    
    
}
