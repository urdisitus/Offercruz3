/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.impl;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
import bo.com.offercruz.dal.contrato.IEmpresaDAO;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Empresa;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Ernesto
 */
public class EmpresaHibernateDAO extends DAOGenericoHibernate<Empresa, Integer> implements IEmpresaDAO {

    @Override
    public Empresa obtenerEmpresa(Integer idUsuario) {
        Query query = getSession().createQuery("Select c from Empresa c join c.usuario u where u.id = :idUsuario ");
        query.setParameter("idUsuario", idUsuario);
        return (Empresa) query.uniqueResult();
    }

    @Override
    public List<Empresa> obtenerTodos() {
        List<Empresa> empresas = super.obtenerTodos(); //To change body of generated methods, choose Tools | Templates.
        for (Empresa empresa : empresas) {
            for (Object object : empresa.getCategorias()) {
                Categoria c = (Categoria) object;
                System.out.println(c.getNombre());
            }
            System.out.println(empresa.getUsuario().getLogin());
        }
        return empresas;
    }

    @Override
    public Empresa obtenerPorId(Integer id) {
        Empresa empresa = super.obtenerPorId(id); //To change body of generated methods, choose Tools | Templates.
        for (Object object : empresa.getCategorias()) {
            Categoria c = (Categoria) object;
            System.out.println(c.getNombre());
        }
        return empresa;
    }

    @Override
    public Empresa recuperarPorId(Integer id) {
        Empresa empresa = super.recuperarPorId(id); //To change body of generated methods, choose Tools | Templates.
        for (Object object : empresa.getCategorias()) {
            Categoria c = (Categoria) object;
            System.out.println(c.getNombre());
        }
        return empresa;
    }

    @Override
    public Integer obtenerIdPorNombre(String nombre) {

        /*Query query = getSession().createQuery("SELECT 1 from " + Categoria.class.getName() + " c where c.nombre = :Nombre AND c.estado > 0 AND c.id != :Id");
         query.setParameter("Nombre", nombre);
         query.setParameter("Id", id);
         return (Categoria)query.uniqueResult();*/
        Query query = getSession().createQuery("SELECT id from Empresa c where c.razonSocial = :Nombre AND c.estado > 0 ");
        query.setParameter("Nombre", nombre);

        return (Integer) query.uniqueResult();
    }
    
    @Override
    public Empresa obtenerPorNombre(String nombre) {

        /*Query query = getSession().createQuery("SELECT 1 from " + Categoria.class.getName() + " c where c.nombre = :Nombre AND c.estado > 0 AND c.id != :Id");
         query.setParameter("Nombre", nombre);
         query.setParameter("Id", id);
         return (Categoria)query.uniqueResult();*/
        Query query = getSession().createQuery("SELECT 1 from " + Empresa.class.getName() + " c where c.razonSocial = :Nombre AND c.estado > 0 ");
        query.setParameter("Nombre", nombre);

        return (Empresa) query.uniqueResult();
    }

}
