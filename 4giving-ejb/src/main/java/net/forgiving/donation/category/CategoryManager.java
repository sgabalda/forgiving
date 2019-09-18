/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.donation.category;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import net.forgiving.common.donation.Category;
import net.forgiving.donation.category.persistence.CategoryDao;

/**
 *
 * @author gabalca
 */
@Singleton
@Remote(CategoryRemote.class)
@TransactionManagement(TransactionManagementType.BEAN)
@DeclareRoles({"User","Admin"})
@RolesAllowed({"User","Admin"})
public class CategoryManager  implements CategoryLocal, CategoryRemote{
    
    private Set<Category> categories;
    
    @Inject
    private CategoryDao categoryDao;
    
    @Resource
    private UserTransaction userTx;
    
    @PostConstruct
    public void init(){
        System.out.println("Inicialitzant categories");
        refreshCategories(null);
        //throw new RuntimeException("Error inicialitzant categories");
    }
    
    @Schedules({
        @Schedule(second = "10/20", minute="*", hour="*", dayOfWeek = "Wed")
    })
    @Lock(LockType.WRITE)
    public void refreshCategories(Timer t){
        if(t!=null){
            System.out.println(t.getInfo());
        }
        //t.cancel(); podem cancelar el timer
        //
        System.out.println("Refrescant categories");
        categories=new HashSet<>(
                categoryDao.getAllCategories()
                //Arrays.asList(new Category(1L, "prova 1"),new Category(2L, "prova 2"))
        );
    }
    
    @PermitAll
    @Lock(LockType.READ)
    public Category getCategory(Long id){
        Optional<Category> result= 
                categories.stream()
                .filter(cat -> cat.getId().equals(id))
                .findAny();
        return result.orElse(null);                
    }
    //salta en el metode que s'espera, no en el que s'executa
    @PermitAll
    @Lock(LockType.READ)
    public Set allCategories(){
        return categories;
    }

    @RolesAllowed("Admin")
    @Lock(LockType.WRITE)
    public Category addCategory(Category cat){

        try{
            userTx.setTransactionTimeout(10);
            userTx.begin();
            categoryDao.storeCategory(cat);
            System.out.println("Guardada la categoria");
            userTx.commit();
        }catch(Exception e){
            System.out.println("Error guardant la categoria");
            try {
                userTx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex) {
                Logger.getLogger(CategoryManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        categories=new HashSet<>(categoryDao.getAllCategories());
        return cat;
    }
    
}
