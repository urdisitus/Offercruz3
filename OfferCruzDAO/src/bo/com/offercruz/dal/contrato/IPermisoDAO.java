/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.contrato;

import bo.com.offercruz.entidades.Permiso;
import java.util.List;

/**
 *
 * @author Ernesto
 */
public interface IPermisoDAO extends IDAOGenerico<Permiso, Integer> {

    Integer getIdPorNombre(String nombre);

    List<Permiso> obtenerPermisos(Integer idPerfil);

    List<Permiso> obtenerPermisosHijosPorTipo(Integer idPermiso, int tipo);

    List<Permiso> obtenerPermisosHijos(Integer idPermisoPadre);

    List<Permiso> obtenerPermisosHijos(Integer idPermisoPadre, int idPerfil);

    List<Permiso> obtenerPermisosPadres(int tipo);

}
