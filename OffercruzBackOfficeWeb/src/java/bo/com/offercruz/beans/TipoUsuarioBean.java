/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import bo.com.offercruz.enums.TipoUsuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ernesto
 */
@ManagedBean
@RequestScoped
public class TipoUsuarioBean {

    /**
     * Creates a new instance of TipoUsuarioBean
     */
    public TipoUsuarioBean() {
    }

    public TipoUsuario[] obtenerTodos() {
        return TipoUsuario.values();
    }
    
    public TipoUsuario obtenerIndividual(int index){
        return TipoUsuario.values()[index];
    }
    
    public int obtenerTipoUsuario(String tipoU){
        if(tipoU.equals("OFFERCRUZ")){
            return 0;
        }else if(tipoU.equals("EMPRESA")){
            return 1;
        }else{
            return 2;
        }        
    }

}
