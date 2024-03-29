package es.physiotherapy.persistence.dao.client;

import es.physiotherapy.persistence.dao.GenericDAOImpl;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.Client_;
import es.physiotherapy.persistence.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class ClientDAOImpl extends GenericDAOImpl<Client, String> implements ClientDAO {
    public ClientDAOImpl() {
        super(Client.class);
    }

    @Override
    public List<Client> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Client> criteria = builder.createQuery(Client.class);
            criteria.from(Client.class);
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<Client> findByCity(String city) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Client> query = session.createQuery("from Client where city = :city", Client.class);
            query.setParameter("city", city);
            return query.getResultStream().toList();
        }
    }

    @Override
    public List<Client> findByBirthDateBetweenDates(LocalDate initDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Client> criteria = builder.createQuery(Client.class);
            Root<Client> root = criteria.from(Client.class);

            criteria.where(builder.between(root.get(
                    Client_.birthDate), initDate, endDate));
            return session.createQuery(criteria).getResultList();
        }
    }
}