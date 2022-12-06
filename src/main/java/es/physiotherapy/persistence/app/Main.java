package es.physiotherapy.persistence.app;

import es.physiotherapy.persistence.Helpers.ASCIIColors;
import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
import es.physiotherapy.persistence.Helpers.HelperMethods;
import es.physiotherapy.persistence.service.PersonalDataService;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final PersonalDataService PDS = new PersonalDataService();

    public static void main(String[] args) {
        System.out.println("Hello Physio Appointment Management!");
        System.out.println("what do you want to do?");
        byte opt = 0;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("1. Client Management");
            System.out.println("\t11. Create a new client");
            System.out.println("\t12. Get all clients");
            System.out.println("\t13. Get a client by DNI");
            System.out.println("\t14. Update a client");
            System.out.println("\t15. Delete a client");
            System.out.println("2. Appointment Management");
            System.out.println("\t21. Create a new appointment");
            System.out.println("\t22. Get all appointments");
            System.out.println("\t23. Get all appointments of a client");
            System.out.println("\t24. Get all appointments after a date");
            System.out.println("\t25. Get all appointments before a date");
            System.out.println("\t26. Get all appointments between two dates");
            System.out.println("\t27. Get all appointments of a client after a date");
            System.out.println("\t28. Get all appointments of a client before a date");
            System.out.println("\t29. Get all appointments of a client between two dates");
            System.out.println("\t210. Update an appointment");
            System.out.println("\t211. Delete an appointment");
            System.out.println("0. Exit");
            System.out.print("Option -> ");
            opt = sc.nextByte();
            sc.nextLine();
            try {
                switch (opt) {
                    case 11 -> createClient(sc);
                    case 12 -> getAllClients();
                    case 21 -> createAppointment(sc);
                    case 22 -> getAllAppointments();
                    case 23 -> getAppointmentsByClient(sc);
                    case 24 -> getAllAppointmentsAfterDate(sc);
                    case 25 -> getAllAppointmentsBeforeDate(sc);
                    case 26 -> getAllAppointmentsBetweenDates(sc);
                    case 27 -> getAppointmentsByClientAfterDate(sc);
//                    case 28 -> getAppointmentsByClientBeforeDate(sc);
//                    case 29 -> getAppointmentsByClientBetweenDates(sc);
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage() + "\n");
            }
        } while (opt != 0);
    }

    private static void getAppointmentsByClientAfterDate(Scanner sc) {
        System.out.print("Client DNI -> ");
        String dni = sc.nextLine();
        System.out.print("Date -> ");
        String date = sc.nextLine();
    }

    private static void getAppointmentsByClient(Scanner sc) {
        System.out.print("Client DNI -> ");
        String dni = sc.nextLine();
        PDS.getAppointmentsByDni(dni)
                .forEach(a -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                a + ASCIIColors.RESET.getColor()));
    }

    private static void getAllAppointmentsBetweenDates(Scanner sc) {
        System.out.println("Enter the first date (yyyy-mm-dd): ");
        LocalDate date1 = LocalDate.parse(sc.nextLine());
        System.out.println("Enter the second date (yyyy-mm-dd): ");
        LocalDate date2 = LocalDate.parse(sc.nextLine());
        PDS.getAppointmentsBetweenDates(date1, date2)
                .forEach(a -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                a + ASCIIColors.RESET.getColor()));
    }

    private static void getAllAppointmentsBeforeDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        String date = sc.nextLine();
        PDS.getAppointmentsBeforeDate(LocalDate.parse(date))
                .forEach(a -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                a + ASCIIColors.RESET.getColor()));
    }

    private static void getAllAppointmentsAfterDate(Scanner sc) {
        System.out.println("Enter the date (yyyy-mm-dd): ");
        String date = sc.nextLine();
        PDS.getAppointmentsAfterDate(HelperMethods.dateParser(date))
                .forEach(a -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                a + ASCIIColors.RESET.getColor()));
    }

    private static void getAllAppointments() {
        PDS.getAllAppointments()
                .forEach(a -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                a + ASCIIColors.RESET.getColor()));
    }

    private static void getAllClients() {
        PDS.getAllClients()
                .forEach(c -> System.out.println(
                        ASCIIColors.GREEN.getColor() +
                                c + ASCIIColors.RESET.getColor()));
    }

    private static void createAppointment(Scanner sc) {
        Appointment appointment = new Appointment();
        System.out.print("Enter the client dni: ");
        String dni = sc.nextLine();
        Client client = PDS.getClientByDni(dni);
        appointment.setClient(client);
        System.out.println("Enter the date (yyyy-mm-dd): ");
        appointment.setDate(HelperMethods.dateParser(sc.nextLine()));
        System.out.println("Enter the time: ");
        appointment.setTime(HelperMethods.timeParser(sc.nextLine()));
        System.out.println("Enter the duration: ");
        appointment.setDuration(sc.nextInt());
        sc.nextLine();
        System.out.println("Enter the treated areas (separated by space): ");
        String[] areas = sc.nextLine().split("\s+");
        System.out.println("Enter any aditional observations (optional): ");
        String notes = sc.nextLine().trim();
        notes = notes.isBlank() ? null : notes;
        TreatedArea treatedArea = HelperMethods.treatedAreaBuilder(areas, notes);
        appointment.setTreatedArea(treatedArea);
        treatedArea.setAppointment(appointment);
        PDS.createAppointment(appointment);
        System.out.println("Appointment created successfully!");
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
        System.out.println("Client created successfully!");
    }
}