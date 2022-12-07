package es.physiotherapy.persistence.service;

import es.physiotherapy.persistence.Helpers.HelperMethods;
import es.physiotherapy.persistence.dao.appointment.AppointmentDAO;
import es.physiotherapy.persistence.dao.appointment.AppointmentDAOImpl;
import es.physiotherapy.persistence.dao.client.ClientDAO;
import es.physiotherapy.persistence.dao.client.ClientDAOImpl;
import es.physiotherapy.persistence.dao.treatedarea.TreatedAreaDAO;
import es.physiotherapy.persistence.dao.treatedarea.TreatedAreaDAOImpl;
import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PersonalDataService {
    private final ClientDAO clientDAO;
    private final AppointmentDAO appointmentDAO;
    private final TreatedAreaDAO treatedAreaDAO;

    public PersonalDataService() {
        this.clientDAO = new ClientDAOImpl();
        this.appointmentDAO = new AppointmentDAOImpl();
        this.treatedAreaDAO = new TreatedAreaDAOImpl();
    }

    public void createClient(Client client) {
        clientDAO.create(client);
    }

    public void updateClient(Client client) {
        clientDAO.save(client);
    }

    public void deleteClient(Client client) {
        clientDAO.delete(client);
    }

    public void updateAppointment(Appointment appointment) {
        appointmentDAO.save(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        appointmentDAO.delete(appointment);
    }

    public void createTreatedArea(TreatedArea treatedArea) {
        treatedAreaDAO.create(treatedArea);
    }

    public void updateTreatedArea(TreatedArea treatedArea) {
        treatedAreaDAO.save(treatedArea);
    }

    public void deleteTreatedArea(TreatedArea treatedArea) {
        treatedAreaDAO.delete(treatedArea);
    }

    public void createAppointment(Appointment appointment) {
        appointmentDAO.create(appointment);
    }

    public List<Client> getClientsByCity(String city) {
        return clientDAO.findClientsByCity(city);
    }

    public List<Client> getClientsAfterBirthDate(LocalDate date) {
        return clientDAO.findClientsBetweenBirthDate(date, LocalDate.now());
    }

    public List<Client> getClientsBeforeBirthDate(LocalDate date) {
        return clientDAO.findClientsBetweenBirthDate(LocalDate.EPOCH, date);
    }

    public List<Client> getAllClients() {
        return clientDAO.findAllClients();
    }

    public Client getClientByDni(String dni) {
        Optional<Client> opt = clientDAO.findById(dni);
        return opt.orElse(null);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAllAppointments();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentDAO.findAppointmentById(id);
    }

    public List<Appointment> getAppointmentsByDni(String dni) {
        return appointmentDAO.findAppointmentByDni(dni);
    }

    public TreatedArea getTreatedAreaById(long id) {
        Optional<TreatedArea> opt = treatedAreaDAO.findById(id);
        return opt.orElse(null);
    }

    public List<TreatedArea> getTreatedAreasByDni(String dni) {
        return treatedAreaDAO.findTreatedAreasByDni(dni);
    }

    public List<Appointment> getAppointmentsAfterDate(LocalDate date) {
        return appointmentDAO.findAppointmentsBetweenDate(date, LocalDate.now());
    }

    public List<Appointment> getAppointmentsBeforeDate(LocalDate date) {
        return appointmentDAO.findAppointmentsBetweenDate(LocalDate.EPOCH, date);
    }

    public List<Appointment> getAppointmentsBetweenDates(LocalDate date1, LocalDate date2) {
        return appointmentDAO.findAppointmentsBetweenDate(date1, date2);
    }

    public List<Appointment> getAppointmentsByTreatedArea(String[] areasString) {
        List<String> areas = HelperMethods.getListOfValidTreatedAreaStrings(areasString);
        return (areas.size() > 0)
                ? appointmentDAO.findAppointmentsByTreatedAreas(areas)
                : null;
    }
}
