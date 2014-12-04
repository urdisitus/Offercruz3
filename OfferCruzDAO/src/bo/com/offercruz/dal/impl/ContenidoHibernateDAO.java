/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.impl;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
import bo.com.offercruz.dal.contrato.IContenidoDAO;
import bo.com.offercruz.entidades.Contenido;
import bo.com.offercruz.entidades.Imagen;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Ernesto
 */
public class ContenidoHibernateDAO extends DAOGenericoHibernate<Contenido, Integer> implements IContenidoDAO {

    @Override
    public List<Contenido> obtenerTodas(Integer idEmpresa) {
        Query query = getSession().createQuery("SELECT r from Contenido r join r.empresa e WHERE e.id = :empresa");
        query.setParameter("empresa", idEmpresa);
        List<Contenido> contenidos = query.list();
        for (Contenido contenido : contenidos) {
            for (Object object : contenido.getImagens()) {
                Imagen img = (Imagen) object;
                System.out.println(img.getNombre());
            }
            System.out.println(contenido.getEmpresa().getRazonSocial());
        }
        return contenidos;
    }

    @Override
    public List<Contenido> obtenerTodos() {
        List<Contenido> contenidos = super.obtenerTodos();
        for (Contenido contenido : contenidos) {
            for (Object object : contenido.getImagens()) {
                Imagen img = (Imagen) object;
                System.out.println(img.getNombre());
            }
            System.out.println(contenido.getEmpresa().getRazonSocial());
        }
        return contenidos;
    }

    @Override
    public Contenido recuperarPorId(Integer id) {
        Contenido contenido = super.recuperarPorId(id);
        for (Object object : contenido.getImagens()) {
            Imagen img = (Imagen) object;
            System.out.println(img.getNombre());
        }

        System.out.println(contenido.getEmpresa().getRazonSocial());
        return contenido;
    }

    @Override
    public Contenido obtenerPorId(Integer id) {
        Contenido contenido = super.obtenerPorId(id);
         for (Object object : contenido.getImagens()) {
            Imagen img = (Imagen) object;
            System.out.println(img.getNombre());
        }
        System.out.println(contenido.getEmpresa().getRazonSocial());
        return contenido;
    }
}
