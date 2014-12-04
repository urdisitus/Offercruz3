/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.bl.contratos;

import bo.com.offercruz.entidades.Contenido;
import bo.com.offercruz.entidades.Empresa;

/**
 *
 * @author Ernesto
 */
public interface IContenidoBO extends IGenericoBO<Contenido, Integer>{
    Empresa getEmpresa();
}
