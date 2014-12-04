/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.imp;

import bo.com.offercruz.bl.contratos.ICategoriaBO;
import bo.com.offercruz.bl.contratos.IEmpresaBO;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import bo.com.offercruz.entidades.Permiso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author juanCarlos
 */
public class InterpretadorMensajeEmpresa extends InterpretadorMensajeGenerico<Empresa, Integer, IEmpresaBO> {

    @Override
    Empresa convertirHojaEnEntidad() {
        Empresa entidad = new Empresa();
        Cell celda;
        //Id
        celda = hojaActual.getCelda(3, 2);
        if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            entidad.setId((int) celda.getNumericCellValue());
        }
        //Razon social
        celda = hojaActual.getCelda(4, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setRazonSocial(hojaActual.getValorCeldaCadena(celda));
        }
        //Slogan
        celda = hojaActual.getCelda(5, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setSlogan(hojaActual.getValorCeldaCadena(celda));
        }

        celda = hojaActual.getCelda(6, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setMision(hojaActual.getValorCeldaCadena(celda));
        }
        celda = hojaActual.getCelda(7, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setVision(hojaActual.getValorCeldaCadena(celda));
        }
        celda = hojaActual.getCelda(8, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setTelefono(hojaActual.getValorCeldaCadena(celda));
        }
        celda = hojaActual.getCelda(9, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setDireccion(hojaActual.getValorCeldaCadena(celda));
        }
        celda = hojaActual.getCelda(10, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setTipoSociedad(hojaActual.getValorCeldaCadena(celda));
        }
        celda = hojaActual.getCelda(11, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setCorreoElectronico(hojaActual.getValorCeldaCadena(celda));
        }
        celda = hojaActual.getCelda(12, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setFax(hojaActual.getValorCeldaCadena(celda));
        }
        celda = hojaActual.getCelda(13, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setNit(hojaActual.getValorCeldaCadena(celda));
        }
//        celda = hojaActual.getCelda(14, 2);
//        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
////            try {
////                //entidad.setFechaApertura(new SimpleDateFormat("dd/MM/yyyy").parse(hojaActual.getValorCeldaCadena(celda)));
////            } catch (ParseException ex) {
////                Logger.getLogger(InterpretadorMensajeEmpresa.class.getName()).log(Level.SEVERE, null, ex);
////            }
//        }
        //Categorias
        int fila = 18;
        do {
            celda = hojaActual.getCelda(fila, 1);
            if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
                String permiso = hojaActual.getValorCeldaCadena(celda);
                Categoria p = new Categoria();
                p.setNombre(permiso);
                entidad.getCategorias().add(p);
            }
            fila++;
        } while (celda.getCellType() != Cell.CELL_TYPE_BLANK);
        return entidad;
    }

    @Override
    IEmpresaBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIEmpresaBO();
    }

    @Override
    boolean esNuevo(Empresa entidad) {
        return entidad.getId() == null;
    }

    @Override
    String getId(Empresa entidad) {
        return entidad.getId().toString();
    }

    @Override
    Integer convertirId(String cadena) throws Exception {
        return convertirIdAEntero(cadena);
    }

    @Override
    void mostrarLista(List<Empresa> lista) {
        int i = 5;
        for (Empresa empresa : lista) {
            hojaActual.setValorCelda(i, 1, empresa.getId());
            hojaActual.setValorCelda(i, 2, empresa.getRazonSocial());
            if (empresa.getSlogan() != null) {
                hojaActual.setValorCelda(i, 3, empresa.getSlogan());
            }
            if (empresa.getMision() != null) {
                hojaActual.setValorCelda(i, 4, empresa.getMision());
            }
            if (empresa.getVision() != null) {
                hojaActual.setValorCelda(i, 5, empresa.getVision());
            }
            if (empresa.getTelefono() != null) {
                hojaActual.setValorCelda(i, 6, empresa.getTelefono());
            }
            if (empresa.getDireccion() != null) {
                hojaActual.setValorCelda(i, 7, empresa.getDireccion());
            }
            if (empresa.getTipoSociedad() != null) {
                hojaActual.setValorCelda(i, 8, empresa.getTipoSociedad());
            }
           // hojaActual.setValorCelda(i, 9, empresa.getCorreoElectronico());
            if (empresa.getFax() != null) {
                hojaActual.setValorCelda(i, 10, empresa.getFax());
            }
            if (empresa.getNit() != null) {
                hojaActual.setValorCelda(i, 11, empresa.getNit());
            }
            if (empresa.getFechaApertura() != null) {
                hojaActual.setValorCelda(i, 12, empresa.getFechaApertura().toString());
            }

            StringBuilder categorias = new StringBuilder();
            for (Object object : empresa.getCategorias()) {
                Categoria categoria = (Categoria) object;
                categorias.append(categoria.getNombre()).append(", ");
            }
            hojaActual.setValorCelda(i, 13, categorias.toString());
            i++;
        }
    }

    @Override
    void mostrarEntidad(Empresa entidad, Workbook libro) {
        preparPlantillaAntesDeEnviar(libro);
        int i = 5;
        hojaActual.setValorCelda(3, 2, entidad.getId());
        hojaActual.setValorCelda(4, 2, entidad.getRazonSocial());
        if (entidad.getSlogan() != null) {
            hojaActual.setValorCelda(5, 2, entidad.getSlogan());
        }
        if (entidad.getMision() != null) {
            hojaActual.setValorCelda(6, 2, entidad.getMision());
        }
        if (entidad.getVision() != null) {
            hojaActual.setValorCelda(7, 2, entidad.getVision());
        }
        if (entidad.getTelefono() != null) {
            hojaActual.setValorCelda(8, 2, entidad.getTelefono());
        }
        if (entidad.getDireccion() != null) {
            hojaActual.setValorCelda(9, 2, entidad.getDireccion());
        }
        if (entidad.getTipoSociedad() != null) {
            hojaActual.setValorCelda(10, 2, entidad.getTipoSociedad());
        }
        hojaActual.setValorCelda(11, 2, entidad.getCorreoElectronico());
        if (entidad.getFax() != null) {
            hojaActual.setValorCelda(12, 2, entidad.getFax());
        }
        if (entidad.getNit() != null) {
            hojaActual.setValorCelda(13, 2, entidad.getNit());
        }
        if (entidad.getFechaApertura() != null) {
            hojaActual.setValorCelda(14, 2, entidad.getFechaApertura().toString());
        }
        int index = 18;
        for (Object object : entidad.getCategorias()) {
            Categoria categoria = (Categoria) object;
            hojaActual.setValorCelda(index, 1, categoria.getNombre());
            index++;
        }

//            StringBuilder categorias = new StringBuilder();
//            for (Object object : empresa.getCategoriaempresas()) {
//                Categoria categoria = (Categoria) object;
//                categorias.append(categoria.getNombre()).append(", ");
//            }   
    }

    @Override
    protected void preparPlantillaAntesDeEnviar(Workbook libro) {
        ICategoriaBO categoriaBO = FactoriaObjetosNegocio.getInstance().getICategoriaBO();
        categoriaBO.setComandoPermiso(nombreEntidad);
        categoriaBO.setIdUsuario(idUsuario);
        List<Categoria> categorias = categoriaBO.obtenerTodos();
        String[] categs = new String[categorias.size()];
        for (int j = 0; j < categorias.size(); j++) {
            categs[j] = categorias.get(j).getNombre();
            hojaActual.setValorCelda(18 + j, 0, ((int) j + 1) + "");
            CellStyle style = libro.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            hojaActual.getCelda(18 + j, 1).setCellStyle(style);
        }

        hojaActual.agregarValidacionLista(18, 17 + categorias.size(), 1, 1, categs, true, false);
    }

}
