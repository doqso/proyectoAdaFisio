package es.physiotherapy.persistence.app;

import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.service.PersonalDataService;
import es.physiotherapy.persistence.util.ASCIIColors;
import es.physiotherapy.persistence.util.HelperMethods;
import es.physiotherapy.persistence.util.JSONReader;
import es.physiotherapy.persistence.util.XMLWritter;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final PersonalDataService PDS = new PersonalDataService();
    private static List<Object> objectsToPrint = null;

    public static void main(String[] args) {
        System.out.println("Hello Physio Appointment Management!");
        System.out.println("what do you want to do?");
        byte opt = 5;
        Scanner sc = new Scanner(System.in);
        menuOptions();
        do {
            System.out.println(ASCIIColors.YELLOW.getColor() + "5. Print menu again");
            System.out.println("0. Exit" + ASCIIColors.RESET.getColor() + "\n");
            System.out.print("Option -> ");
            try {
                opt = sc.nextByte();
                sc.nextLine();
                switch (opt) {
                    // Clients
                    case 11 -> createClient(sc);
                    case 12 -> getAllClients();
                    case 13 -> getClientByDni(sc);
                    case 14 -> getClientsByCity(sc);
                    case 15 -> getClientsAfterBirthDate(sc);
                    case 16 -> getClientsBeforeBirthDate(sc);
                    case 17 -> updateClient(sc);
                    case 18 -> deleteClient(sc);
                    // Appointments
                    case 21 -> createAppointment(sc);
                    case 22 -> getAllAppointments();
                    case 23 -> getAppointmentsByClient(sc);
                    case 24 -> getAppointmentsAfterDate(sc);
                    case 25 -> getAppointmentsBeforeDate(sc);
                    case 26 -> getAppointmentsBetweenDates(sc);
                    case 27 -> getAppointmentsByTreatedArea(sc);
                    case 28 -> updateAppointment(sc);
                    case 29 -> deleteAppointment(sc);
                    // Treated Areas
                    case 31 -> createTreatedArea(sc);
                    case 32 -> updateTreatedArea(sc);
                    case 33 -> deleteTreatedArea(sc);
                    // XML and JSON
                    case 41 -> writeXmlFile(sc);
                    case 42 -> readJsonFile(sc);
                    // Menu
                    case 5 -> menuOptions();
                    case 0 -> System.out.println("Bye!");
                    default -> System.out.println(ASCIIColors.RED.getColor()
                            + "Invalid option" + ASCIIColors.RESET.getColor() + "\n");
                }
            } catch (InputMismatchException e) {
                System.out.println(ASCIIColors.RED.getColor()
                        + "Invalid option" + ASCIIColors.RESET.getColor() + "\n");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(ASCIIColors.RED.getColor() +
                        "Error: " + e.getMessage() + "\n");
            }
        } while (opt != 0);
    }

    public static void readJsonFile(Scanner sc) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.out.println("-- Input path is: " + Paths.get(JSONReader.INPUT_DIR).toAbsolutePath() + " --");
        System.out.print("(1. Client, 2. Appointment) -> ");
        byte opt = sc.nextByte();
        sc.nextLine();
        System.out.print("Input file name -> ");
        String fileName = sc.nextLine();

        List<Object> objects = new ArrayList<>();
        switch (opt) {
            case 1 -> objects = Collections.singletonList(PDS.readJsonFile(fileName, Client.class));
            case 2 -> objects = Collections.singletonList(PDS.readJsonFile(fileName, Appointment.class));
            default -> System.out.println(ASCIIColors.RED.getColor()
                    + "Invalid option" + ASCIIColors.RESET.getColor() + "\n");
        }
        objects.forEach(System.out::println);
    }

    private static void writeXmlFile(Scanner cs) throws IOException {
        System.out.println("-- Output path is: " + Paths.get(XMLWritter.OUTPUT_DIR).toAbsolutePath() + " --");
        System.out.println("Write the filename");
        String fileName = cs.nextLine();
        PDS.writeXmlFile(objectsToPrint, fileName);
    }

    private static void getAppointmentsByTreatedArea(Scanner sc) throws NoSuchFieldException {
        System.out.println("Enter treated areas (separated by spaces): ");
        String[] description = sc.nextLine().split("\\s+");
        List<Appointment> appointments = PDS.getAppointmentsByTreatedArea(description);
        appointments.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(appointments.toArray());
    }

    private static void deleteTreatedArea(Scanner sc) {
        System.out.print("Treated Area ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = PDS.getAppointmentById(id);
        if (appointment == null) throw new RuntimeException("Treated area not found");
        TreatedArea treatedArea = new TreatedArea(appointment);
        PDS.deleteTreatedArea(treatedArea);
    }

    private static void updateTreatedArea(Scanner sc) {
        System.out.println("Enter appointment id: ");
        long id = sc.nextLong();
        sc.nextLine();
        TreatedArea oldTreatedArea = PDS.getTreatedAreaById(id);
        if (oldTreatedArea == null) throw new RuntimeException("Treated Area not found");
        System.out.println("Enter treated areas (separated by space): ");
        String[] treatedAreaStrings = sc.nextLine().split("\\s+");
        System.out.println("Enter observations (optional): ");
        String observations = sc.nextLine().trim();
        TreatedArea newTreatedArea = HelperMethods.treatedAreaBuilder(treatedAreaStrings, observations);
        newTreatedArea.setAppointment(oldTreatedArea.getAppointment());
        if (newTreatedArea.getObservations() == null) newTreatedArea.setObservations(oldTreatedArea.getObservations());
        PDS.updateTreatedArea(newTreatedArea);
        printGreenText("Treated Area updated successfully");
    }

    private static void createTreatedArea(Scanner sc) {
        System.out.println("Creating a new Treated Area");
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = PDS.getAppointmentById(id);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        System.out.print("Treated Areas (separated by space) -> ");
        String[] treatedAreaStrings = sc.nextLine().split("\\s+");
        System.out.print("Observations (optional) -> ");
        String observations = sc.nextLine().trim();
        TreatedArea treatedArea = HelperMethods.treatedAreaBuilder(treatedAreaStrings, observations);
        treatedArea.setAppointment(appointment);
        PDS.createTreatedArea(treatedArea);
        printGreenText("Treated Area created successfully");
    }

    private static void deleteAppointment(Scanner sc) {
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = PDS.getAppointmentById(id);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        PDS.deleteAppointment(appointment);
        printGreenText("Appointment deleted");
    }

    private static void updateAppointment(Scanner sc) {
        System.out.println("Enter the appointment id");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = PDS.getAppointmentById(id);
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
        PDS.updateAppointment(appointment);
        printGreenText("Appointment updated");
    }

    private static void deleteClient(Scanner sc) {
        System.out.print("DNI -> ");
        String dni = sc.nextLine();
        Client client = PDS.getClientByDni(dni);
        if (client == null) throw new RuntimeException("Client not found");
        PDS.deleteClient(client);
        printGreenText("Client deleted");
    }

    private static void updateClient(Scanner sc) {
        System.out.println("Enter the DNI of the client to update");
        String dni = sc.nextLine();
        Client client = PDS.getClientByDni(dni);
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
        PDS.updateClient(client);
        printGreenText("Client updated successfully");
    }

    private static void getClientsBeforeBirthDate(Scanner sc) {
        System.out.print("Birth date (dd/mm/yyyy) -> ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        List<Client> clients = PDS.getClientsBeforeBirthDate(date);
        clients.forEach(System.out::println);
        objectsToPrint = Arrays.asList(clients.toArray());
    }

    private static void getClientsAfterBirthDate(Scanner sc) {
        System.out.print("Birth date (dd/mm/yyyy) -> ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        List<Client> clients = PDS.getClientsAfterBirthDate(date);
        clients.forEach(System.out::println);
        objectsToPrint = Arrays.asList(clients.toArray());
    }

    private static void getClientsByCity(Scanner sc) {
        System.out.print("City -> ");
        String city = sc.nextLine();
        List<Client> clients = PDS.getClientsByCity(city);
        clients.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(clients.toArray());
    }

    private static void getClientByDni(Scanner sc) {
        System.out.print("DNI -> ");
        String dni = sc.nextLine();
        Client client = PDS.getClientByDni(dni);
        if (client == null) throw new RuntimeException("Client not found");
        printGreenText(client);
        objectsToPrint = List.of(client);
    }

    private static void getAppointmentsByClient(Scanner sc) {
        System.out.print("Client DNI -> ");
        String dni = sc.nextLine();
        List<Appointment> appointments = PDS.getAppointmentsByDni(dni);
        appointments.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(appointments.toArray());
    }

    private static void getAppointmentsBetweenDates(Scanner sc) {
        System.out.println("Enter the first date (yyyy-mm-dd): ");
        LocalDate date1 = HelperMethods.dateParser(sc.nextLine());
        System.out.println("Enter the second date (yyyy-mm-dd): ");
        LocalDate date2 = HelperMethods.dateParser(sc.nextLine());
        List<Appointment> appointments = PDS.getAppointmentsBetweenDates(date1, date2);
        appointments.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(appointments.toArray());
    }

    private static void getAppointmentsBeforeDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        String date = sc.nextLine();
        List<Appointment> appointments = PDS.getAppointmentsBeforeDate(HelperMethods.dateParser(date));
        appointments.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(appointments.toArray());
    }

    private static void getAppointmentsAfterDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        String date = sc.nextLine();
        List<Appointment> appointments = PDS.getAppointmentsAfterDate(HelperMethods.dateParser(date));
        appointments.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(appointments.toArray());
    }

    private static void getAllAppointments() {
        List<Appointment> appointments = PDS.getAllAppointments();
        appointments.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(appointments.toArray());
    }

    private static void getAllClients() {
        List<Client> clients = PDS.getAllClients();
        clients.forEach(Main::printGreenText);
        objectsToPrint = Arrays.asList(clients.toArray());
    }

    private static void createAppointment(Scanner sc) {
        Appointment appointment = new Appointment();
        System.out.print("Enter the client dni ");
        String dni = sc.nextLine();
        Client client = PDS.getClientByDni(dni);
        if (client == null) throw new RuntimeException("Client not found");
        appointment.setClient(client);
        System.out.println("Enter the date (yyyy-mm-dd) ");
        appointment.setDate(HelperMethods.dateParser(sc.nextLine()));
        System.out.println("Enter the time (hh:mm) ");
        appointment.setTime(HelperMethods.timeParser(sc.nextLine()));
        System.out.println("Enter the duration (in minutes) ");
        appointment.setDuration(sc.nextInt());
        sc.nextLine();
        System.out.println("Enter the areas to treat with (separated by space) ");
        String[] areas = sc.nextLine().split("\\s+");
        System.out.println("Enter any aditional notes (optional) ");
        String notes = sc.nextLine().trim();
        notes = notes.isBlank() ? null : notes;
        TreatedArea treatedArea = HelperMethods.treatedAreaBuilder(areas, notes);
        appointment.setTreatedArea(treatedArea);
        treatedArea.setAppointment(appointment);
        PDS.createAppointment(appointment);
        printGreenText("Appointment created successfully!");
    }

    private static void createClient(Scanner sc) {
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
        PDS.createClient(client);
        printGreenText("Client created successfully!");
    }

    private static void printGreenText(Object text) {
        System.out.println(ASCIIColors.GREEN.getColor() +
                text + ASCIIColors.RESET.getColor());
    }

    private static void menuOptions() {
        System.out.println();
        System.out.println(ASCIIColors.YELLOW.getColor() + "1 - Client Management\t\t\t\t\t" + "2 - Appointment Management");
        System.out.println(ASCIIColors.CYAN.getColor() + "11. Create a new client\t\t\t\t\t" + "21. Create a new appointment");
        System.out.println("12. Get all clients\t\t\t\t\t\t" + "22. Get all appointments");
        System.out.println("13. Get a client by DNI\t\t\t\t\t" + "23. Get appointments of a client");
        System.out.println("14. Get clients by city\t\t\t\t\t" + "24. Get appointments after a date");
        System.out.println("15. Get clients after birth date\t\t" + "25. Get appointments before a date");
        System.out.println("16. Get clients before birth date\t\t" + "26. Get appointments between two dates");
        System.out.println("17. Update a client\t\t\t\t\t\t" + "27. Get appointments by treated area");
        System.out.println("18. Delete a client\t\t\t\t\t\t" + "28. Update an appointment");
        System.out.println("\t".repeat(10) + "29. Delete an appointment\n");
        System.out.println(ASCIIColors.YELLOW.getColor() + "3 - Treatment area\t\t\t\t\t\t" + "4. I/O Management");
        System.out.println(ASCIIColors.CYAN.getColor() + "31. Create treatment areas \t\t\t\t" + "41. Write last 'GET' query to XML file");
        System.out.println("32. Update a treatment area \t\t\t" + "42. Read object from JSON file");
        System.out.println("33. Delete a treatment area" + ASCIIColors.RESET.getColor());
    }
}