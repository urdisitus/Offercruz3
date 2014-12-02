/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Ernesto
 */
@ManagedBean
@SessionScoped
public class PermisoBean {

    public final String user = "usuario";
    public final String perfil = "perfil";
    public final String contenido = "contenido";
    public final String empresa = "empresa";
    public final String oferta = "oferta";
    public final String categoria = "categoria";

    public String getCategoria() {
        return categoria;
    }

    public String getContenido() {
        return contenido;
    }

    public String getOferta() {
        return oferta;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getPerfil() {
        return perfil;
    }

    public String getUser() {
        return user;
    }

}
