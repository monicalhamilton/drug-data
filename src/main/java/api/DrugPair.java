package api;

import java.util.HashSet;
import java.util.Set;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_drugs == null) ? 0 : _drugs.hashCode());
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
		DrugPair other = (DrugPair) obj_;
		if (_drugs == null) {
			if (other._drugs != null)
				return false;
		} else if (!_drugs.equals(other._drugs))
			return false;
		return true;
	}
	
}
