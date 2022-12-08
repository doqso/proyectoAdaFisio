import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.service.PersonalDataService;
import es.physiotherapy.persistence.util.ASCIIColors;
import es.physiotherapy.persistence.util.HelperMethods;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    private static final PersonalDataService PDS = new PersonalDataService();

    public static void main(String[] args) throws IOException {
        readJson(Client.class, "cosis");
    }

    private static <T> void readJson(Class<T> entityClass, String filename) {
        String source = "input/" + filename + ".json";
        JSONArray jsonArray = new JSONObject(source).getJSONArray(entityClass.getSimpleName().toLowerCase());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            for (Field field : entityClass.getDeclaredFields()) {
                T objectToBuild = null;
                try {
                    objectToBuild = entityClass.getDeclaredConstructor().newInstance();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         NoSuchMethodException e) {
                    throw new RuntimeException("Error creating object from JSON file", e);
                }
                field.setAccessible(true);
                try {
                    field.set(objectToBuild, jsonObject.get(field.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error setting field value " + field.getName(), e);
                }
                try {
                    System.out.println(field.get(objectToBuild));
                } catch (IllegalAccessException e) {
                    System.err.println("Error getting field value " + field.getName());
                }
            }
        }
    }

    private static <T> void testingGenericMethods(T[] entities) {
        if (entities.length == 0) return;
        var entityClass = entities[0].getClass();
        // get field values
        for (T entity : entities) {
            for (Field f : entityClass.getDeclaredFields()) {
                f.setAccessible(true);
                String fieldType = f.getType().getSimpleName();
                if (fieldType.equals("Client") || fieldType.equals("TreatedArea")
                        || fieldType.equals("Appointment")
                        || fieldType.equals("List")) continue;
                try {
                    System.out.println(f.getName() + ": " + f.get(entity));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
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

    private static void getAppointmentsByTreatedAreas() throws NoSuchFieldException {
        String[] areas = {"cervical", "lumbar"};
        PDS.getAppointmentsByTreatedArea(areas)
                .forEach(a -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                a + ASCIIColors.RESET.getColor()));
    }
}
