/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Usuario;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Olvinho
 */
@ManagedBean
@SessionScoped
public class LoginBean implements java.io.Serializable {

    /**
     * Creates a new instance of LoginBean
     */
    public Usuario currentUser;
    boolean loggedIn;

    public LoginBean() {
        inicarVariables();
    }

    private void inicarVariables() {
        currentUser = new Usuario();
        loggedIn = false;
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String closeLogin() throws Exception {
        inicarVariables();
        return "/closelogin.jsf?faces-redirect=true";
    }

    public String doLogin() throws Exception {
        IUsuarioBO usuarioBO = FactoriaObjetosNegocio.getInstance().getIUsuarioBO();
        
       // currentUser.setPassword(usuarioBO.encriptar(currentUser.getPassword()));
        Usuario result = usuarioBO.loguear(currentUser.getLogin(), currentUser.getPassword());
        if (result != null) {
            loggedIn = true;
            currentUser = result;
            return "/loginsuccess.jsf";
        } else {
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de Login", "Nombre de usuario o contrasena invalidos");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
    }

    /*
     * Metodos que verifican los permisos y redirecciona para usarlos
     * en el evento antes de cargar dde las paginas administrativas
     * 
     * Se devuelve FALSO si se Redirecciono porque no cumplio los
     * permisos
     */
    public boolean solicitarPermisoLogin() throws IOException {
        if (!loggedIn) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
            return false;
        }
        return true;
    }

    public boolean solicitarPermiso(String comandoPermiso) throws IOException {
        boolean r = solicitarPermisoLogin();
        if (r) {
            //Permiso si usuario validado
            r = FactoriaObjetosNegocio.getInstance().getIPerfilBO().verificarPermiso(comandoPermiso, currentUser);
            if (!r) {
                //Redirecciono acceso denegado
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/accessdenied.xhtml");
            }
            return r;
        }
        return r;
    }

    public boolean verificarPermiso(String comandoPermiso) {
        boolean r = FactoriaObjetosNegocio.getInstance().getIPerfilBO().verificarPermiso(comandoPermiso, currentUser);
        return r;
    }
}
