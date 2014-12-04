/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import bo.com.offercruz.bl.contratos.IEmpresaBO;
import bo.com.offercruz.bl.contratos.IImagenBO;
import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.impl.CategoriaBO;
import bo.com.offercruz.bl.impl.EmpresaBO;
import bo.com.offercruz.bl.impl.ImagenBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Imagen;
import bo.com.offercruz.entidades.Oferta;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Permiso;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
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
    private DualListModel<String> CateList = new DualListModel<String>();
    private Empresa empresaSeleccionada;
    private List<Categoria> puntero;

    /**
     * Creates a new instance of EmpresaBean
     */
    public EmpresaBean() {

    }

    public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public Empresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setCateList(DualListModel<String> CateList) {
        this.CateList = CateList;
    }

    public DualListModel<String> getCateList() {
        return CateList;
    }

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

    public void seleccionarEmpresaCategorias(Empresa entidad) {
        this.empresaSeleccionada = entidad;
        LlenarListas(entidad);
    }

    public void LlenarListas(Empresa entidad) {
        List<Categoria> TodasCategorias = FactoriaObjetosNegocio.getInstance().getICategoriaBO().obtenerTodos();
        List<Categoria> CategoriaEmpresa = new ArrayList<Categoria>(entidad.getCategorias());
        for (int i = 0; i < TodasCategorias.size(); i++) {
            for (int j = 0; j < CategoriaEmpresa.size(); j++) {
                if (TodasCategorias.get(i).getId() == CategoriaEmpresa.get(j).getId()) {
                    TodasCategorias.remove(i);
                }
            }
        }
        List<String> source = new ArrayList<String>();
        for (int y = 0; y < TodasCategorias.size(); y++) {
            source.add(TodasCategorias.get(y).getNombre());
        }
        List<String> target = new ArrayList<String>();
        for (int x = 0; x < CategoriaEmpresa.size(); x++) {
            target.add(CategoriaEmpresa.get(x).getNombre());
        }
        CateList.setSource(source);
        CateList.setTarget(target);
    }

    public void guardarCategorías() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg;
        boolean guardo = true;

        try {

            if (empresaSeleccionada != null) {

                List<Categoria> cat = new ArrayList<Categoria>();
                for (String string : CateList.getTarget()) {
                    Categoria oo = new Categoria();
                    oo.setNombre(string);
                    cat.add(oo);
                }
                empresaSeleccionada.setCategorias(new HashSet(cat));
                getObjetoNegocio().actualizar(empresaSeleccionada);
            }

        } catch (Exception e) {
            guardo = false;
            System.out.println(e);
        }
        if (guardo) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Categorias guardadas correctamente.");
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No se pudieron guardar las Categorias.");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("guardo", guardo);
    }
}
