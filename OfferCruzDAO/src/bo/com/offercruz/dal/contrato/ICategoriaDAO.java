/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.dal.contrato;

import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.enums.TipoOferta;
import java.util.List;

/**
 *
 * @author Ernesto
 */
public interface ICategoriaDAO extends  IDAOGenerico<Categoria, Integer>{    
    List<Categoria> obtenerTodasbyTipo(TipoOferta tipoOferta);      
    Categoria obtenerPorNombre(String nombre, Integer id);
    Categoria obtenerPorNombre(String nombre);
    Integer obtenerIdPorNombre(String nombre);
}
