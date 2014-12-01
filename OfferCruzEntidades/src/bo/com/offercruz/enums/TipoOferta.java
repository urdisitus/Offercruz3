/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.enums;

/**
 *
 * @author Ernesto
 */
public enum TipoOferta {

    PRODUCTO, SERVICIO, AMBOS;

    @Override
    public String toString() {
        switch (this) {
            case PRODUCTO:
                return "Producto";
            case SERVICIO:
                return "Servicio";
            case AMBOS:
                return "Ambos";
        }
        return "";
    }

}
