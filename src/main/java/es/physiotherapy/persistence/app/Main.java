package es.physiotherapy.persistence.app;

import es.physiotherapy.persistence.Helpers.ASCIIColors;
import es.physiotherapy.persistence.Helpers.HelperMethods;
import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.service.PersonalDataService;

import java.sql.Time;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final PersonalDataService PDS = new PersonalDataService();

    public static void main(String[] args) {
        System.out.println("Hello Physio Appointment Management!");
        System.out.println("what do you want to do?");
        byte opt = 5;
        Scanner sc = new Scanner(System.in);
        menuOptions();
        do {
            System.out.println(ASCIIColors.BLUE.getColor() + "5. Print menu again");
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
                    case 24 -> getAllAppointmentsAfterDate(sc);
                    case 25 -> getAllAppointmentsBeforeDate(sc);
                    case 26 -> getAllAppointmentsBetweenDates(sc);
                    case 27 -> updateAppointment(sc);
                    case 28 -> deleteAppointment(sc);
                    // Treated Areas
                    case 31 -> createTreatedArea(sc);
                    case 32 -> updateTreatedArea(sc);
                    case 33 -> deleteTreatedArea(sc);
                    // Menu
                    case 5 -> menuOptions();
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
        TreatedArea newTreatedArea = new TreatedArea(oldTreatedArea.getAppointment());
        newTreatedArea.setObservations(oldTreatedArea.getObservations());
        System.out.println("Enter treated areas (separated by space): ");
        String[] treatedAreaStrings = sc.nextLine().split("\\s+");
        newTreatedArea.setTreatedAreas(List.of(treatedAreaStrings));
        System.out.println("Enter observations (optional): ");
        String observations = sc.nextLine().trim();
        if (!observations.isBlank())
            newTreatedArea.setObservations(observations);
        else newTreatedArea.setObservations(oldTreatedArea.getObservations());
        PDS.updateTreatedArea(newTreatedArea);
        printText("Treated Area updated successfully");
    }

    private static void createTreatedArea(Scanner sc) {
        System.out.println("Creating a new Treated Area");
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = PDS.getAppointmentById(id);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        TreatedArea treatedArea = new TreatedArea(appointment);
        System.out.print("Treated Areas (separated by space) -> ");
        String[] treatedAreaStrings = sc.nextLine().split("\\s+");
        treatedArea.setTreatedAreas(List.of(treatedAreaStrings));
        System.out.print("Observations (optional) -> ");
        String observations = sc.nextLine().trim();
        if (!observations.isBlank())
            treatedArea.setObservations(observations);
        PDS.createTreatedArea(treatedArea);
        printText("Treated Area created successfully");
    }

    private static void deleteAppointment(Scanner sc) {
        System.out.print("Appointment ID -> ");
        long id = sc.nextLong();
        sc.nextLine();
        Appointment appointment = PDS.getAppointmentById(id);
        if (appointment == null) throw new RuntimeException("Appointment not found");
        PDS.deleteAppointment(appointment);
        printText("Appointment deleted");
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
        printText("Appointment updated");
    }

    private static void deleteClient(Scanner sc) {
        System.out.print("DNI -> ");
        String dni = sc.nextLine();
        Client client = PDS.getClientByDni(dni);
        if (client == null) throw new RuntimeException("Client not found");
        PDS.deleteClient(client);
        printText("Client deleted");
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
        printText("Client updated successfully");
    }

    private static void getClientsBeforeBirthDate(Scanner sc) {
        System.out.print("Birth date (dd/mm/yyyy) -> ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        PDS.getClientsBeforeBirthDate(date).forEach(System.out::println);
    }

    private static void getClientsAfterBirthDate(Scanner sc) {
        System.out.print("Birth date (dd/mm/yyyy) -> ");
        LocalDate date = HelperMethods.dateParser(sc.nextLine());
        PDS.getClientsAfterBirthDate(date).forEach(System.out::println);
    }

    private static void getClientsByCity(Scanner sc) {
        System.out.print("City -> ");
        String city = sc.nextLine();
        PDS.getClientsByCity(city).forEach(Main::printText);
    }

    private static void getClientByDni(Scanner sc) {
        System.out.print("DNI -> ");
        String dni = sc.nextLine();
        Client client = PDS.getClientByDni(dni);
        if (client == null) throw new RuntimeException("Client not found");
        printText(client);
    }

    private static void getAppointmentsByClient(Scanner sc) {
        System.out.print("Client DNI -> ");
        String dni = sc.nextLine();
        PDS.getAppointmentsByDni(dni).forEach(Main::printText);
    }

    private static void getAllAppointmentsBetweenDates(Scanner sc) {
        System.out.println("Enter the first date (yyyy-mm-dd): ");
        LocalDate date1 = HelperMethods.dateParser(sc.nextLine());
        System.out.println("Enter the second date (yyyy-mm-dd): ");
        LocalDate date2 = HelperMethods.dateParser(sc.nextLine());
        PDS.getAppointmentsBetweenDates(date1, date2).forEach(Main::printText);
    }

    private static void getAllAppointmentsBeforeDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        String date = sc.nextLine();
        PDS.getAppointmentsBeforeDate(HelperMethods.dateParser(date)).forEach(Main::printText);
    }

    private static void getAllAppointmentsAfterDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        String date = sc.nextLine();
        PDS.getAppointmentsAfterDate(HelperMethods.dateParser(date)).forEach(Main::printText);
    }

    private static void getAllAppointments() {
        PDS.getAllAppointments().forEach(Main::printText);
    }

    private static void getAllClients() {
        PDS.getAllClients().forEach(Main::printText);
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
        printText("Appointment created successfully!");
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
        printText("Client created successfully!");
    }

    private static void printText(Object text) {
        System.out.println(ASCIIColors.GREEN.getColor() +
                text + ASCIIColors.RESET.getColor());
    }

    private static void menuOptions() {
        System.out.println();
        System.out.println(ASCIIColors.BLUE.getColor() + "1 - Client Management\t\t\t\t\t" + "2 - Appointment Management");
        System.out.println(ASCIIColors.CYAN.getColor() + "11. Create a new client\t\t\t\t\t" + "21. Create a new appointment");
        System.out.println("12. Get all clients\t\t\t\t\t\t" + "22. Get all appointments");
        System.out.println("13. Get a client by DNI\t\t\t\t\t" + "23. Get all appointments of a client");
        System.out.println("14. Get clients by city\t\t\t\t\t" + "24. Get all appointments after a date");
        System.out.println("15. Get clients after birth date\t\t" + "25. Get all appointments before a date");
        System.out.println("16. Get clients before birth date\t\t" + "26. Get all appointments between two dates");
        System.out.println("17. Update a client\t\t\t\t\t\t" + "27. Update an appointment");
        System.out.println("18. Delete a client\t\t\t\t\t\t" + "28. Delete an appointment");
        System.out.println(ASCIIColors.BLUE.getColor() + "3 - Treatment area");
        System.out.println(ASCIIColors.CYAN.getColor() + "31. Create treatment areas");
        System.out.println("32. Update a treatment area");
        System.out.println("33. Delete a treatment area" + ASCIIColors.RESET.getColor());
    }
}