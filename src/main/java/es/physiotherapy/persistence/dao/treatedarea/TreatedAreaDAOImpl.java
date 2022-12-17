package es.physiotherapy.persistence.dao.treatedarea;

import es.physiotherapy.persistence.dao.GenericDAOImpl;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public class TreatedAreaDAOImpl extends GenericDAOImpl<TreatedArea, Long> implements TreatedAreaDAO {
    public TreatedAreaDAOImpl() {
        super(TreatedArea.class);
    }

    @Override
    public List<TreatedArea> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<TreatedArea> criteria = builder.createQuery(TreatedArea.class);
            Root<TreatedArea> root = criteria.from(TreatedArea.class);
            criteria.select(root);
            return session.createQuery(criteria).getResultList();
        }
    }
}