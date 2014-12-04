/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import bo.com.offercruz.bl.contratos.IPerfilBO;
import bo.com.offercruz.bl.contratos.IPermisoBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Permiso;
import bo.com.offercruz.utils.MyTreeNode;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Ernesto
 */
@ManagedBean
@RequestScoped
public class PerfilBean extends BeanGenerico<Perfil, IPerfilBO> {

    private TreeNode[] selectedNodes;
    private TreeNode raizPermisos;
    private Perfil permisosSeleccionado;

    /**
     * Creates a new instance of RolBean
     */
    public PerfilBean() {
    }

    @Override
    Perfil getNuevaEntidad() {
        return new Perfil();
    }

    @Override
    IPerfilBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIPerfilBO();
    }

    @Override
    boolean esNuevaEntidad(Perfil entidad) {
        return (entidad.getId() == null);
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public TreeNode getRaizPermisos() {
        return raizPermisos;
    }

    public void setRaizPermisos(TreeNode raizPermisos) {
        this.raizPermisos = raizPermisos;
    }

    public void setPermisosSeleccionado(Perfil permisosSeleccionado) {
        this.permisosSeleccionado = permisosSeleccionado;
    }

    public Perfil getPermisosSeleccionado() {
        return permisosSeleccionado;
    }

    @Override
    String getComandoPermiso() {
        return "perfil";
    }

    public void guardarPermisos() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg;
        boolean guardo = true;
        List<Permiso> listaPermisos = new ArrayList<Permiso>();
        for (int i = 0; i < raizPermisos.getChildCount(); i++) {
            marcarPermisoR(raizPermisos.getChildren().get(i), listaPermisos);
        }
        try {
            //  getObjetoNegocio().guardarPermisos(listaPermisos);   
        } catch (Exception e) {
            guardo = false;
            System.out.println(e);
        }
        if (guardo) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Permisos guardados correctamente.");
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No se pudieron guardar los permisos.");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("guardo", guardo);

    }

    public boolean marcarPermisoR(TreeNode node, List<Permiso> lista) {
        Permiso rp = (Permiso) ((MyTreeNode) node).getRealData();
        if (node.isLeaf()) {
            //Es Hoja
            boolean valor = treeNodeMarcado(node);
            lista.add(rp);
            return valor;
        } else {
            boolean valor = false;
            for (int i = 0; i < node.getChildCount(); i++) {
                valor = marcarPermisoR(node.getChildren().get(i), lista) || valor;
            }
            lista.add(rp);
            return valor;
        }
    }

    private boolean treeNodeMarcado(TreeNode node) {
        for (TreeNode selectedNode : selectedNodes) {
            if (node == selectedNode) {
                return true;
            }
        }
        return false;
    }

    public void seleccionarPerfilPermisos(Perfil entidad) {
        this.permisosSeleccionado = entidad;
        crearArbolPermisos(entidad);
    }

    private void crearArbolPermisos(Perfil perfil) {
        IPermisoBO permisosBO = FactoriaObjetosNegocio.getInstance().getIPermisoBO();
        List<Permiso> raiz = permisosBO.getPermisosRaiz();
        //System.out.println("Longitud de la lista " + raiz.size());
        raizPermisos = new MyTreeNode("raiz", null);
        //raizPermisos.setExpanded(true);                
        for (Permiso permiso : raiz) {
            TreeNode treeControlTotal = new MyTreeNode(permiso, raizPermisos);
            treeControlTotal.setExpanded(true);
            CrearPermisosR(permiso, treeControlTotal, perfil.getPermisos());
        }
        
//        
//        treeControlTotal.setExpanded(true);
//        
//        treeControlTotal.setSelected(raiz.get(0).isValor());
//        CrearPermisosR(raiz.get(0), treeControlTotal, perfil.getPermisos());
    }

    private void CrearPermisosR(Permiso padre, TreeNode treePadre, Set<Permiso> perfilPermisos) {
        IPermisoBO permisosBO = FactoriaObjetosNegocio.getInstance().getIPermisoBO();
        //List<Permiso> todos = permisosBO.obtenerTodos();
        List<Permiso> hijos = permisosBO.getPermisosHijos(padre.getId());
        TreeNode node;
        for (Permiso rolPermiso : hijos) {
            node = new MyTreeNode(rolPermiso, treePadre);
            node.setExpanded(true);
            for (Permiso per : perfilPermisos) {
                if(per.getId().intValue() == rolPermiso.getId().intValue()){
                    rolPermiso.setValor(true);
                    break;
                }
            }
            node.setSelected(rolPermiso.isValor());
            CrearPermisosR(rolPermiso, node, perfilPermisos);
        }
    }

}
