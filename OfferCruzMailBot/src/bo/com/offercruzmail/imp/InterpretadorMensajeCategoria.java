/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.imp;

import bo.com.offercruz.bl.contratos.ICategoriaBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.enums.TipoOferta;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Ernesto
 */
public class InterpretadorMensajeCategoria extends InterpretadorMensajeGenerico<Categoria, Integer, ICategoriaBO> {

    @Override
    Categoria convertirHojaEnEntidad() {
        Categoria entidad = new Categoria();
        Cell celda;
        //Id
        celda = hojaActual.getCelda(3, 2);
        if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            entidad.setId((int) celda.getNumericCellValue());
        }
        //Nombre
        celda = hojaActual.getCelda(4, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setNombre(hojaActual.getValorCeldaCadena(celda));
        }
        //Tipo categora
        celda = hojaActual.getCelda(5, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            String tipoCategoria = hojaActual.getValorCeldaCadena(celda);
            if (tipoCategoria.equals(TipoOferta.PRODUCTO.toString())) {
                entidad.setTipo(TipoOferta.PRODUCTO.ordinal());
            } else if (tipoCategoria.equals(TipoOferta.SERVICIO.toString())) {
                entidad.setTipo(TipoOferta.SERVICIO.ordinal());
            } else {
                entidad.setTipo(TipoOferta.AMBOS.ordinal());
            }
        }
        return entidad;
    }

    @Override
    protected void preparPlantillaAntesDeEnviar(Workbook libro) {
        String[] descripciones = new String[TipoOferta.values().length];
        for (int i = 0; i < TipoOferta.values().length; i++) {
            descripciones[i] = TipoOferta.values()[i].toString();
        }
        hojaActual.agregarValidacionLista(5, 5, 2, 2, descripciones, true, true);
    }

    @Override
    ICategoriaBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getICategoriaBO();
    }

    @Override
    boolean esNuevo(Categoria entidad) {
        return entidad.getId() == null;
    }

    @Override
    String getId(Categoria entidad) {
        return entidad.getId().toString();
    }

    @Override
    Integer convertirId(String cadena) throws Exception {
        return convertirIdAEntero(cadena);
    }

    @Override
    void mostrarLista(List<Categoria> lista) {
        int i = 5;
        for (Categoria categoria : lista) {
            hojaActual.setValorCelda(i, 1, categoria.getId());
            hojaActual.setValorCelda(i, 2, categoria.getNombre());
            hojaActual.setValorCelda(i, 3, TipoOferta.values()[categoria.getTipo()].toString());
            i++;
        }
    }

    @Override
    void mostrarEntidad(Categoria entidad, Workbook libro) {
        preparPlantillaAntesDeEnviar(libro);
        hojaActual.setValorCelda(3, 2, entidad.getId());
        hojaActual.setValorCelda(4, 2, entidad.getNombre());
        hojaActual.setValorCelda(5, 2, TipoOferta.values()[entidad.getTipo()].toString());
    }

}
