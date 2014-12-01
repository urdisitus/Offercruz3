/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.bl.contratos;

import bo.com.offercruz.entidades.Imagen;

/**
 *
 * @author Ernesto
 */
public interface IImagenBO extends IGenericoBO<Imagen, Integer>{
    Imagen getImagenRedimencionada(int id, int width);
}
