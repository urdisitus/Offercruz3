/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import bo.com.offercruz.bl.contratos.IGenericoBO;
import bo.com.offercruz.bl.excepticiones.BusinessException;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 *
 * @author Olvinho
 * @param <T> Clase entidad
 * @param <U> Clase negocio
 */
public abstract class BeanGenerico<T, U extends IGenericoBO<T, ?>> implements java.io.Serializable {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    private T entidad;

    public BeanGenerico() {
        prepararInsertar();
    }

    public final void prepararInsertar() {
        entidad = getNuevaEntidad();
    }

    public T getEntidad() {
        return entidad;
    }

    public void setEntidad(T entidad) {
        this.entidad = entidad;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void guardar() {
        FacesMessage msg = null;
        boolean guardo = true;
        try {
            getObjetoNegocio().setIdUsuario(loginBean.getCurrentUser().getId());
            if (esNuevaEntidad(entidad)) {
                preInsertar(entidad);
                entidad = getObjetoNegocio().insertar(entidad);
                postInsertar(entidad);
            } else {
                preActualizar(entidad);
                entidad = getObjetoNegocio().actualizar(entidad);
                postInsertar(entidad);
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

    public String tituloForm() {
        return (esNuevaEntidad(entidad)) ? "Nuevo" : "Actualizando";
    }

    public List<T> obtenerTodos() {
        return getObjetoNegocio().obtenerTodos();
    }

    public void seleccionarEntidad(T entidad) {
        setEntidad(entidad);
        despuesDeSeleccionar(entidad);
    }

    protected void preInsertar(T entidad) {
    }

    protected void postInsertar(T entidad) {
    }

    protected void preActualizar(T entidad) {
    }

    protected void postActualizar(T entidad) {
    }

    /**
     * *
     * Metodo plantilla llamado despues de seleccionar un registro de la lista
     *
     * @param entidad La entidad que fue seleccionada
     */
    protected void despuesDeSeleccionar(T entidad) {
    }

    abstract T getNuevaEntidad();

    abstract U getObjetoNegocio();

    abstract boolean esNuevaEntidad(T entidad);

}
