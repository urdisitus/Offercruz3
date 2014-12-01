/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;

import bo.com.offercruz.bl.contratos.IOfertaBO;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.dal.contrato.IOfertaDAO;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Oferta;
import bo.com.offercruz.enums.TipoOferta;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 *
 * @author Ernesto
 */
public class OfertaBO extends ObjetoNegocioGenerico<Oferta, Integer, IOfertaDAO> implements IOfertaBO {

    private Empresa empresa;

    @Override
    IOfertaDAO getObjetoDAO() {
        return getDaoManager().getOfertaDAO();
    }

    @Override
    public void setIdUsuario(Integer idUsuario) {
        super.setIdUsuario(idUsuario);
        empresa = ejecutarEnTransaccion(new Callable<Empresa>() {

            @Override
            public Empresa call() throws Exception {
                Empresa emp = getDaoManager().getEmpresaDAO().obtenerEmpresa(idUsuario);
                for (Object cat : emp.getCategorias()) {
                    Categoria c = (Categoria) cat;
                    System.out.println(c.getNombre());
                }
                return emp;
            }
        });
    }

    @Override
    public List<Oferta> obtenerTodos() {
        List<Oferta> ofertas = new ArrayList<Oferta>();
        if (empresa == null) {
            ofertas.addAll(super.obtenerTodos());
        } else {
            ofertas.addAll(ejecutarEnTransaccion(new Callable<List<Oferta>>() {

                @Override
                public List<Oferta> call() throws Exception {
                    return getObjetoDAO().obtenerTodas(empresa.getId());
                }
            }));
        }
        return ofertas;
    }

    @Override
    public Empresa getEmpresa() {
        return empresa;
    }

    @Override
    protected void validar(Oferta entity) {
        //Nombre 
        boolean nombreValido = true;
        boolean esActualizacion = false;
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
                    appendException(new BusinessExceptionMessage("La Oferta '" + entity.getNombre() + "' ya existe.", "nombre"));
                }
            } else {
                //Actualizacion
                esActualizacion = true;
                if (!getObjetoDAO().checkId(entity.getId())) {
                    appendException(new BusinessExceptionMessage("EL perfil con Id  '" + entity.getId() + "' no existe.", "id"));
                } else {
                    Oferta perf = getObjetoDAO().obtenerPorId(entity.getId());
                    entity.setFechaCreacion(perf.getFechaCreacion());
                    entity.setEstado(perf.getEstado());
                }
            }
        }

        //Descripcion
        if (isNullOrEmpty(entity.getDescripcion())) {
            appendException(new BusinessExceptionMessage("La descripcion es un campo requerido.", "descripcion"));
        } else if (entity.getDescripcion().trim().length() > 200) {
            appendException(new BusinessExceptionMessage("La descripcion no puede tener más de 200 carácteres", "decripcion"));
        }

        //Categoria
        if (entity.getCategoria() == null) {
            appendException(new BusinessExceptionMessage("La categoria es un campo requerido", "categoria"));
        } else {
            //Buscamos por nombre
            if (isNullOrEmpty(entity.getCategoria().getNombre())) {
                appendException(new BusinessExceptionMessage("La categoria es un campo requerido", "categoria"));
            } else {
                Integer cc = getDaoManager().getCategoriaDAO().obtenerIdPorNombre(entity.getCategoria().getNombre());
                if (cc != null) {
                    entity.getCategoria().setId(cc);
                }
                if (entity.getCategoria().getId() == null) {
                    appendException(new BusinessExceptionMessage("La categoria '" + entity.getCategoria().getNombre() + "' no existe", "categoria"));
                }
            }
        }

        entity.setEmpresa(empresa);
        //Empresa 
        if (entity.getEmpresa() == null) {
            appendException(new BusinessExceptionMessage("La empresa es un campo requerido", "empresa"));
        } else {
            if (esActualizacion) {
                //Actualizacion
                Oferta of = getObjetoDAO().obtenerPorId(entity.getId());
                if (!Objects.equals(of.getEmpresa().getId(), entity.getEmpresa().getId())) {
                    appendException(new BusinessExceptionMessage("La empresa no coincide con la empresa que creo la oferta", "empresa"));
                }
            }
        }

        //Precio Unitario
        if (entity.getPrecioUnitario() <= 0) {
            appendException(new BusinessExceptionMessage("El precio unitario debe ser mayor a Cero (0)", "precio unitario"));
        }
    }

    @Override
    protected void preInsertar(Oferta entidad) {
        entidad.setEstado(1);
        entidad.setFechaCreacion(new Date());
        entidad.setFechaModificacion(new Date());
    }

    @Override
    protected void preActualizar(Oferta entidad) {
        entidad.setFechaModificacion(new Date());
    }

}
