package es.physiotherapy.persistence.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "treated_area")
public class TreatedArea {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    private boolean cervical;
    private boolean dorsal;
    private boolean lumbar;
    private boolean sacroiliac;
    private boolean shoulder;
    private boolean elbow;
    private boolean wrist;
    private boolean hand;
    private boolean hip;
    private boolean knee;
    private boolean ankle;
    private boolean foot;
    private String observations;

    public TreatedArea() {
    }

    public TreatedArea(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public boolean isCervical() {
        return cervical;
    }

    public void setCervical(boolean cervical) {
        this.cervical = cervical;
    }

    public boolean isDorsal() {
        return dorsal;
    }

    public void setDorsal(boolean dorsal) {
        this.dorsal = dorsal;
    }

    public boolean isLumbar() {
        return lumbar;
    }

    public void setLumbar(boolean lumbar) {
        this.lumbar = lumbar;
    }

    public boolean isSacroiliac() {
        return sacroiliac;
    }

    public void setSacroiliac(boolean sacroiliac) {
        this.sacroiliac = sacroiliac;
    }

    public boolean isShoulder() {
        return shoulder;
    }

    public void setShoulder(boolean shoulder) {
        this.shoulder = shoulder;
    }

    public boolean isElbow() {
        return elbow;
    }

    public void setElbow(boolean elbow) {
        this.elbow = elbow;
    }

    public boolean isWrist() {
        return wrist;
    }

    public void setWrist(boolean wrist) {
        this.wrist = wrist;
    }

    public boolean isHand() {
        return hand;
    }

    public void setHand(boolean hand) {
        this.hand = hand;
    }

    public boolean isHip() {
        return hip;
    }

    public void setHip(boolean hip) {
        this.hip = hip;
    }

    public boolean isKnee() {
        return knee;
    }

    public void setKnee(boolean knee) {
        this.knee = knee;
    }

    public boolean isAnkle() {
        return ankle;
    }

    public void setAnkle(boolean ankle) {
        this.ankle = ankle;
    }

    public boolean isFoot() {
        return foot;
    }

    public void setFoot(boolean foot) {
        this.foot = foot;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreatedArea that = (TreatedArea) o;
        return (appointment != null && that.appointment != null) && appointment.getId().equals(that.appointment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointment.getId()) + 31;
    }

    @Override
    public String toString() {
        return "TreatedArea{" +
                "cervical=" + cervical +
                ", dorsal=" + dorsal +
                ", lumbar=" + lumbar +
                ", sacroiliac=" + sacroiliac +
                ", shoulder=" + shoulder +
                ", elbow=" + elbow +
                ", wrist=" + wrist +
                ", hand=" + hand +
                ", hip=" + hip +
                ", knee=" + knee +
                ", ankle=" + ankle +
                ", foot=" + foot +
                ", observations='" + observations + '}';
    }
}
