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
    
    public List<String> obtenerTodos()
    {
        return Arrays.asList("Producto","Servicios","Ambos");
    }
    
    public String obtenerIndividual(int tipo){
        switch (tipo) {
            case 0:
                return "Producto";
            case 1:
                return "Servicio";
            case 2:
                return "Ambos";
        }
        return "";
    }
    
    
    
    public TipoOferta obtenerTipoOferta(String oferta){
        if (oferta.equals("Producto")) 
            return TipoOferta.PRODUCTO;
        else 
            
            if (oferta.equals("Servicio"))
                return TipoOferta.SERVICIO;
            else
                return TipoOferta.AMBOS;
    }
    
}
