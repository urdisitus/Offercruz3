/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.imp.control;

import bo.com.offercruz.dal.contrato.control.IDAOManager;
import org.hibernate.HibernateException;

/**
 *
 * @author Olvinho
 */
public class FactoriaDAOManager {

    private static final ThreadLocal<IDAOManager> caja = new ThreadLocal<>();

    public static IDAOManager getDAOManager() {
        IDAOManager daoManager = caja.get();
        if (daoManager == null) {
            try {
                daoManager = new DAOManagerHibernate();
                caja.set(daoManager);
            } catch (HibernateException e) {
                System.out.println(e.getMessage());
            }

        }
        return daoManager;
    }
}
