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
public enum TipoContenido {
    PROMOCION, NOTICIA;

    @Override
    public String toString() {
        switch(this){
            case NOTICIA:
                return "NOTICIA";
            case PROMOCION:
                return "PROMOCION";
        }
        return "";
    }
    
    
}
