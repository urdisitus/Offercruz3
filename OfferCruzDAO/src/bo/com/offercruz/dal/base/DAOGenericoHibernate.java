/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.dal.base;

import bo.com.offercruz.dal.contrato.IDAOGenerico;
import bo.com.offercruz.dal.imp.control.HibernateUtil;
import bo.com.offercruz.entidades.contrato.ISincronizable;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.metadata.ClassMetadata;

/**
 *
 * @author Olvinho
 * @param <T>
 * @param <ID>
 */
public abstract class DAOGenericoHibernate<T, ID extends Serializable> implements IDAOGenerico<T, ID> {

    private final Class<T> persistentClass;
    private Session session;

    public DAOGenericoHibernate() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public void setSession(Session s) {
        this.session = s;
    }

    protected Session getSession() {
        if (session == null) {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        }
        return session;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public T obtenerPorId(ID id) {
        T entity;
        entity = (T) getSession().load(getPersistentClass(), id);
        return entity;
    }

    @Override
    public T recuperarPorId(ID id) {
        Query query = getSession().createQuery("from " + getPersistentClass().getName() + " c where c.estado > :Estado AND c.id = :Id ");
        query.setParameter("Estado", 0);
        query.setParameter("Id", id);
        return (T) query.uniqueResult();
    }

    @Override
    public List<T> obtenerTodos() {
        if (isEntidadEliminacionLogica()) {
            Query query = getSession().createQuery("from " + getPersistentClass().getName() + " c where c.estado > 0");
            return query.list();
        } else {
            return findByCriteria();
        }
    }

    @Override
    public List<T> buscarPorEjemplo(T exampleInstance, String... excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
    
    @Override
    public boolean checkId(ID id) {
        Query query = getSession().createQuery("SELECT 1 from " + getPersistentClass().getName() + " u WHERE u.id = :Id ");
        query.setParameter("Id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    public T persistir(T entity) {
        return (T) getSession().merge(entity);
    }

    @Override
    public void ejecutarOperacionesSesion() {
        getSession().flush();
    }

    @Override
    public void limpiarSesion() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     *
     * @param criterion
     * @return
     */
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit
                = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    public boolean isEntidadEliminacionLogica() {
        ClassMetadata classMetadata = getSession().getSessionFactory().getClassMetadata(getPersistentClass());
        String[] propertyNames = classMetadata.getPropertyNames();
        for (String string : propertyNames) {
            if ("estado".equals(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<T> obtenerNuevosObjetos(Date ultimaFecha) {
        if (ISincronizable.class.isAssignableFrom(getPersistentClass())) {
            Query query = getSession().createQuery("from " + getPersistentClass().getName() + " c where c.fechaModificacion > :fecha AND c.estado > 0 ");
            query.setParameter("fecha", ultimaFecha);
            return query.list();
        }
        return new ArrayList<>();
    }

}
