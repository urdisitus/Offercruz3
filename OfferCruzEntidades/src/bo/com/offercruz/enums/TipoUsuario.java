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
public enum TipoUsuario {
    OFFERCRUZ, EMPRESA , CLIENTE;
    
       @Override
    public String toString() {
        switch (this) {
            case OFFERCRUZ:
                return "Tipo offercruz";
            case EMPRESA:
                return "Tipo empresa";
        }
        return "";
    }
}
