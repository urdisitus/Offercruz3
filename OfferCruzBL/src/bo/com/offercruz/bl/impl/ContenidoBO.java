/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;

import bo.com.offercruz.bl.contratos.IContenidoBO;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.dal.contrato.IContenidoDAO;
import bo.com.offercruz.entidades.Contenido;
import java.util.Date;

/**
 *
 * @author Ernesto
 */
public class ContenidoBO extends ObjetoNegocioGenerico<Contenido, Integer, IContenidoDAO> implements IContenidoBO {

    @Override
    IContenidoDAO getObjetoDAO() {
        return getDaoManager().getContenidoDAO();
    }

    @Override
    protected void preInsertar(Contenido entidad) {
        entidad.setEstado(1);
        entidad.setFechaCreacion(new Date());
        entidad.setFechaModificacion(new Date());
    }

    @Override
    protected void validar(Contenido entity) {
        boolean fechaPubValida = true;
        if (entity.getFechaPublicacion() == null) {
            appendException(new BusinessExceptionMessage("La fecha de publicación es un campo requerido.", "fecha_publicacion"));
            fechaPubValida = false;
        }
        if (entity.getFechaExpiracion() == null) {
            appendException(new BusinessExceptionMessage("La fecha de expiración es un campo requerido.", "fecha_expiracion"));
            fechaPubValida = false;
        }
        if (fechaPubValida) {
            if (!entity.getFechaExpiracion().after(entity.getFechaPublicacion())) {
                appendException(new BusinessExceptionMessage("La fecha de expiración debe ser mayor a la fecha de publicación.", "fechas"));
            }
        }
        if (entity.getDescripcion() == null || entity.getDescripcion().equals("")) {
            appendException(new BusinessExceptionMessage("La descripcion es un campo requerido.", "descripcion"));
        }
        if (entity.getTitulo() == null || entity.getTitulo().equals("")) {
            appendException(new BusinessExceptionMessage("El titulo es un campo requerido.", "titulo"));
        }
        if (entity.getImagens().isEmpty()) {
            appendException(new BusinessExceptionMessage("Usd. debe agregar una o mas imagenes al contenido.", "imagenes"));
        }
        if (entity.getDetallecontenidos().isEmpty()) {
            appendException(new BusinessExceptionMessage("Usd. debe agregar una o mas ofertas al contenido.", "detalle_contenido"));
        }
    }

    @Override
    protected void postActualizar(Contenido entidad) {
        entidad.setFechaModificacion(new Date());
    }

}
