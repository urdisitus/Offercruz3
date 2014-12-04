/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.imp;

import bo.com.offercruz.bl.contratos.IOfertaBO;
import bo.com.offercruz.bl.excepticiones.BusinessException;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Oferta;
import bo.com.offercruz.enums.TipoOferta;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Ernesto
 */
public class InterpretadorMensajeOferta extends InterpretadorMensajeGenerico<Oferta, Integer, IOfertaBO> {

    @Override
    public void setIdUsuario(Integer idUsuario) {
        super.setIdUsuario(idUsuario); //To change body of generated methods, choose Tools | Templates.
        //ObtenerEmpresa desde usuario        
    }

    @Override
    Oferta convertirHojaEnEntidad() {
        Oferta entidad = new Oferta();
        Cell celda;
        // Id
        celda = hojaActual.getCelda(3, 2);
        if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            entidad.setId((int) celda.getNumericCellValue());
        }
        //Nombre
        celda = hojaActual.getCelda(4, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setNombre(hojaActual.getValorCeldaCadena(celda));
        }
        //Nombre
        celda = hojaActual.getCelda(5, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setDescripcion(hojaActual.getValorCeldaCadena(celda));
        }

        //Precio Unitario
        celda = hojaActual.getCelda(7, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            double precio = -1;
            try {
                precio = Double.parseDouble(hojaActual.getValorCeldaCadena(celda).trim());
            } catch (Exception e) {
            }
            entidad.setPrecioUnitario(precio);
        }
        //Tipo oferta
        celda = hojaActual.getCelda(6, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            String tipoCategoria = hojaActual.getValorCeldaCadena(celda);
            if (tipoCategoria.equals(TipoOferta.PRODUCTO.toString())) {
                entidad.setTipoOferta(TipoOferta.PRODUCTO.ordinal());
            } else if (tipoCategoria.equals(TipoOferta.SERVICIO.toString())) {
                entidad.setTipoOferta(TipoOferta.SERVICIO.ordinal());
            } else {
                entidad.setTipoOferta(TipoOferta.AMBOS.ordinal());
            }
        }
        //Categoria
        celda = hojaActual.getCelda(8, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            String categoria = hojaActual.getValorCeldaCadena(celda);
            Categoria c = new Categoria();
            c.setNombre(categoria);
            entidad.setCategoria(c);
        }
        return entidad;
    }

    @Override
    IOfertaBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIOfertaBO();
    }

    @Override
    boolean esNuevo(Oferta entidad) {
        return entidad.getId() == null;
    }

    @Override
    String getId(Oferta entidad) {
        return entidad.getId().toString();
    }

    @Override
    protected void preparPlantillaAntesDeEnviar(Workbook libro) {
        //Tipo oferta
        String[] descripciones = new String[TipoOferta.values().length];
        for (int i = 0; i < TipoOferta.values().length; i++) {
            descripciones[i] = TipoOferta.values()[i].toString();
        }
        hojaActual.agregarValidacionLista(6, 6, 2, 2, descripciones, true, true);

        //Categoria        
        if (getObjetoNegocio().getEmpresa() == null) {
            appendException(new BusinessExceptionMessage("Debe ser un usuario empresa para poder realizar esta operacion", "autentificacion"));
        } else {
            String[] categoria = new String[getObjetoNegocio().getEmpresa().getCategorias().size()];
            int i = 0;
            for (Object object : getObjetoNegocio().getEmpresa().getCategorias()) {
                Categoria c = (Categoria) object;
                categoria[i] = c.getNombre();
                i++;
            }
            hojaActual.agregarValidacionLista(8, 8, 2, 2, categoria, true, true);
        }
    }

    @Override
    Integer convertirId(String cadena) throws Exception {
        return convertirIdAEntero(cadena);
    }

    @Override
    void mostrarLista(List<Oferta> lista) {
        int i = 5;
        for (Oferta oferta : lista) {
            hojaActual.setValorCelda(i, 1, oferta.getId());
            hojaActual.setValorCelda(i, 2, oferta.getNombre());
            hojaActual.setValorCelda(i, 3, oferta.getDescripcion());
            hojaActual.setValorCelda(i, 4, TipoOferta.values()[oferta.getTipoOferta()].toString());
            hojaActual.setValorCelda(i, 5, oferta.getPrecioUnitario() + " Bs.");
            hojaActual.setValorCelda(i, 6, oferta.getCategoria().getNombre());
            i++;
        }
    }

    @Override
    void mostrarEntidad(Oferta entidad, Workbook libro) {
        preparPlantillaAntesDeEnviar(libro);
        if (getObjetoNegocio().getEmpresa() == null) {
            appendException(new BusinessExceptionMessage("Debe ser un usuario de tipo empresa para poder gestionar ofertas", "autentificacion"));
        } else {
            hojaActual.setValorCelda(3, 2, entidad.getId());
            hojaActual.setValorCelda(4, 2, entidad.getNombre());
            hojaActual.setValorCelda(5, 2, entidad.getDescripcion());
            hojaActual.setValorCelda(6, 2, TipoOferta.values()[entidad.getTipoOferta()].toString());
            hojaActual.setValorCelda(7, 2, entidad.getPrecioUnitario());
            hojaActual.setValorCelda(8, 2, entidad.getCategoria().getNombre());
        }
    }

}
