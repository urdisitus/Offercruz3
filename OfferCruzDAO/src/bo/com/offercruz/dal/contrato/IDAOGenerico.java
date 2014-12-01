/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.contrato;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Olvinho
 * @param <T>
 * @param <ID>
 */
public interface IDAOGenerico<T, ID extends Serializable> {

    T obtenerPorId(ID id);
    
    boolean checkId(ID id);

    T recuperarPorId(ID id);

    List<T> obtenerTodos();

    List<T> buscarPorEjemplo(T exampleInstance, String... excludeProperty);

    T persistir(T entity);

    List<T> obtenerNuevosObjetos(Date fecha);

    void ejecutarOperacionesSesion();

    void limpiarSesion();
}
