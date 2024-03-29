/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.test;

import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author gabalca
 */
@Local
@Path("/exemple")
public interface RestExampleInterface {
    @GET
    @Path("/{exemple1}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getExample(
            @PathParam("exemple1") String exemple);
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getExample2();
    
    public String metodeNoExposat();

}
