package api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.LocalDate;
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
	private final LocalDate _administrationDate;

	/**
	 * Constructor for an administration instance.
	 * 
	 * @param patientIdentifier_
	 *            The String patient identifier.
	 * @param administrationDate_
	 *            The administration date.
	 */
	public AdministrationInstance(final String patientIdentifier_,
			final LocalDate administrationDate_) {
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
	public LocalDate getAdministrationDate() {
		return _administrationDate;
	}

	/**
	 * Parse a date string of format yyyy-MM-dd to a Joda DateTime.
	 * 
	 * @param administrationDateString_
	 *            The date string when the administration occurred.
	 * @return The date time object.
	 */
	protected LocalDate parseDateString(final String administrationDateString_) {
		return FORMATTER.parseLocalDate(administrationDateString_);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj_) {
		return EqualsBuilder.reflectionEquals(this, obj_);
	}

}
