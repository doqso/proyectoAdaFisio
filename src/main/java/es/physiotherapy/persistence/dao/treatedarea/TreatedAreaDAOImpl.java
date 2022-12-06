package es.physiotherapy.persistence.dao.treatedarea;

import es.physiotherapy.persistence.dao.GenericDAOImpl;
import es.physiotherapy.persistence.entity.*;
import es.physiotherapy.persistence.util.HibernateUtil;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TreatedAreaDAOImpl extends GenericDAOImpl<TreatedArea, Long> implements TreatedAreaDAO {
    public TreatedAreaDAOImpl() {
        super(TreatedArea.class);
    }

    @Override
    public List<TreatedArea> findTreatedAreasByDni(String dni) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<TreatedArea> criteria = builder.createQuery(TreatedArea.class);
            Root<TreatedArea> root = criteria.from(TreatedArea.class);

            Join<Appointment, Client> join = root
                    .join(TreatedArea_.appointment)
                    .join(Appointment_.client);
            criteria.where(builder.equal(join.get(Client_.dni), dni));
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<TreatedArea> findAllTreatedAreas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<TreatedArea> criteria = builder.createQuery(TreatedArea.class);
            Root<TreatedArea> root = criteria.from(TreatedArea.class);
            criteria.select(root);
            return session.createQuery(criteria).getResultList();
        }
    }
}