import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.service.PersonalDataService;
import es.physiotherapy.persistence.util.ASCIIColors;
import es.physiotherapy.persistence.util.HelperMethods;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final PersonalDataService PDS = new PersonalDataService();

    public static void main(String[] args) throws IOException {
        TreatedArea treatedArea = new TreatedArea();
        treatedArea.setCervical(true);
        treatedArea.setDorsal(true);
        treatedArea.setLumbar(true);
        treatedArea.setObservations("Observations about treated area");
        createTreatedAreaXml(treatedArea);
    }

    private static void createTreatedAreaXml(TreatedArea treatedArea) {
        for (Field field : TreatedArea.class.getDeclaredFields()) {
            Class<?> fieldClass = field.getType();
            if (fieldClass == Appointment.class) continue;
            String fieldName = field.getName();
            Object fieldValue = null;
            field.setAccessible(true);
            try {
                fieldValue = field.get(treatedArea);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error getting field value", e);
            }
            System.out.println(fieldName + ": " + fieldValue);
        }
    }

    private static <T> List<T> readJson(Class<T> entityClass, String filename) throws IOException {
        Path source = Paths.get("./input/" + filename + ".json").normalize();
        JSONArray jsonArray = new JSONArray(Files.readString(source));
        List<T> entities = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject(entityClass.getSimpleName().toLowerCase());
            T objectInBuild = null;
            try {
                objectInBuild = entityClass.getDeclaredConstructor().newInstance();
                entities.add(objectInBuild);
            } catch (InvocationTargetException | InstantiationException
                     | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException("Error creating object from JSON file", e);
            }
            for (Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    // TODO: terminar de implementar el casteo de los tipos de datos
                    Class<?> objectType = field.getType();
                    Object value = null;
                    switch (objectType.getSimpleName()) {
                        case "LocalDate" -> value = LocalDate.parse(jsonObject.getString(field.getName()));
                        case "Time" -> value = Time.valueOf(LocalTime.parse(jsonObject.getString(field.getName())));
                        case "Integer" -> value = jsonObject.getInt(field.getName());
                        case "Long" -> value = jsonObject.getLong(field.getName());
                        case "String" -> value = objectType.cast(jsonObject.get(field.getName()));
                    }
                    field.set(objectInBuild, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error setting field value " + field.getName(), e);
                } catch (JSONException e){
                    System.out.println(ASCIIColors.RED.getColor() + "Error reading field " + field.getName() + ASCIIColors.RESET.getColor());
                }
            }
        }
        return entities;
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
