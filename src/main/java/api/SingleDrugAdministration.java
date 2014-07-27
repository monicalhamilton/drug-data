package api;

/**
 * Holds a unique drug administration occurrence.
 * 
 * @author Monica
 */
public class SingleDrugAdministration {

	private final AdministrationInstance _administrationInstance;
	private final String _drug;

	/**
	 * Constructor
	 * 
	 * @param administrationInstance_
	 *            The administration instance (identifies who and when).
	 * @param drug_
	 *            The drug administered.
	 */
	public SingleDrugAdministration(
			final AdministrationInstance administrationInstance_,
			final String drug_) {
		_administrationInstance = administrationInstance_;
		_drug = drug_;
	}

	/**
	 * @return The administration instance.
	 */
	public AdministrationInstance getAdministrationInstance() {
		return _administrationInstance;
	}

	/**
	 * @return The drug administered.
	 */
	public String getDrug() {
		return _drug;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_administrationInstance == null) ? 0
						: _administrationInstance.hashCode());
		result = prime * result + ((_drug == null) ? 0 : _drug.hashCode());
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
		SingleDrugAdministration other = (SingleDrugAdministration) obj;
		if (_administrationInstance == null) {
			if (other._administrationInstance != null)
				return false;
		} else if (!_administrationInstance
				.equals(other._administrationInstance))
			return false;
		if (_drug == null) {
			if (other._drug != null)
				return false;
		} else if (!_drug.equals(other._drug))
			return false;
		return true;
	}

}
