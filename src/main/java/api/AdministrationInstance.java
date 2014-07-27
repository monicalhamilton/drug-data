package api;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Holds an administration instance, as defined by a patient and date.
 * 
 * @author Monica
 */
public class AdministrationInstance {

	private static final DateTimeFormatter FORMATTER = DateTimeFormat
			.forPattern("yyyy-MM-dd");

	private final String _patientIdentifier;
	private final DateTime _administrationDate;

	/**
	 * Constructor for an administration instance.
	 * 
	 * @param patientIdentifier_
	 *            The String patient identifier.
	 * @param administrationDate_
	 *            The administration date.
	 */
	public AdministrationInstance(final String patientIdentifier_,
			final DateTime administrationDate_) {
		_patientIdentifier = patientIdentifier_;
		_administrationDate = administrationDate_;
	}

	/**
	 * Construction for an administration instance that takes the date as a
	 * string.
	 * 
	 * @param patientIdentifier_
	 *            The string patient identifier.
	 * @param administrationDateString_
	 *            The administration date as a yyyy-MM-dd string.
	 */
	public AdministrationInstance(String patientIdentifier_,
			String administrationDateString_) {
		_patientIdentifier = patientIdentifier_;
		_administrationDate = parseDateString(administrationDateString_);
	}

	/**
	 * @return The patient identifier.
	 */
	public String getPatientIdentifier() {
		return _patientIdentifier;
	}

	/**
	 * @return The administration date.
	 */
	public DateTime getAdministrationDate() {
		return _administrationDate;
	}

	/**
	 * Parse a date string of format yyyy-MM-dd to a Joda DateTime.
	 * 
	 * @param administrationDateString_
	 *            The date string when the administration occurred.
	 * @return The date time object.
	 */
	protected DateTime parseDateString(final String administrationDateString_) {
		return FORMATTER.parseDateTime(administrationDateString_);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_administrationDate == null) ? 0 : _administrationDate
						.hashCode());
		result = prime
				* result
				+ ((_patientIdentifier == null) ? 0 : _patientIdentifier
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj_) {
		if (this == obj_)
			return true;
		if (obj_ == null)
			return false;
		if (getClass() != obj_.getClass())
			return false;
		AdministrationInstance other = (AdministrationInstance) obj_;
		if (_administrationDate == null) {
			if (other._administrationDate != null)
				return false;
		} else if (!_administrationDate.equals(other._administrationDate))
			return false;
		if (_patientIdentifier == null) {
			if (other._patientIdentifier != null)
				return false;
		} else if (!_patientIdentifier.equals(other._patientIdentifier))
			return false;
		return true;
	}
	
}
