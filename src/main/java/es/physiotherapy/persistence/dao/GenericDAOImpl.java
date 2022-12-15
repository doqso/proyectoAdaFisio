package es.physiotherapy.persistence.dao;

import es.physiotherapy.persistence.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

/**
 * @param <T> Entity
 * @param <R> Type of the primary key
 */
public class GenericDAOImpl<T, R> implements GenericDao<T, R> {
    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T create(T entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                session.flush();
                tx.commit();
                return entity;
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public void save(T entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.merge(entity);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public void deleteById(R id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                T entity = session.find(entityClass, id);
                session.remove(entity);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public void delete(T entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.remove(entity);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<T> findById(R id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(entityClass, id));
        }
    }
}
