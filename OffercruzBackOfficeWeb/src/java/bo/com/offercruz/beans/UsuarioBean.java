/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.beans;
import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Usuario;
import bo.com.offercruz.utils.MailHelper;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Ernesto
 */

@ManagedBean
@SessionScoped
public class UsuarioBean extends  BeanGenerico<Usuario, IUsuarioBO>{
    private int idPerfil;

    /**
     * Creates a new instance of UsuarioBean
     */
    public UsuarioBean() {

    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    @Override
    Usuario getNuevaEntidad() {
        Usuario nuevo = new Usuario();
        idPerfil = 0;
        return nuevo;
    }

    @Override
    IUsuarioBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIUsuarioBO();
    }

    @Override
    protected void despuesDeSeleccionar(Usuario entidad) {
        idPerfil = 0;
        if (entidad.getPerfil() != null && entidad.getPerfil().getId() != null) {
            idPerfil = entidad.getPerfil().getId();
        }
    }

    @Override
    boolean esNuevaEntidad(Usuario entidad) {
        return (entidad.getId() == null);
    }

    @Override
    protected void preInsertar(Usuario entidad) {
        pasarLlavesForanas(entidad);
    }

    @Override
    protected void preActualizar(Usuario entidad) {
        pasarLlavesForanas(entidad);
    }

    private void pasarLlavesForanas(Usuario entidad) {
        if (idPerfil > 0) {
            entidad.setPerfil(new Perfil());
            entidad.getPerfil().setId(idPerfil);
        }
    }

    @Override
    protected void postInsertar(Usuario entidad) {
        MailHelper.enviarCredenciales(entidad);
    }

    @Override
    String getComandoPermiso() {
        return "usuario";
    }    
}
