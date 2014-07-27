package api;


/**
 * Holds a unique drug administration occurrence. 
 * 
 * @author Monica
 */
public class SingleDrugAdministration {

	private final AdministrationInstance _administrationIdentifier;
	private final String _drug;

	public SingleDrugAdministration(final AdministrationInstance administrationIdentifier_,
			final String drug_) {
		_administrationIdentifier = administrationIdentifier_;
		_drug = drug_;
	}

	public AdministrationInstance getAdministrationInstance() {
		return _administrationIdentifier;
	}

	public String getDrug() {
		return _drug;
	}
	

}
