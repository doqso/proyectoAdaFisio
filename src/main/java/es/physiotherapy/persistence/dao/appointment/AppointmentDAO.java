package es.physiotherapy.persistence.dao.appointment;

import es.physiotherapy.persistence.dao.GenericDao;
import es.physiotherapy.persistence.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentDAO extends GenericDao<Appointment, Long> {

    List<Appointment> findByDni(String dni);

    List<Appointment> findAll();

    List<Appointment> findBetweenDate(LocalDate initDate, LocalDate endDate);
}