package es.physiotherapy.persistence.entity;

import es.physiotherapy.persistence.util.ASCIIColors;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dni")
    private Client client;
    @OneToOne(mappedBy = TreatedArea_.APPOINTMENT,
            cascade = CascadeType.ALL)
    private TreatedArea treatedArea;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "appointment_use_tool",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "tool_id"))
    private List<Tool> tools = new ArrayList<>();

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

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
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
        return ASCIIColors.GREEN.getColor() +
                "Appointment\t\t" + ASCIIColors.PURPLE.getColor() + "Nº" + id + "\n" +
                ASCIIColors.BLUE.getColor() + "client" + "\t\tdate" + "\t\t\ttime" + "\t\t\tduration\n" +
                ASCIIColors.RESET.getColor() + (client != null ? client.getDni() : null) + "\t" + date + "\t\t" + time + "\t\t" + duration + "\n" +
                (treatedArea != null ? treatedArea + "\n" : "") + "-".repeat(50) + "\n";
    }
}
