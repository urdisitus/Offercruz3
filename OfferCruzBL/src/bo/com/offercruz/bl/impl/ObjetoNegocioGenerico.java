 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;

import bo.com.offercruz.bl.contratos.IGenericoBO;
import bo.com.offercruz.bl.excepticiones.BusinessException;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.bl.excepticiones.PermisosInsuficientesException;
import bo.com.offercruz.dal.contrato.IDAOGenerico;
import bo.com.offercruz.dal.contrato.control.IDAOManager;
import bo.com.offercruz.dal.imp.control.FactoriaDAOManager;
import bo.com.offercruz.entidades.Permiso;
import bo.com.offercruz.entidades.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Olvinho
 * @param <T> Clase entidad
 * @param <ID> Clase que que representa el Id
 * @param <U> Clase DAO
 */
public abstract class ObjetoNegocioGenerico<T, ID extends Serializable, U extends IDAOGenerico<T, ID>>
        implements IGenericoBO<T, ID> {

    private IDAOManager daoManager;
    protected Integer idUsuario;
    protected Usuario usuarioActual;
    private BusinessException mensajesError;
    private String comandoPermiso;

    public ObjetoNegocioGenerico() {

    }

    @Override
    public String getComandoPermiso() {
        return comandoPermiso;
    }

    @Override
    public void setComandoPermiso(String comandoPermiso) {
        this.comandoPermiso = comandoPermiso;
    }

    public IDAOManager getDaoManager() {
        if (daoManager == null) {
            this.daoManager = FactoriaDAOManager.getDAOManager();
        }
        return daoManager;
    }

    protected <V> V ejecutarEnTransaccion(Callable<V> ejecutor) {
        boolean comiteado = false;
        try {
            getDaoManager().iniciarTransaccion();
            V result = ejecutor.call();
            getDaoManager().confirmarTransaccion();
            comiteado = true;
            return result;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(ObjetoNegocioGenerico.class.getName()).log(Level.SEVERE, null, ex);
            throw new BusinessException("Error de ejecución dentro de la transacción");
        } finally {
            if (!comiteado) {
                try {
                    getDaoManager().cancelarTransaccion();
                } catch (Exception e) {

                }
            }
        }
    }

    protected void appendException(BusinessExceptionMessage message) {
        if (mensajesError == null) {
            mensajesError = new BusinessException(message);
            return;
        }
        mensajesError.getMessages().add(message);
    }

    @Override
    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    @Override
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public T recuperarPorId(final ID id) {
        return ejecutarEnTransaccion(new Callable<T>() {
            @Override
            public T call() throws Exception {
                if (!tienePermiso()) {
                    throw new PermisosInsuficientesException("No tiene los privilegios necesarios para ejecutar esta acción, contacte al administrador");
                }
                T entidad = getObjetoDAO().recuperarPorId(id);
                if (entidad != null) {
                    despuesDeRecuperar(entidad);
                }
                return entidad;
            }
        });
    }

    @Override
    public List<T> obtenerTodos() {
        return ejecutarEnTransaccion(new Callable<List<T>>() {
            @Override
            public List<T> call() {
                if (!tienePermiso()) {
                    throw new PermisosInsuficientesException("No tiene los privilegios necesarios para ejecutar esta acción, contacte al administrador");
                }
                return getObjetoDAO().obtenerTodos();
            }
        });
    }

    @Override
    public List<T> obtenerNuevosObjetos(final Date fecha) {
        return ejecutarEnTransaccion(new Callable<List<T>>() {
            @Override
            public List<T> call() {
                if (!tienePermiso()) {
                    throw new PermisosInsuficientesException("No tiene los privilegios necesarios para ejecutar esta acción, contacte al administrador");
                }
                return getObjetoDAO().obtenerNuevosObjetos(fecha);
            }
        });
    }

    @Override
    public T insertar(T entity) {
        if (idUsuario == null) {
            throw new BusinessException("Debe definir el usuario que solicitando la acción para continuar");
        }
        final T x = entity;
        return ejecutarEnTransaccion(() -> {
            usuarioActual = getDaoManager().getUsuarioDAO().recuperarPorId(idUsuario);
            if (usuarioActual == null) {
                throw new BusinessException("El usuario especificado no existe");
            }
            if (!tienePermiso()) {
                throw new PermisosInsuficientesException("No tiene los privilegios necesarios para ejecutar esta acción, contacte al administrador");
            }
            mensajesError = null;
            validar(x);
            if (mensajesError != null) {
                throw mensajesError;
            }
            preInsertar(x);
            T aux = getObjetoDAO().persistir(x);
            postInsertar(aux);
            return aux;
        });
    }

    protected void validar(T entity) {

    }

    @Override
    public T actualizar(T entity) {
        if (idUsuario == null) {
            throw new BusinessException("Debe definir el usuario que solicitando la acción para continuar");
        }
        final T x = entity;
        return ejecutarEnTransaccion(new Callable<T>() {
            @Override
            public T call() {
                usuarioActual = getDaoManager().getUsuarioDAO().recuperarPorId(idUsuario);
                if (usuarioActual == null) {
                    throw new BusinessException("El usuario especificado no existe");
                }
                if (!tienePermiso()) {
                    throw new PermisosInsuficientesException("No tiene los privilegios necesarios para ejecutar esta acción, contacte al administrador");
                }
                mensajesError = null;
                validar(x);
                if (mensajesError != null) {
                    throw mensajesError;
                }
                preActualizar(x);
                T aux = getObjetoDAO().persistir(x);
                postActualizar(aux);
                return aux;
            }
        });
    }
    
    public boolean verificarPermiso(int codigo, Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        if (usuario.getPerfil() == null) {
            return false;
        }

        if (usuario.getPerfil().getId() == null) {
            return false;
        }
        List<Permiso> permisos = getDaoManager().getPermisoDAO().obtenerPermisos(usuario.getPerfil().getId());
        for (Permiso permiso : permisos) {
            if (permiso.getComando().equals(comandoPermiso)) {
                return true;
            }
        }
        return false;
    }

    public boolean verificarPermiso(String comandoPermiso, Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        if (usuario.getPerfil() == null) {
            return false;
        }

        if (usuario.getPerfil().getId() == null) {
            return false;
        }
        List<Permiso> permisos = getDaoManager().getPermisoDAO().obtenerPermisos(usuario.getPerfil().getId());
        for (Permiso permiso : permisos) {
            if (permiso.getComando().equals(comandoPermiso)) {
                return true;
            }
        }
        return false;
    }

    /**
     * *
     *
     * @return Verdadero si el usuario actual puede insertar.
     */
    private boolean tienePermiso() {
        if(comandoPermiso != null && (comandoPermiso.equals("cambiar_pass") || comandoPermiso.equals("permiso"))){
            return true;
        }
        if(usuarioActual== null && idUsuario!= null){               
            usuarioActual = getDaoManager().getUsuarioDAO().recuperarPorId(idUsuario);
        }
        return verificarPermiso(comandoPermiso, usuarioActual);
    }

    /**
     * *
     *
     * @return Verdadero si el usuario actual puede modificar.
     */

    protected int IdPermisoInsertar() {
        return 0;
    }

    protected int IdPermisoActualizar() {
        return 0;
    }

    abstract U getObjetoDAO();

    protected void despuesDeRecuperar(T entidad) {

    }

    protected boolean isNullOrEmpty(String valor) {
        return (valor == null) || ("".equals(valor));
    }

    protected void preInsertar(T entidad) {

    }

    protected void postInsertar(T entidad) {

    }

    protected void preActualizar(T entidad) {
    }

    protected void postActualizar(T entidad) {

    }
}
