package es.physiotherapy.persistence.dao.appointment;

import es.physiotherapy.persistence.dao.GenericDAOImpl;
import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Appointment_;
import es.physiotherapy.persistence.entity.Client_;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.util.HibernateUtil;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class AppointmentDAOImpl extends GenericDAOImpl<Appointment, Long> implements AppointmentDAO {
    public AppointmentDAOImpl() {
        super(Appointment.class);
    }

    @Override
    public Appointment findAppointmentById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
            Root<Appointment> root = criteria.from(Appointment.class);

            criteria.where(builder.equal(root.get(Appointment_.id), id));
            return session.createQuery(criteria).getSingleResult();
        }
    }

    @Override
    public List<Appointment> findAppointmentsByDni(String dni) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
            Root<Appointment> root = criteria.from(Appointment.class);
            criteria.select(root)
                    .where(builder.equal(root.get(Appointment_.client).get(Client_.dni), dni));
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<Appointment> findAllAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
            Root<Appointment> root = criteria.from(Appointment.class);
            criteria.select(root);
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<Appointment> findAppointmentsBetweenDate(LocalDate initDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
            Root<Appointment> root = criteria.from(Appointment.class);

            criteria.where(builder.between(root.get(
                    Appointment_.date), initDate, endDate));
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<Appointment> findAppointmentsByTreatedAreas(List<String> areas) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
            Root<Appointment> root = criteria.from(Appointment.class);

            Join<Appointment, TreatedArea> join = root.join(Appointment_.treatedArea);
            Predicate predicate = builder.conjunction();
            for (String area : areas) {
                predicate = builder.and(predicate, builder.isTrue(join.get(area)));
            }

            criteria.where(predicate);
            return session.createQuery(criteria).getResultList();
        }
    }

    @Override
    public List<Appointment> findAppointmentsBetweenDateByClient(String dni, LocalDate initDate, LocalDate endDate) {
        return null;
    }
}
