/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.dal.contrato;

import bo.com.offercruz.entidades.Perfil;

/**
 *
 * @author Ernesto
 */
public interface IPerfilDAO extends IDAOGenerico<Perfil, Integer> {
    boolean checkId(Integer id);
    Integer getIdPorNombre(String nombre);
}
