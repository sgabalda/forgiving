/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.admin;

import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author gabalca
 */
@Stateless
@Path("/admin")
//TODO securitzar
public class AdminManagementBean {
    
    @POST
    public void notifyMantenianceShutdown(@QueryParam("h") int hores){
        //publiquem a la cua un missatge. Incorpora el temps previst de desconexió
        
    }
    
}
