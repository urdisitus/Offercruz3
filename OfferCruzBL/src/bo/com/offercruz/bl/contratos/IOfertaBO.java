/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.bl.contratos;

import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Oferta;

/**
 *
 * @author Ernesto
 */
public interface IOfertaBO extends IGenericoBO<Oferta, Integer>{
    Empresa getEmpresa();
}
