/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.impl;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
import bo.com.offercruz.dal.contrato.IPermisoDAO;
import bo.com.offercruz.entidades.Permiso;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Ernesto
 */
public class PermisoHibernateDAO extends DAOGenericoHibernate<Permiso, Integer> implements IPermisoDAO {

    @Override
    public Integer getIdPorNombre(String nombre) {
        Query query = getSession().createQuery("SELECT id from Permiso r WHERE r.permisoTexto = :nombre");
        query.setParameter("nombre", nombre);
        Integer intte = (Integer) query.uniqueResult();
        return intte;
    }

    @Override
    public List<Permiso> obtenerPermisos(Integer idPermiso) {
        Query query = getSession().createQuery("select p from Permiso p join p.perfils t WHERE t.id = :idPerfil ");
        query.setParameter("idPerfil", idPermiso);
        return query.list();
    }

    @Override
    public List<Permiso> obtenerPermisosHijos(Integer idPermiso) {
        Query query = getSession().createQuery("select p from Permiso p WHERE p.permisoPadreId = :idPermiso ");
        query.setParameter("idPermiso", idPermiso);
        return query.list();
    }

    @Override
    public List<Permiso> obtenerPermisosHijos(Integer idPermisoPadre, int idPerfil) {
        Query query = getSession().createQuery("select p from Permiso p join p.perfils t WHERE p.permisoPadreId = :idPermisoPadre AND t.id = :idPerfil ");
        query.setParameter("idPerfil", idPerfil);
        query.setParameter("idPermisoPadre", idPermisoPadre);
        return query.list();
    }

    @Override
    public List<Permiso> obtenerPermisosPadres() {
        Query query = getSession().createQuery("select p from Permiso p WHERE p.permisoPadreId = null ");
        return query.list();
    }

}
