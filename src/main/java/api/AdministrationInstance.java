package api;

import java.util.Date;

/**
 * Holds a unique drug administration occurrence. 
 * 
 * @author Monica
 */
public class AdministrationInstance {

	protected final String _patientIdentifier;
	protected final Date _administrationDate;

	public AdministrationInstance(final String patientIdentifier_,
			final Date administrationDate_) {
		_patientIdentifier = patientIdentifier_;
		_administrationDate = administrationDate_;
	}

	public AdministrationInstance(String patientIdentifier_,
			String administrationDateString_) {
		_patientIdentifier = patientIdentifier_;
		_administrationDate = parseDateString(administrationDateString_);
	}

	protected Date parseDateString(final String administrationDateString_) {
		// TODO
		return null;
	}

}
