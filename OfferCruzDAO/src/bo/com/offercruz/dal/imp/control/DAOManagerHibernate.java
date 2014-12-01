/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.imp.control;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
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
import bo.com.offercruz.dal.contrato.control.IDAOManager;
import bo.com.offercruz.dal.exception.DAOException;
import bo.com.offercruz.dal.impl.CategoriaHibernateDAO;
import bo.com.offercruz.dal.impl.ClienteHibernateDAO;
import bo.com.offercruz.dal.impl.ContenidoHibernateDAO;
import bo.com.offercruz.dal.impl.EmpresaHibernateDAO;
import bo.com.offercruz.dal.impl.ImagenHibernateDAO;
import bo.com.offercruz.dal.impl.OfertaHibernateDAO;
import bo.com.offercruz.dal.impl.PerfilHibernateDAO;
import bo.com.offercruz.dal.impl.PermisoHibernateDAO;
import bo.com.offercruz.dal.impl.SolicitudHibernateDAO;
import bo.com.offercruz.dal.impl.UsuarioHibernateDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Olvinho
 */
public class DAOManagerHibernate implements IDAOManager {

    private static final Logger LOG = Logger.getLogger(DAOManagerHibernate.class.getName());
    private SincronizablesInterceptor interceptor = new SincronizablesInterceptor();

    private ICategoriaDAO categoriaDAO;
    private IUsuarioDAO usuarioDAO;
    private IContenidoDAO contenidoDAO;
    private IEmpresaDAO empresaDAO;
    private IOfertaDAO ofertaDAO;
    private IImagenDAO imagenDAO;
    private ISolicitudDAO solicitudDAO;
    private IClienteDAO clienteDAO;
    private IPerfilDAO perfilDAO;
    private IPermisoDAO permisoDAO;

    public DAOManagerHibernate() {

    }

    @Override
    public IUsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) usuarioDAO);
        }
        return usuarioDAO;
    }
    
    @Override
    public IPerfilDAO getPerfilDAO() {
        if (perfilDAO == null) {
            perfilDAO = new PerfilHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) perfilDAO);
        }
        return perfilDAO;
    }

    private void asignarSesionActual(DAOGenericoHibernate dao) {
        if (dao != null) {
            dao.setSession(sesion);
        }
    }

    private void asignarNuevaSesion() {
        asignarSesionActual((DAOGenericoHibernate) categoriaDAO);
        asignarSesionActual((DAOGenericoHibernate) solicitudDAO);
        asignarSesionActual((DAOGenericoHibernate) imagenDAO);
        asignarSesionActual((DAOGenericoHibernate) contenidoDAO);
        asignarSesionActual((DAOGenericoHibernate) empresaDAO);
        asignarSesionActual((DAOGenericoHibernate) ofertaDAO);
        asignarSesionActual((DAOGenericoHibernate) clienteDAO);
        asignarSesionActual((DAOGenericoHibernate) imagenDAO);
        asignarSesionActual((DAOGenericoHibernate) usuarioDAO);
        asignarSesionActual((DAOGenericoHibernate) perfilDAO);
        asignarSesionActual((DAOGenericoHibernate) permisoDAO);
        
    }

    Session sesion;

    @Override
    public void iniciarTransaccion() {
        try {
            sesion = HibernateUtil.getSessionFactory().withOptions().interceptor(interceptor).openSession();
            asignarNuevaSesion();
            sesion.beginTransaction();
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
            throw new DAOException("Imposible iniciar la transacción", e);
        }

    }

    @Override
    public void confirmarTransaccion() {
        try {
            sesion.getTransaction().commit();
            sesion.close();
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
            throw new DAOException("Imposible confirmar la transacción", e);
        }

    }

    @Override
    public void cancelarTransaccion() {
        try {
            if (sesion.getTransaction() != null) {
                sesion.getTransaction().rollback();
            }
            sesion.close();
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, null, e);
            throw new DAOException("Imposible cancelar la trasancción", e);
        }

    }

    @Override
    public ICategoriaDAO getCategoriaDAO() {
        if (categoriaDAO == null) {
            categoriaDAO = new CategoriaHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) categoriaDAO);
        }
        return categoriaDAO;
    }

    @Override
    public IContenidoDAO getContenidoDAO() {
        if (contenidoDAO == null) {
            contenidoDAO = new ContenidoHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) contenidoDAO);
        }
        return contenidoDAO;
    }

    @Override
    public IOfertaDAO getOfertaDAO() {
        if (ofertaDAO == null) {
            ofertaDAO = new OfertaHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) ofertaDAO);
        }
        return ofertaDAO;
    }

    @Override
    public IEmpresaDAO getEmpresaDAO() {
        if (empresaDAO == null) {
            empresaDAO = new EmpresaHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) empresaDAO);
        }
        return empresaDAO;
    }

    @Override
    public IClienteDAO getClienteDAO() {
        if (clienteDAO == null) {
            clienteDAO = new ClienteHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) clienteDAO);
        }
        return clienteDAO;
    }

    @Override
    public IImagenDAO getImagenDAO() {
        if (imagenDAO == null) {
            imagenDAO = new ImagenHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) imagenDAO);
        }
        return imagenDAO;
    }

    @Override
    public ISolicitudDAO getSolicitudDAO() {
        if (solicitudDAO == null) {
            solicitudDAO = new SolicitudHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) solicitudDAO);
        }
        return solicitudDAO;
    }

    @Override
    public IPermisoDAO getPermisoDAO() {
         if (permisoDAO == null) {
            permisoDAO = new PermisoHibernateDAO();
            asignarSesionActual((DAOGenericoHibernate) permisoDAO);
        }
        return permisoDAO;
    }

}
