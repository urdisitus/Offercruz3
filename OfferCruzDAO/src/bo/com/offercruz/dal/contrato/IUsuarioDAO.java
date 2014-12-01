/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.contrato;

import java.util.List;
import bo.com.offercruz.entidades.Usuario;

/**
 *
 * @author Ernesto
 */
public interface IUsuarioDAO extends IDAOGenerico<Usuario, Integer> {

    Integer getIdUsuarioPorCorreoElectronico(String email);

    Usuario logear(String usuario, String pass);

    Integer getIdPorLogin(String nombre);

    boolean checkId(Integer id);
}
