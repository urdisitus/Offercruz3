/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.beans;

import bo.com.offercruz.bl.contratos.IPerfilBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Permiso;
import bo.com.offercruz.utils.MyTreeNode;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Ernesto
 */
public class PerfilBean  extends  BeanGenerico<Perfil, IPerfilBO>{
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
//            getObjetoNegocio().guardarPermisos(listaPermisos);
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
    
//    public void seleccionarRolPermisos(Rol entidad) {
//        this.permisosSeleccionado =  entidad;
//        crearArbolPermisos(entidad);
//    }
    
//    private void crearArbolPermisos(Perfil rol) {
//        List<Perfil> raiz = getObjetoNegocio().obtenerTodos();
//        //System.out.println("Longitud de la lista " + raiz.size());
//        raizPermisos = new MyTreeNode("raiz", null);
//        //raizPermisos.setExpanded(true);
//        TreeNode treeControlTotal = new MyTreeNode(raiz.get(0), raizPermisos);
//        treeControlTotal.setExpanded(true);
//
//        CrearPermisosR(raiz.get(0), treeControlTotal);
//    }

//    private void CrearPermisosR(RolPermiso padre, TreeNode treePadre) {
//        List<RolPermiso> hijos = getObjetoNegocio().getPermisos(padre.getId().getIdRol(), padre.getId().getIdPermiso());
//        TreeNode node;
//        for (RolPermiso rolPermiso : hijos) {
//            node = new MyTreeNode(rolPermiso, treePadre);
//            node.setExpanded(true);
//            node.setSelected(rolPermiso.isValor());
//            CrearPermisosR(rolPermiso, node);
//        }
//    }
}
