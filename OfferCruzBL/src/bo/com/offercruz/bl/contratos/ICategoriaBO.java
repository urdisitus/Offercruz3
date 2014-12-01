/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.contratos;

import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.enums.TipoOferta;
import java.util.List;

/**
 *
 * @author Ernesto
 */
public interface ICategoriaBO extends IGenericoBO<Categoria, Integer> {
    List<Categoria> obtenerTodasbyTipo(TipoOferta tipoOferta);
}
