package es.physiotherapy.persistence.entity;

import es.physiotherapy.persistence.Helpers.ASCIIColors;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(nullable = false)
    private String dni;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String city;
    @Column(name = Client_.BIRTH_DATE, nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = Appointment_.CLIENT,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    public Client() {
    }

    public Client(String dni) {
        this.dni = dni;
    }

    public Client(String dni, String name, String surname, String phone, String address, String city, LocalDate birthDate) {
        setDni(dni);
        setName(name);
        setSurname(surname);
        setPhone(phone);
        setAddress(address);
        setCity(city);
        setBirthDate(birthDate);
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(dni, client.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return ASCIIColors.GREEN.getColor() +
                "Client\t\t\t\t" + ASCIIColors.PURPLE.getColor() + dni + "\n" +
                ASCIIColors.BLUE.getColor() + "name" + "\t\t\t\tsurname" + "\n" +
                ASCIIColors.RESET.getColor() + name + "\t\t\t\t" + surname + "\n" +
                ASCIIColors.BLUE.getColor() + "phone" + "\t\t\t\taddress" + "\n" +
                ASCIIColors.RESET.getColor() + phone + "\t\t\t" + address + "\n" +
                ASCIIColors.BLUE.getColor() + "birthDate" + "\t\t\tcity" + "\n" +
                ASCIIColors.RESET.getColor() + birthDate + "\t\t\t" + city + "\n" + "-".repeat(50);
    }
}
