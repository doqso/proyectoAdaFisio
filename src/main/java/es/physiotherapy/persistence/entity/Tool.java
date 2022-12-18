package es.physiotherapy.persistence.entity;

import es.physiotherapy.persistence.util.ASCIIColors;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tool")
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer stock;
    @ManyToMany(mappedBy = "tools")
    private List<Appointment> appointments = new ArrayList<>();

    public Tool() {
    }

    public Tool(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }


    @Override
    public String toString() {
        return ASCIIColors.GREEN.getColor()+
                "Tool" + "\t".repeat(2) + ASCIIColors.PURPLE.getColor() + "Nº" + id + "\n" +
                ASCIIColors.BLUE.getColor() + "Stock" + "\t".repeat(2) + "Name" + "\n" +
                ASCIIColors.RESET.getColor() + stock + "\t".repeat(3) + name + "\n" +
                "-".repeat(50) + "\n";
    }
}
