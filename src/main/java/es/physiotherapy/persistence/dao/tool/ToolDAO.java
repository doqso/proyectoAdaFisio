package es.physiotherapy.persistence.dao.tool;

import es.physiotherapy.persistence.dao.GenericDao;
import es.physiotherapy.persistence.entity.Tool;

import java.util.List;

public interface ToolDAO extends GenericDao<Tool, Long> {
    List<Tool> findByAppointment(Long appointmentId);
    List<Tool> findAll();
}
