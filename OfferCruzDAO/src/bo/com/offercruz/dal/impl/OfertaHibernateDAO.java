/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.impl;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
import bo.com.offercruz.dal.contrato.IOfertaDAO;
import bo.com.offercruz.entidades.Oferta;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Ernesto
 */
public class OfertaHibernateDAO extends DAOGenericoHibernate<Oferta, Integer> implements IOfertaDAO {

    @Override
    public Integer getIdPorNombre(String nombre) {
        Query query = getSession().createQuery("SELECT id from Oferta r WHERE r.nombre = :nombre");
        query.setParameter("nombre", nombre);
        Integer intte = (Integer) query.uniqueResult();
        return intte;
    }

    @Override
    public List<Oferta> obtenerTodas(Integer empresa) {
        Query query = getSession().createQuery("SELECT r from Oferta r join r.empresa e WHERE e.id = :empresa");
        query.setParameter("empresa", empresa);
        List<Oferta> ofertas = query.list();
        for (Oferta oferta : ofertas) {
            System.out.println(oferta.getCategoria().getNombre());
            System.out.println(oferta.getEmpresa().getRazonSocial());
        }
        return ofertas;
    }

    @Override
    public List<Oferta> obtenerTodos() {
        List<Oferta> ofertas = super.obtenerTodos();
        for (Oferta oferta : ofertas) {
            System.out.println(oferta.getCategoria().getNombre());
            System.out.println(oferta.getEmpresa().getRazonSocial());
        }
        return ofertas;
    }

    @Override
    public Oferta recuperarPorId(Integer id) {
        Oferta oferta = super.recuperarPorId(id);

        System.out.println(oferta.getCategoria().getNombre());
        System.out.println(oferta.getEmpresa().getRazonSocial());
        return oferta;
    }

    @Override
    public Oferta obtenerPorId(Integer id) {
        Oferta oferta = super.obtenerPorId(id);
        System.out.println(oferta.getCategoria().getNombre());
        System.out.println(oferta.getEmpresa().getRazonSocial());
        return oferta;
    }

}
