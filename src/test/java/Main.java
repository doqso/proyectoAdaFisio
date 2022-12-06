import es.physiotherapy.persistence.Helpers.ASCIIColors;
import es.physiotherapy.persistence.dao.appointment.AppointmentDAO;
import es.physiotherapy.persistence.dao.appointment.AppointmentDAOImpl;
import es.physiotherapy.persistence.entity.*;
import es.physiotherapy.persistence.Helpers.HelperMethods;
import es.physiotherapy.persistence.service.PersonalDataService;
import jakarta.persistence.metamodel.SingularAttribute;
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    private static final PersonalDataService PDS = new PersonalDataService();

    public static void main(String[] args) {
        try {
            getAppointmentsByTreatedAreas();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void createAppointmentAndTreatedArea() {
        Appointment appointment = new Appointment();
        appointment.setClient(PDS.getClientByDni("12345678A"));
        appointment.setDate(LocalDate.now());
        appointment.setDuration(60);
        appointment.setTime(Time.valueOf(LocalTime.parse("10:00")));
        String[] areas = {"knee", "elbow", "hip"};
        TreatedArea treatedArea = HelperMethods.treatedAreaBuilder(areas, null);
        appointment.setTreatedArea(treatedArea);
        PDS.createAppointment(appointment);
    }

    private static void createClientAndAppointment() {
        Client client = new Client();
        client.setDni("x5744414A");
        client.setName("Paco");
        client.setSurname("Perez");
        client.setCity("Madrid");
        client.setAddress("Calle de la calle");
        client.setPhone("666666666");
        client.setBirthDate(LocalDate.of(1990, 1, 1));

        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now());
        appointment.setDuration(60);
        appointment.setTime(Time.valueOf(LocalTime.parse("10:00")));

        client.setAppointments(List.of(appointment));
        appointment.setClient(client);
        PDS.createClient(client);
    }

    private static void getAppointmentsByTreatedAreas(){
        String[] areas = {"cervical", "lumbara", "elbow"};
        PDS.getAppointmentssByTreatedArea(areas)
                .forEach(a -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                a + ASCIIColors.RESET.getColor()));
    }
}
