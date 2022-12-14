package es.physiotherapy.persistence.util;

import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    public static final String INPUT_DIR = "input/";

    public static List<Appointment> getAppointmentsFromJsonFile(String filename) throws NoSuchFieldException, IllegalAccessException {
        String jsonString = null;
        try {
            jsonString = Files.readString(Paths.get(INPUT_DIR + filename));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file " + filename, e);
        }
        JSONArray jsonObject = new JSONObject(jsonString).getJSONArray("appointments");
        List<Appointment> appointments = new ArrayList<>();
        for (int i = 0; i < jsonObject.length(); i++) {
            JSONObject appointmentJson = jsonObject.getJSONObject(i);
            Appointment appointment = new Appointment();
            appointment.setClient(new Client(appointmentJson.getString("client_dni")));
            appointment.setDate(HelperMethods.dateParser(appointmentJson.getString("date")));
            appointment.setTime(HelperMethods.timeParser(appointmentJson.getString("time")));
            appointment.setDuration(appointmentJson.getInt("duration"));
            JSONObject treatedAreaJson = appointmentJson.getJSONObject("treatedArea");
            TreatedArea treatedArea = new TreatedArea();
            treatedArea.setAppointment(appointment);
            JSONArray areas = treatedAreaJson.getJSONArray("areas");
            for (int j = 0; j < areas.length(); j++) {
                Field field = TreatedArea.class.getDeclaredField(areas.getString(j));
                field.setAccessible(true);
                field.setBoolean(treatedArea, true);
            }
            treatedArea.setObservations(treatedAreaJson.getString("observations"));
            appointment.setTreatedArea(treatedArea);
            appointments.add(appointment);
        }
        return appointments;
    }

    public static List<Client> getClientsFromJsonFile(String filename) throws IllegalAccessException {
        String jsonString = null;
        try {
            jsonString = Files.readString(Paths.get(INPUT_DIR + filename));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file " + filename, e);
        }
        JSONArray jsonObject = new JSONObject(jsonString).getJSONArray("clients");
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < jsonObject.length(); i++) {
            JSONObject clientJson = jsonObject.getJSONObject(i);
            Client client = new Client();
            for (Field field : Client.class.getDeclaredFields()) {
                field.setAccessible(true);
                switch (field.getType().getSimpleName()) {
                    case "String" -> field.set(client, clientJson.getString(field.getName()));
                    case "LocalDate" -> field.set(client,
                            HelperMethods.dateParser(clientJson.getString(field.getName())));
                }
            }
            clients.add(client);
        }
        return clients;
    }
}
