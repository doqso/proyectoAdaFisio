package es.physiotherapy.persistence.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TreatedArea.class)
public abstract class TreatedArea_ {

	public static volatile SingularAttribute<TreatedArea, Boolean> lumbar;
	public static volatile SingularAttribute<TreatedArea, Boolean> dorsal;
	public static volatile SingularAttribute<TreatedArea, Boolean> elbow;
	public static volatile SingularAttribute<TreatedArea, Boolean> cervical;
	public static volatile SingularAttribute<TreatedArea, Appointment> appointment;
	public static volatile SingularAttribute<TreatedArea, Boolean> wrist;
	public static volatile SingularAttribute<TreatedArea, Boolean> hip;
	public static volatile SingularAttribute<TreatedArea, Boolean> knee;
	public static volatile SingularAttribute<TreatedArea, String> observations;
	public static volatile SingularAttribute<TreatedArea, Boolean> sacroiliac;
	public static volatile SingularAttribute<TreatedArea, Boolean> shoulder;
	public static volatile SingularAttribute<TreatedArea, Boolean> ankle;
	public static volatile SingularAttribute<TreatedArea, Boolean> foot;
	public static volatile SingularAttribute<TreatedArea, Boolean> hand;

	public static final String LUMBAR = "lumbar";
	public static final String DORSAL = "dorsal";
	public static final String ELBOW = "elbow";
	public static final String CERVICAL = "cervical";
	public static final String APPOINTMENT = "appointment";
	public static final String WRIST = "wrist";
	public static final String HIP = "hip";
	public static final String KNEE = "knee";
	public static final String OBSERVATIONS = "observations";
	public static final String SACROILIAC = "sacroiliac";
	public static final String SHOULDER = "shoulder";
	public static final String ANKLE = "ankle";
	public static final String FOOT = "foot";
	public static final String HAND = "hand";

}

