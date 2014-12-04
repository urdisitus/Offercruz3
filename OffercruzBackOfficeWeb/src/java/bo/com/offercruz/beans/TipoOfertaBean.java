/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.beans;

import bo.com.offercruz.enums.TipoOferta;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author juanCarlos
 */
@ManagedBean
@RequestScoped
public class TipoOfertaBean {

    /**
     * Creates a new instance of TipoOfertaBean
     */
    public TipoOfertaBean() {
    }
    
    
    public TipoOferta[] obtenerTodos()
    {
        return TipoOferta.values();
    }
    
    public TipoOferta obtenerIndividual(int index)
    {
        return TipoOferta.values()[index];
    }
    
    
    public int obtenerTipoOferta(String oferta){
        if (oferta.equals("Producto")) 
            return 0;
        else 
            if (oferta.equals("Servicio"))
                return 1;
            else
                return 2;
    }
    
}
