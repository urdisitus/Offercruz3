/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.beans;
import bo.com.offercruz.bl.contratos.ICategoriaBO;
import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.utils.MailHelper;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Ernesto
 */

@ManagedBean
@SessionScoped
public class CategoriaBean extends  BeanGenerico<Categoria, ICategoriaBO>{


    public CategoriaBean()
    {
    
    }
    
    
    @Override
    Categoria getNuevaEntidad() {   
        Categoria nueva = new Categoria();
        return nueva;
    }

    @Override
    ICategoriaBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getICategoriaBO();
    }

    @Override
    boolean esNuevaEntidad(Categoria entidad) {
        return (entidad.getId() == null);
    }

    @Override
    String getComandoPermiso() {
        return "categoria";
    }

    /**
     * Creates a new instance of UsuarioBean
     */
   
}
