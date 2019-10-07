/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.forgiving.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import net.forgiving.common.donation.Petition;

/**
 *
 * @author gabalca
 */
@WebServlet(urlPatterns = "/JPA")
@PersistenceContext(name = "persUnit/forgiving", unitName = "net.forgiving_4givingPU")
public class JPAFactoryTestServlet extends HttpServlet {

    @Resource
    private UserTransaction ut;
    
    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("net.forgiving_4givingPU"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        EntityManager em = emf.createEntityManager();
        

            try {
                ut.begin();
            
                //constrim la entity
                Petition p =new Petition();
                //....
                em.persist(p);

                ut.commit();
            
            } catch (Exception ex) {
                Logger.getLogger(JPAFactoryTestServlet.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    ut.rollback();
                } catch (IllegalStateException ex1) {
                    Logger.getLogger(JPAFactoryTestServlet.class.getName()).log(Level.SEVERE, null, ex1);
                } catch (SecurityException ex1) {
                    Logger.getLogger(JPAFactoryTestServlet.class.getName()).log(Level.SEVERE, null, ex1);
                } catch (SystemException ex1) {
                    Logger.getLogger(JPAFactoryTestServlet.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }finally{
                em.close();
            }
            
        
    }
    
    
    
}
