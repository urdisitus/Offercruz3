/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.bl.impl;

import bo.com.offercruz.bl.contratos.IPermisoBO;
import bo.com.offercruz.dal.contrato.IPermisoDAO;
import bo.com.offercruz.entidades.Permiso;
import java.util.List;

/**
 *
 * @author Ernesto
 */
public class PermisoBO extends ObjetoNegocioGenerico<Permiso, Integer, IPermisoDAO>
        implements IPermisoBO{

    @Override
    IPermisoDAO getObjetoDAO() {
        return getDaoManager().getPermisoDAO();
    }

    @Override
    public List<Permiso> getPermisosHijos(int idPermisoPadre, int idPerfil) {
        return ejecutarEnTransaccion(() -> {
            return getDaoManager().getPermisoDAO().obtenerPermisosHijos(idPermisoPadre, idPerfil);
        });        
    }

    @Override
    public List<Permiso> getPermisosHijos(int idPermisoPadre) {
        return ejecutarEnTransaccion(() -> {
            return getDaoManager().getPermisoDAO().obtenerPermisosHijos(idPermisoPadre);
        });
    }

    @Override
    public List<Permiso> getPermisosRaiz() {
        return ejecutarEnTransaccion(() -> {
            return getDaoManager().getPermisoDAO().obtenerPermisosPadres();
        });
    }
    
}
