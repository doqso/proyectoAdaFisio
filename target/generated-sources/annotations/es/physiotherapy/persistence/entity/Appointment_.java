package es.physiotherapy.persistence.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Time;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Appointment.class)
public abstract class Appointment_ {

	public static volatile SingularAttribute<Appointment, LocalDate> date;
	public static volatile SingularAttribute<Appointment, Integer> duration;
	public static volatile SingularAttribute<Appointment, TreatedArea> treatedArea;
	public static volatile SingularAttribute<Appointment, Client> client;
	public static volatile SingularAttribute<Appointment, Long> id;
	public static volatile SingularAttribute<Appointment, Time> time;
	public static volatile ListAttribute<Appointment, Tool> tools;

	public static final String DATE = "date";
	public static final String DURATION = "duration";
	public static final String TREATED_AREA = "treatedArea";
	public static final String CLIENT = "client";
	public static final String ID = "id";
	public static final String TIME = "time";
	public static final String TOOLS = "tools";

}

