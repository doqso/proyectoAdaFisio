package es.physiotherapy.persistence.dao.appointment;

import es.physiotherapy.persistence.dao.GenericDAOImpl;
import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Appointment_;
import es.physiotherapy.persistence.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class AppointmentDAOImpl extends GenericDAOImpl<Appointment, Long> implements AppointmentDAO {
    public AppointmentDAOImpl() {
        super(Appointment.class);
    }

    @Override
    public List<Appointment> findByDni(String dni) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Appointment> query = session.createQuery("from Appointment where client.dni = :dni", Appointment.class);
            query.setParameter("dni", dni);
            return query.getResultStream().toList();
        }
    }

    @Override
    public List<Appointment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
            Root<Appointment> root = criteria.from(Appointment.class);
            criteria.select(root);
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<Appointment> findBetweenDate(LocalDate initDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
            Root<Appointment> root = criteria.from(Appointment.class);

            criteria.where(builder.between(root.get(
                    Appointment_.date), initDate, endDate));
            return session.createQuery(criteria).getResultList();
        }
    }
}
