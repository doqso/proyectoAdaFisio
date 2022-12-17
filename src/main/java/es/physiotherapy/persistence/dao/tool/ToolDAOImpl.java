package es.physiotherapy.persistence.dao.tool;

import es.physiotherapy.persistence.dao.GenericDAOImpl;
import es.physiotherapy.persistence.entity.*;
import es.physiotherapy.persistence.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ToolDAOImpl extends GenericDAOImpl<Tool, Long> implements ToolDAO {
    public ToolDAOImpl() {
        super(Tool.class);
    }

    @Override
    public List<Tool> findByAppointment(Long appointmentId) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Tool> criteria = builder.createQuery(Tool.class);
            Join<Tool, Appointment> join = criteria.from(Tool.class).join(Tool_.appointments);
            criteria.where(builder.equal(join.get(Appointment_.id), appointmentId));
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<Tool> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Tool> criteria = builder.createQuery(Tool.class);
            criteria.from(Tool.class);
            return session.createQuery(criteria).getResultList();
        }
    }
}
