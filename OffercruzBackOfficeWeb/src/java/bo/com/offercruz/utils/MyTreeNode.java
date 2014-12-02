/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.utils;

import bo.com.offercruz.entidades.Permiso;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Olvinho
 */
public class MyTreeNode extends DefaultTreeNode{
    
    public MyTreeNode(Object data, TreeNode parent){
        super(data, parent);
    }
    @Override
    public Object getData(){
        if (super.getData() instanceof Permiso){
          return (((Permiso)super.getData()).getPermisoTexto());
        }
        return super.getData();
    }
    
    public Object getRealData(){
        return super.getData();
    }
}