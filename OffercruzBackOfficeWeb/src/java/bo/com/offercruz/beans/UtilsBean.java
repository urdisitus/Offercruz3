
package bo.com.offercruz.beans;
import bo.com.offercruz.bl.contratos.IPerfilBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Perfil;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class UtilsBean {

    /**
     * Creates a new instance of UtilsBean
     */
    public UtilsBean() {
    }
     
    public String descripcionPerfil(Perfil perfil){
        if (perfil == null){
            return "";
        }
        if (perfil.getId() == null){
            return "";
        }
//        IPerfilBO rolBO = FactoriaObjetosNegocio.getInstance().getIPerfilBO();
        return perfil.getNombre();
    }

}
