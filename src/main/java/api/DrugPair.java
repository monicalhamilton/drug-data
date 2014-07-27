package api;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Wraps a pair of drugs for convenience. Does checks that the two drugs differ
 * from each other.
 * 
 * @author Monica
 */
public class DrugPair {

	private final Set<String> _drugs;

	/**
	 * Constructor
	 * 
	 * @param drugA_
	 *            A drug in the pair
	 * @param drugB_
	 *            Another drug in the pair
	 */
	public DrugPair(final String drugA_, final String drugB_) {
		_drugs = new HashSet<String>();
		_drugs.add(drugA_);
		_drugs.add(drugB_);
	}

	public Set<String> getDrugs() {
		return _drugs;
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
