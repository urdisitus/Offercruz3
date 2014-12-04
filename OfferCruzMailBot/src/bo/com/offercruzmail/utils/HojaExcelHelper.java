/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.utils;

import java.util.Date;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Olvinho
 */
public class HojaExcelHelper {

    private Sheet hoja;

    public HojaExcelHelper(Sheet hoja) {
        this.hoja = hoja;
    }

    public Sheet getHoja() {
        return hoja;
    }

    public void setHoja(Sheet hoja) {
        this.hoja = hoja;
    }

    public Cell getCelda(int rowIndex, int colIndex) {
        Row fila = hoja.getRow(rowIndex);
        if (fila == null) {
            fila = hoja.createRow(rowIndex);
        }

        Cell celda = fila.getCell(colIndex);
        if (celda == null) {
            celda = fila.createCell(colIndex);
        }

        return celda;

    }
    
     public void setValorCelda(int rowIndex, int colIndex, Integer valor) {
        Cell celda = getCelda(rowIndex, colIndex);
        if (valor != null) {
            celda.setCellValue(valor);
        }
    }

    public void setValorCelda(int rowIndex, int colIndex, String valor) {
        Cell celda = getCelda(rowIndex, colIndex);
        if (valor != null) {
            celda.setCellValue(valor);
        }
    }

    public void setValorCelda(int rowIndex, int colIndex, Float valor) {
        Cell celda = getCelda(rowIndex, colIndex);
        if (valor != null) {
            celda.setCellValue(valor);
        }
    }

    public void setValorCelda(int rowIndex, int colIndex, Short valor) {
        Cell celda = getCelda(rowIndex, colIndex);
        if (valor != null) {
            celda.setCellValue(valor);
        }
    }

    public void setValorCelda(int rowIndex, int colIndex, Date valor) {
        Cell celda = getCelda(rowIndex, colIndex);
        if (valor != null) {
            celda.setCellValue(valor);
            CreationHelper createHelper =hoja.getWorkbook().getCreationHelper();
            CellStyle estiloFecha = hoja.getWorkbook().createCellStyle();
            estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yy"));
            celda.setCellStyle(estiloFecha);
        }
    }

    public void setValorCelda(int rowIndex, int colIndex, Double valor) {
        Cell celda = getCelda(rowIndex, colIndex);
        if (valor != null) {
            celda.setCellValue(valor);
        }
    }

    public void setValorCelda(int rowIndex, int colIndex, Byte valor) {
        Cell celda = getCelda(rowIndex, colIndex);
        if (valor != null) {
            celda.setCellValue(valor);
        }
    }
    
    /**
     * *
     * Devuelve el valor de la celda en cadena
     *
     * @param celda La celda que contien el valor
     * @return El valor de la celda en cadena, si la celda es nula, devuelve una
     * cadena vacía
     */
    public static String getValorCelda(Cell celda) {
        if (celda == null) {
            return "";
        }
        switch (celda.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return celda.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(celda.getBooleanCellValue());
            case Cell.CELL_TYPE_NUMERIC:
                double valor = celda.getNumericCellValue();
                if (valor % 1 == 0) {
                    return String.valueOf((int) valor);
                }
                return String.valueOf(valor);
            case Cell.CELL_TYPE_FORMULA:
            case Cell.CELL_TYPE_ERROR:
            case Cell.CELL_TYPE_BLANK:
            default:
                return "";
        }
    }

    public void agregarValidacionLista(int primerFila, int ultimaFila, int primerColumna, int ultimaColumna,
            String[] valores, boolean mostrarCombo, boolean mostrarError) {
        if (valores != null && valores.length > 0) {
            CellRangeAddressList celdas = new CellRangeAddressList(primerFila, ultimaFila, primerColumna, ultimaColumna);
            DataValidationHelper dvHelper = hoja.getDataValidationHelper();
            DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(valores);
            DataValidation validation = dvHelper.createValidation(dvConstraint, celdas);
            validation.setSuppressDropDownArrow(mostrarCombo);
            validation.setShowErrorBox(mostrarError);
            hoja.addValidationData(validation);
        }
    }
    
    public  String getValorCeldaCadena(Cell celda) {
        return HojaExcelHelper.getValorCelda(celda);
    }

}
