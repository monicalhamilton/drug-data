package api;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Holds a unique drug administration occurrence.
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

	public String getPatientIdentifier() {
		return _patientIdentifier;
	}

	public DateTime getAdministrationDate() {
		return _administrationDate;
	}

	protected DateTime parseDateString(final String administrationDateString_) {
		return FORMATTER.parseDateTime(administrationDateString_);
	}

}
