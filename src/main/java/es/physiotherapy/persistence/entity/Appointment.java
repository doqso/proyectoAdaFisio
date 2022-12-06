package es.physiotherapy.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Time time;
    private Integer duration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni")
    private Client client;
    @OneToOne(mappedBy = TreatedArea_.APPOINTMENT,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private TreatedArea treatedArea;

    public Appointment() {
    }

    public Appointment(Client client) {
        setClient(client);
    }

    public Appointment(TreatedArea treatedArea) {
        setTreatedArea(treatedArea);
    }

    public Appointment(Long id, LocalDate date, Time time, Integer duration, Client client) {
        setId(id);
        setDate(date);
        setTime(time);
        setDuration(duration);
        setClient(client);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TreatedArea getTreatedArea() {
        return treatedArea;
    }

    public void setTreatedArea(TreatedArea treatedArea) {
        this.treatedArea = treatedArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id) + 31;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", duration=" + duration +'}';
    }
}
