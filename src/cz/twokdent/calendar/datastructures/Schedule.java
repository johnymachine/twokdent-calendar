package cz.twokdent.calendar.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Schedule {
	private HashMap<String, Doctor> doctors;
	private HashMap<String, Reservation> reservations;
	private HashMap<String, Patient> patients;
	private HashMap<String, Type> types;
	
	public Schedule(HashMap<String, Doctor> doctors, HashMap<String, Patient> patients, HashMap<String, Reservation> reservations, HashMap<String, Type> types) {
		super();
		Objects.requireNonNull(doctors, "Doctors must not be null.");
		Objects.requireNonNull(patients, "Patients must not be null.");
		Objects.requireNonNull(reservations, "Reservations must not be null.");
		Objects.requireNonNull(types, "Types must not be null.");
		this.doctors = doctors;
		this.patients = patients;
		this.reservations = reservations;
		this.types = types;
	}
	
	public HashMap<String, Doctor> getDoctors() {
		return doctors;
	}
	public HashMap<String, Reservation> getReservations() {
		return reservations;
	}
	public HashMap<String, Patient> getPatients() {
		return patients;
	}
	public HashMap<String, Type> getTypes() {
		return types;
	}

	@Override
	public String toString() {
		return "Schedule [doctors=" + doctors + ", reservations=" + reservations + ", patients=" + patients + ", types="
				+ types + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((doctors == null) ? 0 : doctors.hashCode());
		result = prime * result + ((patients == null) ? 0 : patients.hashCode());
		result = prime * result + ((reservations == null) ? 0 : reservations.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
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
		Schedule other = (Schedule) obj;
		if (doctors == null) {
			if (other.doctors != null)
				return false;
		} else if (!doctors.equals(other.doctors))
			return false;
		if (patients == null) {
			if (other.patients != null)
				return false;
		} else if (!patients.equals(other.patients))
			return false;
		if (reservations == null) {
			if (other.reservations != null)
				return false;
		} else if (!reservations.equals(other.reservations))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		return true;
	}
	public List<Reservation> getReservationsByDoctor(Doctor doctor, Date from, Date to) {
		return this.getReservationsByDoctor(doctor, from, to, false);
	}
	
	public List<Reservation> getReservationsByDoctor(Doctor doctor, Date from, Date to, Boolean showDeleted) {
		List<Reservation> list = new ArrayList<Reservation>(reservations.values());
		List<Reservation> result = new ArrayList<Reservation>();
		
		// Sort
		Collections.sort(list, new ReservationFromDateComparator());
		
		// Filter
		for (Reservation reservation: list) {
            if (reservation.getFrom().after(from) && reservation.getFrom().before(to) && reservation.getIsDeleted().equals(showDeleted) && reservation.getDoctor().getId().equals(doctor.getId())) {
                result.add(reservation);
            }
		}
		return result;
	}
}
