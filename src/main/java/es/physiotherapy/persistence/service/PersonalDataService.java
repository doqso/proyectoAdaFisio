package es.physiotherapy.persistence.service;

import es.physiotherapy.persistence.dao.appointment.AppointmentDAO;
import es.physiotherapy.persistence.dao.appointment.AppointmentDAOImpl;
import es.physiotherapy.persistence.dao.client.ClientDAO;
import es.physiotherapy.persistence.dao.client.ClientDAOImpl;
import es.physiotherapy.persistence.dao.tool.ToolDAO;
import es.physiotherapy.persistence.dao.tool.ToolDAOImpl;
import es.physiotherapy.persistence.dao.treatedarea.TreatedAreaDAO;
import es.physiotherapy.persistence.dao.treatedarea.TreatedAreaDAOImpl;
import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.Tool;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.util.ASCIIColors;
import es.physiotherapy.persistence.util.HelperMethods;
import es.physiotherapy.persistence.util.JSONReader;
import es.physiotherapy.persistence.util.XMLWritter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonalDataService {
    private final ClientDAO clientDAO;
    private final AppointmentDAO appointmentDAO;
    private final TreatedAreaDAO treatedAreaDAO;
    private final ToolDAO toolDAO;
    List<Object> objectsToPrint = null;
    private Object lastDeletedObject = null;

    public PersonalDataService() {
        this.clientDAO = new ClientDAOImpl();
        this.appointmentDAO = new AppointmentDAOImpl();
        this.treatedAreaDAO = new TreatedAreaDAOImpl();
        this.toolDAO = new ToolDAOImpl();
    }

    public void getAllTools() {
        objectsToPrint = new ArrayList<>(toolDAO.findAll());
        objectsToPrint.forEach(System.out::println);
    }

    public void getToolById(Scanner sc) {
        System.out.print("ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Tool tool = toolDAO.findById(id).orElse(null);
        if (tool == null) throw new IllegalArgumentException("There is no tool with that id");
        System.out.println(tool);
        objectsToPrint = List.of(tool);
    }

    public void getToolsByAppointmentId(Scanner sc) {
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        objectsToPrint = List.copyOf(toolDAO.findByAppointment(id));
        if (objectsToPrint.isEmpty()) System.out.println("No tools found for appointment with id " + id);
        objectsToPrint.forEach(System.out::println);
    }

    public void createTool(Scanner sc) {
        System.out.print("Name -> ");
        String name = sc.nextLine();
        System.out.print("stock -> ");
        int stock = sc.nextInt();
        sc.nextLine();
        Tool tool = toolDAO.create(new Tool(name, stock));
        System.out.println("Tool created with id " + tool.getId());
    }

    public void deleteTool(Scanner sc) {
        System.out.println("Introduce the id of the tool you want to delete");
        long id = sc.nextLong();
        sc.nextLine();
        Tool tool = toolDAO.findById(id).orElse(null);
        if (tool == null) throw new IllegalArgumentException("No tool with id " + id + " found");
        toolDAO.delete(tool);
        System.out.println("Tool with id " + id + " deleted");
    }

    public void createClient(Scanner sc) {
        Client client = new Client();
        System.out.print("Enter the client's dni: ");
        client.setDni(sc.nextLine());
        System.out.print("Enter the client's name: ");
        client.setName(sc.nextLine());
        System.out.print("Enter the client's surname: ");
        client.setSurname(sc.nextLine());
        System.out.print("Enter the client's phone: ");
        client.setPhone(sc.nextLine());
        System.out.print("Enter the client's address: ");
        client.setAddress(sc.nextLine());
        System.out.print("Enter the client's city: ");
        client.setCity(sc.nextLine());
        System.out.print("Enter the client's bitrh date (yyyy-mm-dd): ");
        client.setBirthDate(HelperMethods.dateParser(sc.nextLine()));
        clientDAO.create(client);
        printGreenText("Client created successfully!");
    }

    public void updateClient(Scanner sc) {
        System.out.println("Enter the client's dni: ");
        String dni = sc.nextLine();
        Client client = clientDAO.findById(dni).orElse(null);
        if (client == null) throw new RuntimeException("Client not found");
        System.out.println("Enter the new name");
        String name = sc.nextLine();
        System.out.println("Enter the new surname");
        String surname = sc.nextLine();
        System.out.println("Enter the new birth date");
        LocalDate birthDate = HelperMethods.dateParser(sc.nextLine());
        System.out.println("Enter the new city");
        String city = sc.nextLine();
        System.out.println("Enter the new address");
        String address = sc.nextLine();
        System.out.println("Enter the new phone number");
        String phone = sc.nextLine();
        client.setName(name);
        client.setSurname(surname);
        client.setBirthDate(birthDate);
        client.setCity(city);
        client.setAddress(address);
        client.setPhone(phone);
        clientDAO.save(client);
        printGreenText("Client updated successfully");
    }

    public void deleteClient(Scanner sc) {
        System.out.print("DNI -> ");
        String dni = sc.nextLine();
        Client client = clientDAO.findById(dni).orElse(null);
        if (client == null) throw new RuntimeException("Client not found");
        clientDAO.delete(client);
        printGreenText("Client deleted");
        lastDeletedObject = client;
    }

    public void updateAppointment(Scanner sc) {
        System.out.println("Enter the appointment id");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = appointmentDAO.findById(id).orElse(null);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        System.out.println("Enter the new date (dd/mm/yyyy)");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        System.out.println("Enter the new time (hh:mm)");
        Time time = HelperMethods.timeParser(sc.nextLine());
        System.out.println("Enter the new duration (in minutes)");
        int duration = sc.nextInt();
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setDuration(duration);
        appointmentDAO.save(appointment);
        printGreenText("Appointment updated");
    }

    public void deleteAppointment(Scanner sc) {
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = appointmentDAO.findById(id).orElse(null);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        appointmentDAO.delete(appointment);
        printGreenText("Appointment deleted");
        lastDeletedObject = appointment;
    }

    public void createTreatedArea(Scanner sc) {
        System.out.println("Creating a new Treated Area");
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = appointmentDAO.findById(id).orElse(null);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        System.out.print("Treated Areas (separated by space) -> ");
        String[] treatedAreaStrings = sc.nextLine().split("\\s+");
        System.out.print("Observations (optional) -> ");
        String observations = sc.nextLine().trim();
        TreatedArea treatedArea = HelperMethods.treatedAreaBuilder(treatedAreaStrings, observations);
        treatedArea.setAppointment(appointment);
        treatedAreaDAO.create(treatedArea);
        printGreenText("Treated Area created successfully");
    }

    public void updateTreatedArea(Scanner sc) {
        System.out.println("Enter appointment id: ");
        long id = sc.nextLong();
        sc.nextLine();
        TreatedArea oldTreatedArea = treatedAreaDAO.findById(id).orElse(null);
        if (oldTreatedArea == null) throw new RuntimeException("Treated Area not found");
        System.out.println("Enter treated areas (separated by space): ");
        String[] treatedAreaStrings = sc.nextLine().split("\\s+");
        System.out.println("Enter observations (optional): ");
        String observations = sc.nextLine().trim();
        TreatedArea newTreatedArea = HelperMethods.treatedAreaBuilder(treatedAreaStrings, observations);
        newTreatedArea.setAppointment(oldTreatedArea.getAppointment());
        if (newTreatedArea.getObservations() == null) newTreatedArea.setObservations(oldTreatedArea.getObservations());
        treatedAreaDAO.save(newTreatedArea);
        printGreenText("Treated Area updated successfully");
    }

    public void deleteTreatedArea(Scanner sc) {
        System.out.print("Treated Area ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        TreatedArea treatedArea = treatedAreaDAO.findById(id).orElse(null);
        if (treatedArea == null) throw new RuntimeException("Treated area not found");
        treatedAreaDAO.delete(treatedArea);
        printGreenText("Treated area deleted");
        lastDeletedObject = treatedArea;
    }

    public void createAppointment(Scanner sc) {
        Appointment appointment = new Appointment();
        System.out.print("Enter the client dni ");
        String dni = sc.nextLine();
        Client client = clientDAO.findById(dni).orElse(null);
        if (client == null) throw new RuntimeException("Client not found");
        appointment.setClient(client);
        System.out.println("Enter the date (yyyy-mm-dd) ");
        appointment.setDate(HelperMethods.dateParser(sc.nextLine()));
        System.out.println("Enter the time (hh:mm) ");
        appointment.setTime(HelperMethods.timeParser(sc.nextLine()));
        System.out.println("Enter the duration (in minutes) ");
        appointment.setDuration(sc.nextInt());
        sc.nextLine();
        System.out.println("Enter the areas to treat with (separated by space ex: cervical hand) ");
        String[] areas = sc.nextLine().split("\\s+");
        System.out.println("Enter any aditional notes (optional) ");
        String notes = sc.nextLine().trim();
        notes = notes.isBlank() ? null : notes;
        TreatedArea treatedArea = HelperMethods.treatedAreaBuilder(areas, notes);
        appointment.setTreatedArea(treatedArea);
        treatedArea.setAppointment(appointment);
        appointmentDAO.create(appointment);
        printGreenText("Appointment created successfully!");
    }

    public void getClientsByCity(Scanner sc) {
        System.out.print("City -> ");
        String city = sc.nextLine();
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City cannot be null");
        objectsToPrint = List.copyOf(clientDAO.findByCity(city));
        objectsToPrint.forEach(System.out::println);
    }

    public void getClientsAfterBirthDate(Scanner sc) {
        System.out.print("Birth date (dd/mm/yyyy) -> ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        objectsToPrint = List.copyOf(clientDAO.findByBirthDateBetweenDates(date, LocalDate.now()));
        objectsToPrint.forEach(System.out::println);
    }

    public void getClientsBeforeBirthDate(Scanner sc) {
        System.out.print("Birth date (dd/mm/yyyy) -> ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        objectsToPrint = List.copyOf(clientDAO.findByBirthDateBetweenDates(LocalDate.MIN, date));
        objectsToPrint.forEach(System.out::println);
    }

    public void getAllClients() {
        objectsToPrint = List.copyOf(clientDAO.findAll());
        objectsToPrint.forEach(System.out::println);
    }

    public void getClientByDni(Scanner sc) {
        System.out.print("DNI -> ");
        String dni = sc.nextLine();
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("Dni cannot be null");
        Client client = clientDAO.findById(dni).orElse(null);
        if (client == null) throw new RuntimeException("Client not found");
        printGreenText(client);
        objectsToPrint = List.of(client);
    }

    public void getAllAppointments() {
        objectsToPrint = List.copyOf(appointmentDAO.findAll());
        objectsToPrint.forEach(System.out::println);
    }

    public void getAppointmentById(Scanner sc) {
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        if (id < 0) throw new IllegalArgumentException("Id cannot be negative");
        Appointment appointment = appointmentDAO.findById(id).orElse(null);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        System.out.println(appointment);
        objectsToPrint = List.of(appointment);
    }

    public void getAppointmentsByDni(Scanner sc) {
        System.out.print("Client DNI -> ");
        String dni = sc.nextLine();
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("Dni cannot be null");
        objectsToPrint = List.copyOf(appointmentDAO.findByDni(dni));
        objectsToPrint.forEach(System.out::println);
    }

    public void getAppointmentsAfterDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        objectsToPrint = List.copyOf(appointmentDAO.findBetweenDate(date, LocalDate.now()));
        objectsToPrint.forEach(System.out::println);
    }

    public void getAppointmentsBeforeDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        if (date == null) throw new IllegalArgumentException("Date cannot be null");
        objectsToPrint = List.copyOf(appointmentDAO.findBetweenDate(LocalDate.EPOCH, date));
        objectsToPrint.forEach(System.out::println);
    }

    public void writeXmlFile(Scanner sc) throws IOException {
        if (!Files.exists(XMLWritter.getOutputDir())) Files.createDirectories(XMLWritter.getOutputDir());
        System.out.println(ASCIIColors.BLUE.getColor() + "Writing in: " +
                XMLWritter.getOutputDir().toAbsolutePath() + ASCIIColors.RESET.getColor());
        if (objectsToPrint == null || objectsToPrint.isEmpty()) throw new RuntimeException("No items to be written");
        System.out.println("File name -> ");
        String fileName = sc.nextLine().trim();
        if (fileName.isBlank()) throw new IllegalArgumentException("File name is empty");
        if (!fileName.endsWith(".xml")) fileName += ".xml";
        switch (objectsToPrint.get(0).getClass().getSimpleName()) {
            case "Client" -> XMLWritter.createClientXmlFile(objectsToPrint.toArray(), fileName);
            case "Appointment" -> XMLWritter.createAppointmentXmlFile(objectsToPrint.toArray(), fileName);
            case "Tool" -> XMLWritter.createToolXmlFile(objectsToPrint.toArray(), fileName);
        }
        printGreenText("File created successfully!");
    }

    public void readJsonFile(Scanner sc) throws IllegalAccessException, NoSuchFieldException, IOException {
        if (!Files.exists(JSONReader.getInputDir())) Files.createDirectories(JSONReader.getInputDir());
        System.out.println(ASCIIColors.BLUE.getColor() + "Reading from: " +
                JSONReader.getInputDir().toAbsolutePath() + ASCIIColors.RESET.getColor());
        System.out.println("(1. Client, 2. Appointment, 3. Tool)");
        System.out.print("Option -> ");
        byte opt = sc.nextByte();
        sc.nextLine();
        if (opt < 1 || opt > 3) throw new IllegalArgumentException("Invalid option");
        System.out.print("File name -> ");
        String fileName = sc.nextLine().trim();

        if (fileName.isBlank())
            throw new IllegalArgumentException("File name is null or empty");
        if (!fileName.endsWith(".json")) fileName += ".json";
        List<Object> list = new ArrayList<>();
        switch (opt) {
            case 1 -> list.addAll(JSONReader.getClientsFromJsonFile(fileName));
            case 2 -> list.addAll(JSONReader.getAppointmentsFromJsonFile(fileName));
            case 3 -> list.addAll(JSONReader.getToolsFromJsonFile(fileName));
        }
        list.forEach(System.out::println);
        objectsToPrint = list;

        System.out.println("Add to database? (y/n)");
        if (!sc.nextLine().trim().equalsIgnoreCase("y")) return;
        switch (opt) {
            case 1 -> clientDAO.create((List<Client>) (List<?>) list);
            case 2 -> appointmentDAO.create((List<Appointment>) (List<?>) list);
            case 3 -> toolDAO.create((List<Tool>) (List<?>) list);
            default -> throw new RuntimeException("Objects could not be added to database");
        }
        printGreenText("Objects added to database");
    }

    public void recoverLastDeletedObject() {
        if (lastDeletedObject == null) throw new RuntimeException("No item to recover");
        switch (lastDeletedObject.getClass().getSimpleName()) {
            case "Client" -> {
                Client client = (Client) lastDeletedObject;
                client.getAppointments().forEach(appointment -> appointment.setId(null));
                clientDAO.create(client);
            }
            case "Appointment" -> {
                Appointment appointment = (Appointment) lastDeletedObject;
                appointment.setId(null);
                appointmentDAO.create(appointment);
            }
            case "TreatedArea" -> treatedAreaDAO.create((TreatedArea) lastDeletedObject);
        }
        printGreenText("Object recovered successfully");
        lastDeletedObject = null;
    }

    public void setOutputDirectoryForXml(Scanner sc) throws IOException {
        System.out.println(ASCIIColors.BLUE.getColor() +
                "Current output directory: " + XMLWritter.getOutputDir()
                .toAbsolutePath() + ASCIIColors.RESET.getColor());
        System.out.println("Do you want to change it? (y/n)");
        if (!sc.nextLine().trim().equalsIgnoreCase("y")) return;
        System.out.print("new output directory -> ");
        String newDir = sc.nextLine();
        if (newDir.isBlank()) throw new IllegalArgumentException("Directory cannot be empty");
        XMLWritter.setOutputDir(newDir);
        if (!Files.exists(XMLWritter.getOutputDir())) {
            System.out.println("Directory does not exist, creating...");
            Files.createDirectories(XMLWritter.getOutputDir());
        }
        printGreenText("Output directory changed successfully");
    }

    public void setInputDirectoryForJson(Scanner sc) throws IOException {
        System.out.println(ASCIIColors.BLUE.getColor() +
                "Current input directory: " + JSONReader.getInputDir()
                .toAbsolutePath() + ASCIIColors.RESET.getColor());
        System.out.println("Do you want to change it? (y/n)");
        if (!sc.nextLine().trim().equalsIgnoreCase("y")) return;
        System.out.print("new input directory -> ");
        String newDir = sc.nextLine();
        if (newDir.isBlank()) throw new IllegalArgumentException("Directory cannot be empty");
        JSONReader.setInputDir(newDir);
        if (!Files.exists(JSONReader.getInputDir())) {
            System.out.println("Directory does not exist, creating...");
            Files.createDirectories(JSONReader.getInputDir());
        }
        printGreenText("Input directory changed successfully");
    }

    private static void printGreenText(Object text) {
        System.out.println(ASCIIColors.GREEN.getColor() +
                text + ASCIIColors.RESET.getColor());
    }


}
