package cz.twokdent.calendar.datastructures;

import java.util.Date;
import java.util.Objects;

public class Reservation {
	private String id;
	private Patient patient;
	private Doctor doctor;
	private Type type;
	private String activity;
	private Date from;
	private Date to;
	private Boolean isDeleted;
	
	public Reservation(String id, Patient patient, Doctor doctor, Type type, String activity, Date from, Date to,
			Boolean isDeleted) {
		super();
		Objects.requireNonNull(id, "Id must not be null.");
		Objects.requireNonNull(patient, "Patient must not be null.");
		Objects.requireNonNull(doctor, "Doctor must not be null.");
		Objects.requireNonNull(from, "From must not be null.");
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.type = type;
		this.activity = activity;
		this.from = from;
		this.to = to;
		this.isDeleted = isDeleted;
	}

	public String getId() {
		return id;
	}

	public Patient getPatient() {
		return patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public Type getType() {
		return type;
	}

	public String getActivity() {
		return activity;
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + ((doctor == null) ? 0 : doctor.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((patient == null) ? 0 : patient.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (doctor == null) {
			if (other.doctor != null)
				return false;
		} else if (!doctor.equals(other.doctor))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDeleted == null) {
			if (other.isDeleted != null)
				return false;
		} else if (!isDeleted.equals(other.isDeleted))
			return false;
		if (patient == null) {
			if (other.patient != null)
				return false;
		} else if (!patient.equals(other.patient))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", patient=" + patient + ", doctor=" + doctor + ", type=" + type
				+ ", activity=" + activity + ", from=" + from + ", to=" + to + ", isDeleted=" + isDeleted + "]";
	}
}
