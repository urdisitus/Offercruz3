/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;

import bo.com.offercruz.bl.contratos.IPerfilBO;
import bo.com.offercruz.bl.contratos.IPermisoBO;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.dal.contrato.IPerfilDAO;
import bo.com.offercruz.dal.contrato.IPermisoDAO;
import bo.com.offercruz.dal.imp.control.FactoriaDAOManager;
import bo.com.offercruz.dal.impl.PermisoHibernateDAO;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Permiso;
import bo.com.offercruz.entidades.Usuario;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author Ernesto
 */
public class PerfilBO extends ObjetoNegocioGenerico<Perfil, Integer, IPerfilDAO>
        implements IPerfilBO {

    @Override
    IPerfilDAO getObjetoDAO() {
        return getDaoManager().getPerfilDAO();
    }

//    @Override
//    public boolean verificarPermiso(String comandoPermiso, Usuario usuario) {
//        
//        if (usuario == null) {
//            return false;
//        }
//
//        if (usuario.getPerfil() == null) {
//            return false;
//        }
//
//        if (usuario.getPerfil().getId() == null) {
//            return false;
//        }
//        return ejecutarEnTransaccion(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                List<Permiso> permisos = getDaoManager().getPermisoDAO().obtenerPermisos(usuario.getPerfil().getId());
//                for (Permiso permiso : permisos) {
//                    if(permiso.getComando().equals(comandoPermiso)){
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//    }

    @Override
    protected void validar(Perfil entity) {
        //Nombre 
        boolean nombreValido = true;
        if (isNullOrEmpty(entity.getNombre())) {
            appendException(new BusinessExceptionMessage("El nombre es un campo requerido.", "nombre"));
            nombreValido = false;
        } else if (entity.getNombre().trim().length() > 50) {
            appendException(new BusinessExceptionMessage("El nombre no puede tener más de 50 carácteres", "nombre"));
            nombreValido = false;
        }

        if (nombreValido) {
            if (entity.getId() == null) {
                //Insercion
                Integer idPerfil = getObjetoDAO().getIdPorNombre(entity.getNombre());
                if (idPerfil != null) {
                    appendException(new BusinessExceptionMessage("El Perfil '" + entity.getNombre() + "' ya existe.", "nombre"));
                }
            } else {
                //Actualizacion
                if (!getObjetoDAO().checkId(entity.getId())) {
                    appendException(new BusinessExceptionMessage("EL perfil con Id  '" + entity.getId() + "' no existe.", "id"));
                } else {
                    Perfil perf = getObjetoDAO().obtenerPorId(entity.getId());
                    entity.setFechaCreacion(perf.getFechaCreacion());
                    entity.setEstado(perf.getEstado());
                }
            }
        }
        //Permisos 
        if (entity.getPermisos().isEmpty()) {
            appendException(new BusinessExceptionMessage("El perfil debe tener uno o mas permisos asignados.", "permisos"));
        } else {
            Set nuevosPermisos = new HashSet();
            for (Object permiso : entity.getPermisos()) {
                Permiso p = (Permiso) permiso;
                boolean existe = false;
                for (Object object : nuevosPermisos) {
                    Permiso pp = (Permiso) object;
                    if (pp.getPermisoTexto().equals(p.getPermisoTexto())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    nuevosPermisos.add(p);
                }
            }
            entity.setPermisos(nuevosPermisos);
            IPermisoDAO permisoDAO = FactoriaDAOManager.getDAOManager().getPermisoDAO();
            for (Object object : nuevosPermisos) {
                Permiso pp = (Permiso) object;
                pp.setId(permisoDAO.getIdPorNombre(pp.getPermisoTexto()));
            }
        }
    }

    @Override
    protected void preActualizar(Perfil entidad) {
        entidad.setFechaModificacion(new Date());
    }

    @Override
    protected void preInsertar(Perfil entidad) {
        entidad.setEstado(1);
        entidad.setFechaCreacion(new Date());
        entidad.setFechaModificacion(new Date());
    }

}
