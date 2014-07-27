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
}
