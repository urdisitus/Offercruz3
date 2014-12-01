/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.impl;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
import bo.com.offercruz.dal.contrato.IPerfilDAO;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Permiso;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Ernesto
 */
public class PerfilHibernateDAO extends DAOGenericoHibernate<Perfil, Integer> implements IPerfilDAO {

    @Override
    public boolean checkId(Integer id) {
        Query query = getSession().createQuery("SELECT 1 from Perfil r WHERE r.id = :id");
        query.setParameter("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    public Integer getIdPorNombre(String nombre) {
        Query query = getSession().createQuery("SELECT id from Perfil r WHERE r.nombre = :nombre");
        query.setParameter("nombre", nombre);
        Integer intte = (Integer) query.uniqueResult();
        return intte;
    }

    @Override
    public Perfil obtenerPorId(Integer id) {
        Perfil p = super.obtenerPorId(id); //To change body of generated methods, choose Tools | Templates.
        for (Object object : p.getPermisos()) {
            System.out.println(((Permiso) object).getPermisoTexto());
        }
        return p;
    }

    @Override
    public List<Perfil> obtenerTodos() {
        List<Perfil> list = super.obtenerTodos(); //To change body of generated methods, choose Tools | Templates.
        for (Perfil perfil : list) {
            for (Object object : perfil.getPermisos()) {
                System.out.println(((Permiso) object).getPermisoTexto());
            }
        }
        return list;

    }

    @Override
    public Perfil recuperarPorId(Integer id) {
        Perfil p = super.recuperarPorId(id); //To change body of generated methods, choose Tools | Templates.
        for (Object object : p.getPermisos()) {
            System.out.println(((Permiso) object).getPermisoTexto());
        }
        return p;
    }

}
