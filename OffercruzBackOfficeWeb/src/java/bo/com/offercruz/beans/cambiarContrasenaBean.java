/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.excepticiones.BusinessException;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Dario
 */
@ManagedBean
@SessionScoped
public class cambiarContrasenaBean {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    private String passActual;
    private String nuevaPass;
    private String confirmarPass;

    public cambiarContrasenaBean() {
        System.out.println("Entro al constructor");
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public String getConfirmarPass() {
        return confirmarPass;
    }

    public String getNuevaPass() {
        return nuevaPass;
    }

    public String getPassActual() {
        return passActual;
    }

    public void setConfirmarPass(String confirmarPass) {
        this.confirmarPass = confirmarPass;
    }

    public void setNuevaPass(String nuevaPass) {
        this.nuevaPass = nuevaPass;
    }

    public void setPassActual(String passActual) {
        this.passActual = passActual;
    }

    public void guardarPas() {

        FacesMessage msg = null;
        boolean guardo = true;
        try {
            System.out.println("Entro a guardar");
            IUsuarioBO usuarioBO = FactoriaObjetosNegocio.getInstance().getIUsuarioBO();
            String contraseña = usuarioBO.encriptar(passActual.trim());
            Usuario User = loginBean.currentUser;
            usuarioBO.setIdUsuario(User.getId());
            usuarioBO.setComandoPermiso("cambiar_pass");
            if (contraseña.equals(User.getPassword()) && (nuevaPass.trim().equals(confirmarPass.trim()))) {
                System.out.println("se puede cambiar");
                String nuevaEncriptada = usuarioBO.encriptar(nuevaPass.trim());
                User.setPassword(nuevaEncriptada);
                usuarioBO.actualizar(User);
            } else {
                guardo = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas son incorrectas.");
            }
        } catch (BusinessException e) {
            mostrarErorresDeNegocio(e);
            return;
        } catch (Exception e) {
            guardo = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        }

        if (guardo) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Los datos han sido guardados.");
        } else if (msg == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ocurrió un error inesperado.");
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    protected void mostrarErorresDeNegocio(BusinessException excepcion) {
        FacesMessage msg;
        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "La operación no pudo ser completada debido a los siguientes errores:");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        for (BusinessExceptionMessage mensaje : excepcion.getMessages()) {

            if (mensaje.getIndex() > 0) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                        "- Fila " + mensaje.getIndex() + ":" + mensaje.getMessage());
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "- " + mensaje.getMessage());
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
