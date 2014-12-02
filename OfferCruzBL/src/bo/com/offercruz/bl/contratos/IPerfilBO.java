/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.contratos;

import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Usuario;

/**
 *
 * @author Ernesto
 */
public interface IPerfilBO extends IGenericoBO<Perfil, Integer> {

    boolean verificarPermiso(String comandoPermiso, Usuario usuario);
    boolean verificarPermiso(int codigoPermiso, Usuario usuario);
}
