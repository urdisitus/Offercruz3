/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.contratos;

import java.util.List;
import bo.com.offercruz.entidades.Permiso;

/**
 *
 * @author Ernesto
 */
public interface IPermisoBO extends IGenericoBO<Permiso, Integer> {

    List<Permiso> getPermisosHijos(int idPermisoPadre, int idPerfil);

    List<Permiso> obtenerPermisosHijosPorTipo(int idPermisoPadre, int tipo);

    List<Permiso> getPermisosHijos(int idPermisoPadre);

    List<Permiso> getPermisosRaiz(int tipo);
}
