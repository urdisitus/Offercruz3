/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;

import bo.com.offercruz.bl.contratos.IEmpresaBO;
import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import static bo.com.offercruz.bl.impl.UsuarioBO.SHA256;
import static bo.com.offercruz.bl.impl.UsuarioBO.getStringMessageDigest;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.dal.contrato.ICategoriaDAO;
import bo.com.offercruz.dal.contrato.IEmpresaDAO;
import bo.com.offercruz.dal.contrato.IUsuarioDAO;
import bo.com.offercruz.dal.imp.control.FactoriaDAOManager;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Usuario;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author juanCarlos
 */
public class EmpresaBO extends ObjetoNegocioGenerico<Empresa, Integer, IEmpresaDAO> implements IEmpresaBO {

    @Override
    IEmpresaDAO getObjetoDAO() {
        return getDaoManager().getEmpresaDAO();
    }

    public boolean validarEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private final Pattern pattern = Pattern.compile(UsuarioBO.EMAIL_PATTERN);

    @Override
    protected void validar(Empresa entity) {
        //Nombre
        boolean nombreValido = true;
        if (isNullOrEmpty(entity.getRazonSocial())) {
            appendException(new BusinessExceptionMessage("La razón social es un campo requerido.", "nombre"));
            nombreValido = false;
        }
        if (nombreValido && entity.getId() == null) {
            Integer emp = getObjetoDAO().obtenerIdPorNombre(entity.getRazonSocial());
            if (emp != null) {
                appendException(new BusinessExceptionMessage("La empresa '" + entity.getRazonSocial() + "' ya existe.", "nombre"));
            }
        }

        //Email
        if (entity.getId() == null) {
            if ((getDaoManager().getUsuarioDAO().getIdUsuarioPorCorreoElectronico(entity.getCorreoElectronico()) != null)) {
                appendException(new BusinessExceptionMessage("El email " + entity.getCorreoElectronico() + " ya esta registrado", "email"));
            }
            if (isNullOrEmpty(entity.getCorreoElectronico())) {
                appendException(new BusinessExceptionMessage("El email es un campo requerido", "email"));
            } else if (entity.getCorreoElectronico().length() > 50) {
                appendException(new BusinessExceptionMessage("El email no puede tener más de 50 carácteres", "email"));
            } else if (!validarEmail(entity.getCorreoElectronico())) {
                appendException(new BusinessExceptionMessage("El email no es válido", "email"));
            }
        }
    //        else if (getObjetoDAO().checkId(entity.getId())) {
//            Empresa emp = getDaoManager().getEmpresaDAO().recuperarPorId(entity.getId());
//            Usuario actual = getDaoManager().getUsuarioDAO().obtenerPorId(emp.getUsuario().getId());
//            if (!actual.getCorreoElectronico().equalsIgnoreCase(entity.getCorreoElectronico())) {
//                if ((getDaoManager().getUsuarioDAO().getIdUsuarioPorCorreoElectronico(entity.getCorreoElectronico()) != null)) {
//                    appendException(new BusinessExceptionMessage("El email " + entity.getCorreoElectronico() + " ya esta registrado", "email"));
//                }
//                actual.setCorreoElectronico(entity.getCorreoElectronico());
//            }
//            entity.setUsuario(actual);
//        }

        // VALIDAR FECHA
        //Permisos 
        if (!entity.getCategorias().isEmpty()) {
//            appendException(new BusinessExceptionMessage("La empresa debe tener una o mas categorias asignadas.", "categorias"));
//        } else {
            Set nuevosPermisos = new HashSet();
            for (Object permiso : entity.getCategorias()) {
                Categoria p = (Categoria) permiso;
                boolean existe = false;
                for (Object object : nuevosPermisos) {
                    Categoria pp = (Categoria) object;
                    if (pp.getNombre().equals(p.getNombre())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    nuevosPermisos.add(p);
                }
            }
            entity.setCategorias(nuevosPermisos);
            ICategoriaDAO permisoDAO = FactoriaDAOManager.getDAOManager().getCategoriaDAO();
            for (Object object : nuevosPermisos) {
                Categoria pp = (Categoria) object;
                pp.setId(permisoDAO.obtenerIdPorNombre(pp.getNombre()));
            }
        }
    }

    @Override
    protected void preInsertar(Empresa entidad) {
        IUsuarioDAO objUsuario = getDaoManager().getUsuarioDAO();
        entidad.setEstado(1);
        entidad.setFechaCreacion(new Date());
        entidad.setFechaModificacion(new Date());
        Perfil p = new Perfil();
        p.setId(getDaoManager().getPerfilDAO().getIdPorNombre("AdministradorEmpresa"));
        Usuario user = new Usuario();
        user.setPerfil(p);
        user.setLogin(entidad.getRazonSocial().replaceAll(" ", ""));
        user.setCorreoElectronico(entidad.getCorreoElectronico());
        user.setTipo(2);
        user.setEstado(1);
        user.setFechaCreacion(new Date());
        user.setFechaModificacion(new Date());
        user.setPassword(encriptar(UsuarioBO.cadenaAleatoria(15)));
        entidad.setUsuario(objUsuario.persistir(user));
    }

    public String encriptar(String texto) {
        return UsuarioBO.getStringMessageDigest(texto, SHA256);
    }

    @Override
    protected void preActualizar(Empresa entidad) {
        // IUsuarioDAO 
        entidad.setFechaModificacion(new Date());

    }

}
