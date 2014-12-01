/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.contrato.control;

import bo.com.offercruz.dal.contrato.ICategoriaDAO;
import bo.com.offercruz.dal.contrato.IClienteDAO;
import bo.com.offercruz.dal.contrato.IContenidoDAO;
import bo.com.offercruz.dal.contrato.IEmpresaDAO;
import bo.com.offercruz.dal.contrato.IImagenDAO;
import bo.com.offercruz.dal.contrato.IOfertaDAO;
import bo.com.offercruz.dal.contrato.IPerfilDAO;
import bo.com.offercruz.dal.contrato.IPermisoDAO;
import bo.com.offercruz.dal.contrato.ISolicitudDAO;
import bo.com.offercruz.dal.contrato.IUsuarioDAO;

/**
 *
 * @author Olvinho
 */
public interface IDAOManager {
    
    ICategoriaDAO getCategoriaDAO();
    
    IPerfilDAO getPerfilDAO();

    IContenidoDAO getContenidoDAO();
    
    IOfertaDAO getOfertaDAO();
    
    IEmpresaDAO getEmpresaDAO();
    
    IClienteDAO getClienteDAO();
    
    IImagenDAO getImagenDAO();
    
    ISolicitudDAO getSolicitudDAO();
    
    IUsuarioDAO getUsuarioDAO();
    
    IPermisoDAO getPermisoDAO();
    
    void iniciarTransaccion();

    void confirmarTransaccion();

    void cancelarTransaccion();
}
