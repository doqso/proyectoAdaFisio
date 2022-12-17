package es.physiotherapy.persistence.app;

import es.physiotherapy.persistence.service.PersonalDataService;
import es.physiotherapy.persistence.util.ASCIIColors;
import jakarta.persistence.PersistenceException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final PersonalDataService PDS = new PersonalDataService();

    public static void main(String[] args) {
        System.out.println("Hello Physio Appointment Management!");
        System.out.println("what do you want to do?");
        int opt = 5;
        Scanner sc = new Scanner(System.in);
        menuOptions();
        do {
            System.out.println(ASCIIColors.YELLOW.getColor() + "5. Print menu again");
            System.out.println("0. Exit" + ASCIIColors.RESET.getColor());
            System.out.print("Option -> ");
            try {
                opt = sc.nextInt();
                sc.nextLine();
                switch (opt) {
                    // 1. Clients
                    case 11 -> PDS.createClient(sc);
                    case 12 -> PDS.getAllClients();
                    case 13 -> PDS.getClientByDni(sc);
                    case 14 -> PDS.getClientsByCity(sc);
                    case 15 -> PDS.getClientsAfterBirthDate(sc);
                    case 16 -> PDS.getClientsBeforeBirthDate(sc);
                    case 17 -> PDS.updateClient(sc);
                    case 18 -> PDS.deleteClient(sc);
                    // 2. Appointments
                    case 21 -> PDS.createAppointment(sc);
                    case 22 -> PDS.getAllAppointments();
                    case 23 -> PDS.getAppointmentById(sc);
                    case 24 -> PDS.getAppointmentsByDni(sc);
                    case 25 -> PDS.getAppointmentsAfterDate(sc);
                    case 26 -> PDS.getAppointmentsBeforeDate(sc);
                    case 27 -> PDS.updateAppointment(sc);
                    case 28 -> PDS.deleteAppointment(sc);
                    // 3. Treated Areas
                    case 31 -> PDS.createTreatedArea(sc);
                    case 32 -> PDS.updateTreatedArea(sc);
                    case 33 -> PDS.deleteTreatedArea(sc);
                    // 4. I/O
                    case 41 -> PDS.writeXmlFile(sc);
                    case 42 -> PDS.readJsonFile(sc);
                    case 43 -> PDS.recoverLastDeletedObject();
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
            } catch (PersistenceException e) {
                System.out.println(ASCIIColors.RED.getColor()
                        + "Persistence error: Cannot apply changes to database" + ASCIIColors.RESET.getColor() + "\n");
            } catch (Exception e) {
                System.out.println(ASCIIColors.RED.getColor() +
                        "Error: " + e.getMessage() + "\n");
            }
        } while (opt != 0);
    }

    private static void menuOptions() {
        System.out.print(ASCIIColors.YELLOW.getColor());
        System.out.println("1 - Clients" + "\t".repeat(8) + "2 - Appointments");
        System.out.print(ASCIIColors.CYAN.getColor());
        System.out.println("11. Create a new client" + "\t".repeat(5) + "21. Create a new appointment");
        System.out.println("12. Get all clients" + "\t".repeat(6) + "22. Get all appointments");
        System.out.println("13. Get a client by DNI" + "\t".repeat(5) + "23. Get appointments by ID");
        System.out.println("14. Get clients by city" + "\t".repeat(5) + "24. Get appointments by client DNI");
        System.out.println("15. Get clients after birth date" + "\t".repeat(2) + "25. Get appointments after date");
        System.out.println("16. Get clients before birth date" + "\t".repeat(2) + "26. Get appointments before date");
        System.out.println("17. Update a client" + "\t12".repeat(6) + "27. Update an appointment");
        System.out.println("18. Delete a client" + "\t".repeat(6) + "28. Delete an appointment");
        System.out.print(ASCIIColors.YELLOW.getColor());
        System.out.println("3 - Treatment area" + "\t".repeat(6) + "4. I/O Management");
        System.out.print(ASCIIColors.CYAN.getColor());
        System.out.println("31. Create treatment areas" + "\t".repeat(4) + "41. Write last 'GET' query to XML file");
        System.out.println("32. Update a treatment area" + "\t".repeat(4) + "42. Read object from JSON file");
        System.out.println("33. Delete a treatment area" + "\t".repeat(4) + "43. Recover las deleted item" + ASCIIColors.RESET.getColor());
    }
}