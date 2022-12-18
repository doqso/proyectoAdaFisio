package es.physiotherapy.persistence.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tool.class)
public abstract class Tool_ {

	public static volatile ListAttribute<Tool, Appointment> appointments;
	public static volatile SingularAttribute<Tool, String> name;
	public static volatile SingularAttribute<Tool, Long> id;
	public static volatile SingularAttribute<Tool, Integer> stock;

	public static final String APPOINTMENTS = "appointments";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String STOCK = "stock";

}

