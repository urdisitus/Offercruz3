/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.imp;

import bo.com.offercruz.bl.contratos.IPerfilBO;
import bo.com.offercruz.bl.contratos.IPermisoBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Permiso;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Ernesto
 */
public class InterpretadorMensajePerfil extends InterpretadorMensajeGenerico<Perfil, Integer, IPerfilBO> {

    @Override
    Perfil convertirHojaEnEntidad() {
        Perfil entidad = new Perfil();
        Cell celda;
        //Id
        celda = hojaActual.getCelda(4, 3);
        if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            entidad.setId((int) celda.getNumericCellValue());
        }
        //Nombre
        celda = hojaActual.getCelda(5, 3);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setNombre(hojaActual.getValorCeldaCadena(celda));
        }
        //Descripcion
        celda = hojaActual.getCelda(6, 3);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setDescripcion(hojaActual.getValorCeldaCadena(celda));
        }
        //Permisos
        int fila = 12;
        do {
            celda = hojaActual.getCelda(fila, 2);
            if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
                String permiso = hojaActual.getValorCeldaCadena(celda);
                Permiso p = new Permiso();
                p.setPermisoTexto(permiso);
                entidad.getPermisos().add(p);
            }
            fila++;
        } while (celda.getCellType() != Cell.CELL_TYPE_BLANK);
        return entidad;
    }

    @Override
    protected void preparPlantillaAntesDeEnviar(Workbook libro) {
        IPermisoBO permisoBO = FactoriaObjetosNegocio.getInstance().getIPermisoBO();
        permisoBO.setComandoPermiso(nombreEntidad);
        permisoBO.setIdUsuario(idUsuario);
        List<Permiso> pers = permisoBO.obtenerTodos();
        String[] permisos = new String[pers.size()];
        for (int j = 0; j < pers.size(); j++) {
            permisos[j] = pers.get(j).getPermisoTexto();
            hojaActual.setValorCelda(12 + j, 1, ((int) j + 1) + "");
            CellStyle style = libro.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            hojaActual.getCelda(12 + j, 2).setCellStyle(style);
        }
        hojaActual.agregarValidacionLista(12, 18, 2, 2, permisos, true, false);
    }

    @Override
    IPerfilBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIPerfilBO();
    }

    @Override
    boolean esNuevo(Perfil entidad) {
        return entidad.getId() == null;
    }

    @Override
    String getId(Perfil entidad) {
        return entidad.getId().toString();
    }

    @Override
    Integer convertirId(String cadena) throws Exception {
        return convertirIdAEntero(cadena);
    }

    @Override
    void mostrarLista(List<Perfil> lista) {
        int i = 5;
        for (Perfil perfil : lista) {
            hojaActual.setValorCelda(i, 1, perfil.getId());
            hojaActual.setValorCelda(i, 2, perfil.getNombre());
            hojaActual.setValorCelda(i, 3, perfil.getDescripcion());
            StringBuilder permisos = new StringBuilder();
            for (Object object : perfil.getPermisos()) {
                Permiso permiso = (Permiso) object;
                permisos.append(permiso.getPermisoTexto()).append(", ");
            }
            hojaActual.setValorCelda(i, 4, permisos.toString());
            i++;
        }
    }

    @Override
    void mostrarEntidad(Perfil entidad, Workbook libro) {
        preparPlantillaAntesDeEnviar(libro);
        hojaActual.setValorCelda(4, 3, entidad.getId());
        hojaActual.setValorCelda(5, 3, entidad.getNombre());
        hojaActual.setValorCelda(6, 3, entidad.getDescripcion());
        int index = 12;
        for (Object object : entidad.getPermisos()) {
            Permiso permiso = (Permiso) object;
            hojaActual.setValorCelda(index, 2, permiso.getPermisoTexto());
            index++;
        }
    }

}
