package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityOffer extends ActivityOffer_Base {

	private final int amount;

	public ActivityOffer(Activity activity, LocalDate begin, LocalDate end, int amount) {
		checkArguments(activity, begin, end, amount);

		setBegin(begin);
		setEnd(end);
		setCapacity(activity.getCapacity());

		this.amount = amount;

		setActivity(activity);
	}

	public void delete() {
		setActivity(null);

		for (Booking booking : getBookingSet()) {
			booking.delete();
		}

		deleteDomainObject();
	}

	private void checkArguments(Activity activity, LocalDate begin, LocalDate end, int amount) {
		if (activity == null || begin == null || end == null) {
			throw new ActivityException();
		}

		if (end.isBefore(begin)) {
			throw new ActivityException();
		}

		if (amount < 1) {
			throw new ActivityException();
		}
	}

	int getNumberActiveOfBookings() {
		int count = 0;
		for (Booking booking : getBookingSet()) {
			if (!booking.isCancelled()) {
				count++;
			}
		}
		return count;
	}

	public int getPrice() {
		return this.amount;
	}

	boolean available(LocalDate begin, LocalDate end) {
		return hasVacancy() && matchDate(begin, end);
	}

	boolean matchDate(LocalDate begin, LocalDate end) {
		if (begin == null || end == null) {
			throw new ActivityException();
		}

		return begin.equals(getBegin()) && end.equals(getEnd());
	}

	boolean hasVacancy() {
		return getCapacity() > getNumberActiveOfBookings();
	}

	public Booking getBooking(String reference) {
		for (Booking booking : getBookingSet()) {
			if (booking.getReference().equals(reference)
					|| booking.isCancelled() && booking.getCancel().equals(reference)) {
				return booking;
			}
		}
		return null;
	}

}
