/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.forgiving.common.donation.Item;
import net.forgiving.donation.persistence.ItemDao;
import net.forgiving.donation.persistence.ItemStorageException;

/**
 *
 * @author gabalca
 */
@Stateless
@Path("/items")
@DeclareRoles("Admin")
public class ItemBean{
    
    @Inject
    private ItemDao itemDao;
    
    @Resource
    private SessionContext sessionContext;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void createItem(Item i) throws ItemStorageException{
        System.out.println("Storing item!!");
        try{
            itemDao.storeItem(i);
        }catch(ItemStorageException ex){
            sessionContext.setRollbackOnly();
            //throw ex;
        }
    }
    
    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getItemById(@PathParam("itemId") long id) throws ItemStorageException{
        System.out.println("Getting item "+id);
        return itemDao.getItemById(id);
    }
    
    @DELETE
    @Path("/{itemId}")
    @RolesAllowed("Admin")
    public void deleteItem(@PathParam("itemId") long id) throws ItemStorageException{
        System.out.println("Aqui eliminariem l'Item "+id);
        //TODO implementar al DAO
    }
    
}
