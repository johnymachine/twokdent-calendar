package cz.twokdent.calendar.datastructures;

import java.util.Comparator;

public class ReservationFromDateComparator implements Comparator<Reservation> {

	@Override
	public int compare(Reservation r1, Reservation r2) {
		return r1.getFrom().compareTo(r2.getFrom());
	}
}