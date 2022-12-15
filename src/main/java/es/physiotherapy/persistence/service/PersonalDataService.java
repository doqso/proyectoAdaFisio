package es.physiotherapy.persistence.service;

import es.physiotherapy.persistence.dao.appointment.AppointmentDAO;
import es.physiotherapy.persistence.dao.appointment.AppointmentDAOImpl;
import es.physiotherapy.persistence.dao.client.ClientDAO;
import es.physiotherapy.persistence.dao.client.ClientDAOImpl;
import es.physiotherapy.persistence.dao.treatedarea.TreatedAreaDAO;
import es.physiotherapy.persistence.dao.treatedarea.TreatedAreaDAOImpl;
import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.util.HelperMethods;
import es.physiotherapy.persistence.util.JSONReader;
import es.physiotherapy.persistence.util.XMLWritter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        client.getAppointments().forEach(a -> a.setId(null));
        clientDAO.create(client);
    }

    public void updateClient(Client client) {
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        clientDAO.save(client);
    }

    public void deleteClient(Client client) {
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        clientDAO.delete(client);
    }

    public void updateAppointment(Appointment appointment) {
        if (appointment == null) throw new IllegalArgumentException("Appointment cannot be null");
        appointmentDAO.save(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        if (appointment == null) throw new IllegalArgumentException("Appointment cannot be null");
        appointmentDAO.delete(appointment);
    }

    public void createTreatedArea(TreatedArea treatedArea) {
        if (treatedArea == null) throw new IllegalArgumentException("TreatedArea cannot be null");
        treatedAreaDAO.create(treatedArea);
    }

    public void updateTreatedArea(TreatedArea treatedArea) {
        if (treatedArea == null) throw new IllegalArgumentException("TreatedArea cannot be null");
        treatedAreaDAO.save(treatedArea);
    }

    public void deleteTreatedArea(TreatedArea treatedArea) {
        if (treatedArea == null) throw new IllegalArgumentException("TreatedArea cannot be null");
        treatedAreaDAO.delete(treatedArea);
    }

    public void createAppointment(Appointment appointment) {
        if (appointment == null) throw new IllegalArgumentException("Appointment cannot be null");
        appointment.setId(null);
        appointmentDAO.create(appointment);
    }

    public List<Client> getClientsByCity(String city) {
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City cannot be null");
        return clientDAO.findClientsByCity(city);
    }

    public List<Client> getClientsAfterBirthDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        return clientDAO.findClientsBetweenBirthDate(date, LocalDate.now());
    }

    public List<Client> getClientsBeforeBirthDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        return clientDAO.findClientsBetweenBirthDate(LocalDate.EPOCH, date);
    }

    public List<Client> getAllClients() {
        return clientDAO.findAllClients();
    }

    public Client getClientByDni(String dni) {
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("Dni cannot be null");
        Optional<Client> opt = clientDAO.findById(dni);
        return opt.orElse(null);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAllAppointments();
    }

    public Appointment getAppointmentById(long id) {
        if (id < 0) throw new IllegalArgumentException("Id cannot be negative");
        return appointmentDAO.findAppointmentById(id);
    }

    public List<Appointment> getAppointmentsByDni(String dni) {
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("Dni cannot be null");
        return appointmentDAO.findAppointmentByDni(dni);
    }

    public TreatedArea getTreatedAreaById(long id) {
        if (id < 0) throw new IllegalArgumentException("Id cannot be negative");
        Optional<TreatedArea> opt = treatedAreaDAO.findById(id);
        return opt.orElse(null);
    }


    public List<Appointment> getAppointmentsAfterDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        return appointmentDAO.findAppointmentsBetweenDate(date, LocalDate.now());
    }

    public List<Appointment> getAppointmentsBeforeDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        return appointmentDAO.findAppointmentsBetweenDate(LocalDate.EPOCH, date);
    }

    public List<Appointment> getAppointmentsBetweenDates(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) throw new IllegalArgumentException("Date cannot be null");
        return appointmentDAO.findAppointmentsBetweenDate(date1, date2);
    }

    public List<Appointment> getAppointmentsByTreatedArea(String[] areasString) throws NoSuchFieldException {
        if (areasString == null || areasString.length < 1)
            throw new IllegalArgumentException("Areas cannot be null or empty");
        List<String> areas = HelperMethods.getListOfValidTreatedAreaStrings(areasString);
        return (areas.size() > 0)
                ? appointmentDAO.findAppointmentsByTreatedAreas(areas)
                : null;
    }

    public void writeXmlFile(List<Object> list, String fileName) throws IOException {
        if (list == null || list.size() == 0 || fileName == null || fileName.trim().isBlank())
            throw new IllegalArgumentException("File name or list is null or empty");
        fileName = fileName.trim();
        if (!fileName.endsWith(".xml")) fileName += ".xml";
        switch (list.get(0).getClass().getSimpleName()) {
            case "Client" -> XMLWritter.createClientXmlFile(list.toArray(), fileName);
            case "Appointment" -> XMLWritter.createAppointmentXmlFile(list.toArray(), fileName);
        }
    }

    public <T> List<T> readJsonFile(String fileName, Class<T> type) throws IllegalAccessException, NoSuchFieldException {
        if (fileName == null || fileName.trim().isBlank())
            throw new IllegalArgumentException("File name is null or empty");
        fileName = fileName.trim();
        if (!fileName.endsWith(".json")) fileName += ".json";
        List<T> list = new ArrayList<>();
        switch (type.getSimpleName()) {
            case "Client" -> list = (List<T>) JSONReader.getClientsFromJsonFile(fileName);
            case "Appointment" -> list = (List<T>) JSONReader.getAppointmentsFromJsonFile(fileName);
        }
        return list;
    }
}
