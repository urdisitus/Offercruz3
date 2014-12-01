/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.dal.contrato;

import bo.com.offercruz.entidades.Empresa;

/**
 *
 * @author Ernesto
 */
public interface IEmpresaDAO extends  IDAOGenerico<Empresa, Integer>{
    Empresa obtenerPorNombre(String nombre);   
    Integer obtenerIdPorNombre(String nombre);   
    Empresa obtenerEmpresa(Integer idUsuario);
}
