package es.physiotherapy.persistence.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Client.class)
public abstract class Client_ {

	public static volatile ListAttribute<Client, Appointment> appointments;
	public static volatile SingularAttribute<Client, String> address;
	public static volatile SingularAttribute<Client, String> phone;
	public static volatile SingularAttribute<Client, String> city;
	public static volatile SingularAttribute<Client, String> surname;
	public static volatile SingularAttribute<Client, String> name;
	public static volatile SingularAttribute<Client, LocalDate> birthDate;
	public static volatile SingularAttribute<Client, String> dni;

	public static final String APPOINTMENTS = "appointments";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";
	public static final String CITY = "city";
	public static final String SURNAME = "surname";
	public static final String NAME = "name";
	public static final String BIRTH_DATE = "birthDate";
	public static final String DNI = "dni";

}

