/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.impl;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
import bo.com.offercruz.dal.contrato.ICategoriaDAO;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.enums.TipoOferta;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Ernesto
 */
public class CategoriaHibernateDAO extends DAOGenericoHibernate<Categoria, Integer> implements ICategoriaDAO {

    @Override
    public List<Categoria> obtenerTodasbyTipo(TipoOferta tipoOferta) {
        Query query = getSession().createQuery("from Categoria c where c.tipo > :Tipo AND c.estado > 0 ");
        query.setParameter("Tipo", tipoOferta.ordinal());
        return query.list();
    }
    
    @Override
    public Integer obtenerIdPorNombre(String nombre){
         Query query = getSession().createQuery("SELECT id from Categoria c where c.nombre = :Nombre AND c.estado > 0 ");
        query.setParameter("Nombre", nombre);
        return (Integer)query.uniqueResult();
    }

    @Override
    public Categoria obtenerPorNombre(String nombre, Integer id) {
        Query query = getSession().createQuery("SELECT 1 from Categoria c where c.nombre = :Nombre AND c.estado > 0 AND c.id != :Id");
        query.setParameter("Nombre", nombre);
        query.setParameter("Id", id);
        return (Categoria)query.uniqueResult();
    }
    
     @Override
    public Categoria obtenerPorNombre(String nombre) {
        Query query = getSession().createQuery("SELECT 1 from Categoria c where c.nombre = :Nombre AND c.estado > 0 ");
        query.setParameter("Nombre", nombre);
        return (Categoria)query.uniqueResult();
    }

}
