package es.physiotherapy.persistence.dao.client;

import es.physiotherapy.persistence.dao.GenericDao;
import es.physiotherapy.persistence.entity.Client;

import java.time.LocalDate;
import java.util.List;

public interface ClientDAO extends GenericDao<Client, String> {
    List<Client> findClientsByCity(String city);

    List<Client> findClientsBetweenBirthDate(LocalDate initDate, LocalDate endDate);

    List<Client> findAllClients();
}
