/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.impl;

import bo.com.offercruz.dal.base.DAOGenericoHibernate;
import bo.com.offercruz.dal.contrato.IPerfilDAO;
import bo.com.offercruz.dal.contrato.IUsuarioDAO;
import bo.com.offercruz.dal.imp.control.FactoriaDAOManager;
import bo.com.offercruz.entidades.Usuario;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Ernesto
 */
public class UsuarioHibernateDAO extends DAOGenericoHibernate<Usuario, Integer> implements IUsuarioDAO {

    @Override
    public Integer getIdUsuarioPorCorreoElectronico(String email) {
        Query query = getSession().createQuery("SELECT id FROM Usuario u WHERE u.correoElectronico = :CorreoElectronico AND u.estado > 0 ");
        query.setParameter("CorreoElectronico", email);
        return (Integer) query.uniqueResult();
    }

    @Override
    public Usuario logear(String usuario, String pass) {
        Query query = getSession().createQuery("FROM Usuario u WHERE u.login = :Login AND u.password = :Password AND u.estado > 0 ");
        query.setParameter("Login", usuario);
        query.setParameter("Password", pass);
        return (Usuario) query.uniqueResult();
    }

    @Override
    public Integer getIdPorLogin(String login) {
        Query query = getSession().createQuery("SELECT id from Usuario u WHERE u.login = :Login AND u.estado > 0 ");
        query.setParameter("Login", login);
        return (Integer) query.uniqueResult();
    }

   

    @Override
    public Usuario recuperarPorId(Integer id) {
        Usuario user = super.recuperarPorId(id); //To change body of generated methods, choose Tools | Templates.
        System.out.println(user.getPerfil().getNombre());
        return user;
    }
    
    

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> users = super.obtenerTodos();
        for (Usuario usuario : users) {
            System.out.println(usuario.getPerfil().getNombre());
        }
        return users;
    }

}
