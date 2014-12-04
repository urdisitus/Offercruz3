/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import bo.com.offercruz.bl.contratos.IEmpresaBO;
import bo.com.offercruz.bl.contratos.IImagenBO;
import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.impl.EmpresaBO;
import bo.com.offercruz.bl.impl.ImagenBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Imagen;
import bo.com.offercruz.entidades.Permiso;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Dario
 */
@ManagedBean
@SessionScoped
public class EmpresaBean extends BeanGenerico<Empresa, IEmpresaBO> {

    private int idCategoria;
    private UploadedFile file;

    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Creates a new instance of EmpresaBean
     */
    public EmpresaBean() {
        
    }

    @Override
    Empresa getNuevaEntidad() {
        Empresa nuevo = new Empresa();
        return nuevo;
    }

    @Override
    IEmpresaBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIEmpresaBO();
    }

    @Override
    boolean esNuevaEntidad(Empresa entidad) {
        return (entidad.getId() == null);
    }

    @Override
    String getComandoPermiso() {
        return "empresa";
    }

    public String getEstado(int idEstado) {
        if (idEstado == 0) {
            return "Inactivo";
        } else {
            return "Activo";
        }
    }

    public void AbrirDialogN() {
        prepararInsertar();
    }

    @Override
    public void preInsertar(Empresa entidad) {
        if (file != null) {
            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
                Imagen img = new Imagen();
                img.setNombre(file.getFileName());
                img.setImagenFisica((Base64.encodeBase64String(file.getContents())));
                img.setFechaCreacion(new Date());
                img.setFechaModificacion(new Date());
                img.setEstado(1);
                entidad.setImagen(img);
                FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        }
    }
}
