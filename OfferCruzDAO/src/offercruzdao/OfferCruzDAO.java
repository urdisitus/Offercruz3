/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package offercruzdao;

import bo.com.offercruz.dal.imp.control.DAOManagerHibernate;
import bo.com.offercruz.dal.impl.CategoriaHibernateDAO;
import bo.com.offercruz.entidades.Categoria;
import java.util.Date;

/**
 *
 * @author Ernesto
 */
public class OfferCruzDAO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    DAOManagerHibernate manager = new DAOManagerHibernate();
        Categoria c = new Categoria();
        c.setEstado(1);
        c.setId(23);
        c.setNombre("Juan Carlos");
        //c.setTipoOferta(TipoOferta.PRODUCTO);
        c.setFechaModificacion(new Date());
        c.setFechaCreacion(new Date());
        manager.iniciarTransaccion();
        try {
            ((CategoriaHibernateDAO) manager.getCategoriaDAO()).persistir(c);
            manager.confirmarTransaccion();
        } catch (Exception e) {
            manager.cancelarTransaccion();
        }    
    }
    
}
