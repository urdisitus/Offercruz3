/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.dal.contrato;

import bo.com.offercruz.entidades.Contenido;
import java.util.List;

/**
 *
 * @author Ernesto
 */
public interface IContenidoDAO extends  IDAOGenerico<Contenido, Integer>{
    List<Contenido> obtenerTodas(Integer idEmpresa);
}
