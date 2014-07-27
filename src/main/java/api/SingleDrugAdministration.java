package api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
