/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import java.util.Date;

import bo.com.offercruz.bl.contratos.IContenidoBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Contenido;
import bo.com.offercruz.entidades.Oferta;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ernesto
 */
@ManagedBean
@SessionScoped
public class ContenidoBean extends BeanGenerico<Contenido, IContenidoBO> {

    public ContenidoBean() {

    }

    private Contenido contenidoSeleccionado;

    private DualListModel<String> ofertas;

    @Override
    Contenido getNuevaEntidad() {
        Contenido nuevo = new Contenido();
        return nuevo;
    }

    @PostConstruct
    public void init() {
        ofertas = new DualListModel<String>();
    }

    public Contenido getContenidoSeleccionado() {
        return contenidoSeleccionado;
    }

    public void setContenidoSeleccionado(Contenido contenidoSeleccionado) {
        this.contenidoSeleccionado = contenidoSeleccionado;
    }

    public void inicializarLista(Contenido entidad) {
        contenidoSeleccionado = entidad;
        List<String> ofertasDisponibles = new ArrayList();
        List<String> ofertasSelectas = new ArrayList();
        if (getObjetoNegocio().getIdUsuario() == null) {
            getObjetoNegocio().setIdUsuario(getLoginBean().getCurrentUser().getId());
            getObjetoNegocio().setComandoPermiso(getComandoPermiso());
        }
//        if (getEntidad() == null) {
        //nuevo
//            for (Object object : getObjetoNegocio().getEmpresa().getOfertas()) {
//                Oferta of = (Oferta) object;
//                ofertasDisponibles.add(of);
//            }
//        } else {
        //Edicion            
        for (Object object : getObjetoNegocio().getEmpresa().getOfertas()) {
            Oferta of = (Oferta) object;
            boolean existe = false;
            for (Object objec : contenidoSeleccionado.getOfertas()) {
                Oferta of1 = (Oferta) objec;
                if (of1.getId() == of.getId()) {
                    existe = true;
                    break;
                }
            }
            if (existe) {
                ofertasSelectas.add(of.getNombre());
            } else {
                ofertasDisponibles.add(of.getNombre());
            }
        }
//        }C
        ofertas.setSource(ofertasDisponibles);
        ofertas.setTarget(ofertasSelectas);
    }

    public DualListModel<String> getOfertas() {
        return ofertas;
    }

    public void setOfertas(DualListModel<String> ofertas) {
        this.ofertas = ofertas;
    }

    @Override
    protected void preInsertar(Contenido entidad) {
        entidad.setEmpresa(getObjetoNegocio().getEmpresa());
    }

    public void guardarOfertas() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg;
        guardo = true;
        if (getObjetoNegocio().getIdUsuario() == null) {
            getObjetoNegocio().setIdUsuario(getLoginBean().getCurrentUser().getId());
            getObjetoNegocio().setComandoPermiso(getComandoPermiso());
        }
        try {
            if (contenidoSeleccionado != null) {
                List<Oferta> ofert = new ArrayList<Oferta>();
                for (String string : ofertas.getTarget()) {
                    Oferta oo = new Oferta();
                    oo.setNombre(string);
                    ofert.add(oo);
                }
                contenidoSeleccionado.setOfertas(new HashSet(ofert));
                getObjetoNegocio().actualizar(contenidoSeleccionado);
            }
        } catch (Exception e) {
            guardo = false;
            System.out.println(e);
        }
        if (guardo) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Oferta guardados correctamente.");
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No se pudieron guardar las ofertas.");
        }
        context.addCallbackParam("guardo", guardo);
    }

    public String parseDate(Date date) {
        return (new SimpleDateFormat("dd/MM/yyy")).format(date);
    }

    @Override
    public void guardar() {
        RequestContext context = RequestContext.getCurrentInstance();
        super.guardar(); //To change body of generated methods, choose Tools | Templates.
        context.addCallbackParam("guardo", guardo);
    }

    @Override
    IContenidoBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIContenidoBO();
    }

    @Override
    boolean esNuevaEntidad(Contenido entidad) {
        return (entidad.getId() == null);
    }

    @Override
    String getComandoPermiso() {
        return "contenido";
    }

}
