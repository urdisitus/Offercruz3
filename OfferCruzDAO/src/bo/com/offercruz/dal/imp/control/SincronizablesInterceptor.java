/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.imp.control;


import bo.com.offercruz.entidades.contrato.ISincronizable;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 *
 * @author Olvinho
 */
public class SincronizablesInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

        if (entity instanceof ISincronizable) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("FechaModificacion".equals(propertyNames[i])) {
                    state[i] = new Date();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (entity instanceof ISincronizable) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("FechaModificacion".equals(propertyNames[i])) {
                    currentState[i] = new Date();
                    return true;
                }
            }
        }
        return false;
    }

}
