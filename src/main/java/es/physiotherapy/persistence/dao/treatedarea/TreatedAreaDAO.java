package es.physiotherapy.persistence.dao.treatedarea;

import es.physiotherapy.persistence.dao.GenericDao;
import es.physiotherapy.persistence.entity.TreatedArea;

import java.util.List;

public interface TreatedAreaDAO extends GenericDao<TreatedArea, Long> {
    List<TreatedArea> findTreatedAreasByDni(String dni);
    List<TreatedArea> findAllTreatedAreas();
}
