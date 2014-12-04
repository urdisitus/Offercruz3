/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;

import bo.com.offercruz.dal.contrato.IDAOGenerico;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Oferta;
import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 *
 * @author Ernesto
 */
public abstract class GestionEmpresaBO<T, ID extends Serializable, U extends IDAOGenerico<T, ID>> extends ObjetoNegocioGenerico<T, ID, U> {

    private Empresa empresa;

    @Override
    public void setIdUsuario(Integer idUsuario) {
        super.setIdUsuario(idUsuario);
        empresa = ejecutarEnTransaccion(new Callable<Empresa>() {

            @Override
            public Empresa call() throws Exception {
                Empresa emp = getDaoManager().getEmpresaDAO().obtenerEmpresa(idUsuario);
                if (emp.getImagen() != null) {
                    System.out.println(emp.getImagen().getNombre());
                }
                for (Object object : emp.getOfertas()) {
                    Oferta of = (Oferta) object;
                    System.out.println(of.getNombre());
                }
                for (Object cat : emp.getCategorias()) {
                    Categoria c = (Categoria) cat;
                    System.out.println(c.getNombre());
                }
                return emp;
            }
        });
    }

    public Empresa getEmpresa() {
        return empresa;
    }

}
