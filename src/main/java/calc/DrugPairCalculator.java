package calc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import api.AdministrationInstance;
import api.DrugPair;
import api.SingleDrugAdministration;

/**
 * Utility class for calculating various drug pair calculations.
 */
public class DrugPairCalculator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DrugPairCalculator.class);
	/**
	 * Get all drug pairs administered together with a given minimum occurrence
	 * from a list of deserialized drug administration objects.
	 * 
	 * @param drugAdministrations_
	 *            A list of all drug administrations as a single drug with
	 *            administration information.
	 * @param minOccurrence_
	 *            The minimum number of times a pair of drugs must be
	 *            administered together in order to be returned.
	 * @return A set of all drug pairs administered together with a given
	 *         minimum occurrence.
	 */
	public static Set<DrugPair> getDrugPairsWithMinOccurence(
			final List<SingleDrugAdministration> drugAdministrations_,
			final int minOccurrence_) {
		LOGGER.info(
				"Going to calculate the drug pairs with minimum occurrence {} "
						+ "on a list of drug administrations of size {}.",
				minOccurrence_, drugAdministrations_.size());

		Map<AdministrationInstance, Set<String>> drugAdminMap = getMapOfAdminInstanceToDrugsAdministered(drugAdministrations_);
		Map<DrugPair, Integer> drugPairMap = getMapOfDrugPairToOccurrence(drugAdminMap);
		Set<DrugPair> drugPairsWithMinOccurrence = getDrugPairsWithMinOccurrence(
				drugPairMap, minOccurrence_);

		return drugPairsWithMinOccurrence;
	}

	/**
	 * Get the drug pairs that occur with at least the minimum specified
	 * occurrence.
	 * 
	 * @param drugPairToOccurrenceMap_
	 *            A map of drug pairs to their occurrence level.
	 * @param minOccurrence_
	 *            The minimum occurrence of the drug pair required.
	 * @return Only those drug pairs meeting the minimum occurrence.
	 */
	protected static Set<DrugPair> getDrugPairsWithMinOccurrence(
			final Map<DrugPair, Integer> drugPairToOccurrenceMap_,
			final int minOccurrence_) {
		// Now only return those that meet the minimum occurrence requirements
		LOGGER.info(
				"Going to calculate which of the {} drug pairs occurred more than {} times.",
				drugPairToOccurrenceMap_.size(), minOccurrence_);
		Set<DrugPair> drugPairsWithMinOccurrence = new HashSet<DrugPair>();
		for (Map.Entry<DrugPair, Integer> drugPairEntry : drugPairToOccurrenceMap_
				.entrySet()) {
			Integer drugPairOccurrence = drugPairEntry.getValue();
			if (drugPairOccurrence >= minOccurrence_) {
				DrugPair drugPair = drugPairEntry.getKey();
				drugPairsWithMinOccurrence.add(drugPair);
			}
		}
		LOGGER.info("Found {} drug pairs that occurred more than {} times.",
				drugPairsWithMinOccurrence.size(), minOccurrence_);

		return drugPairsWithMinOccurrence;
	}

	/**
	 * Get a map of administration instance (defined by the patient and the
	 * date) to drugs administered during that instance.
	 * 
	 * @param drugAdministrations_
	 *            A list of drug administrations (a single drug with its
	 *            administration information).
	 * @return A map of administration instance to drugs administered during
	 *         that instance.
	 */
	protected static Map<AdministrationInstance, Set<String>> getMapOfAdminInstanceToDrugsAdministered(
			final List<SingleDrugAdministration> drugAdministrations_) {
		LOGGER.info(
				"Going to map administration instances to set of drugs for {} drug administrations.",
				drugAdministrations_.size());

		// Initialize the map to return.
		Map<AdministrationInstance, Set<String>> drugAdminMap = new HashMap<AdministrationInstance, Set<String>>();

		// Loop over the individual drug administrations.
		for (SingleDrugAdministration drugAdministration : drugAdministrations_) {
			AdministrationInstance adminInstance = drugAdministration
					.getAdministrationInstance();
			String drugAdministered = drugAdministration.getDrug();
			// Check if we've seen this administration instance before.
			Set<String> drugsSoFar;
			if (drugAdminMap.containsKey(adminInstance)) {
				// We've seen this administration instance before.
				drugsSoFar = drugAdminMap.get(adminInstance);
			} else {
				// This is a new administration instance.
				drugsSoFar = new HashSet<String>();
			}
			// Add the new drug to either the existing set or the new set.
			drugsSoFar.add(drugAdministered);
			// Update the map.
			drugAdminMap.put(adminInstance, drugsSoFar);
		}

		// Return the final map.
		return drugAdminMap;
	}

	/**
	 * Get a map of drug pairs to number of occurrences.
	 * 
	 * @param drugAdminMap_
	 *            A map of drug administration instances to the drugs that were
	 *            given during that administration.
	 * @return A map of pairs of drugs to number of times administered together.
	 */
	static protected Map<DrugPair, Integer> getMapOfDrugPairToOccurrence(
			final Map<AdministrationInstance, Set<String>> drugAdminMap_) {
		LOGGER.info("Going to map drug pairs to number of occurrences for "
				+ "{} administration instances.", drugAdminMap_.size());

		// Initialize the map to return
		Map<DrugPair, Integer> drugPairToOccurrenceMap = new HashMap<DrugPair, Integer>();

		// Iterate through the sets of drugs administered together.
		for (Set<String> drugSet : drugAdminMap_.values()) {
			// Get all of the possible drug pairs in the set.
			Set<DrugPair> drugPairs = findAllDrugPairs(drugSet);
			// For each drug pair, keep track of the number of times
			// encountered.
			for (DrugPair drugPair : drugPairs) {
				if (drugPairToOccurrenceMap.containsKey(drugPair)) {
					// We've seen this drug pair before.
					// Increment the occurrence count.
					Integer currentCount = drugPairToOccurrenceMap
							.get(drugPair);
					drugPairToOccurrenceMap.put(drugPair, currentCount + 1);
				} else {
					// This is a new drug pair. Start the count at 1.
					drugPairToOccurrenceMap.put(drugPair, 1);
				}
			}
		}

		LOGGER.info("Mapped {} drug pairs to their respecitive occurrences.",
				drugPairToOccurrenceMap.size());
		return drugPairToOccurrenceMap;

	}

	/**
	 * Given a set of drugs, return all pairs of drugs. Order is unimportant and
	 * a drug should not be paired with itself.
	 * 
	 * @param drugSet_
	 *            The set of drugs from which to grab pairs.
	 * @return The set of all drug pairs.
	 */
	static protected Set<DrugPair> findAllDrugPairs(final Set<String> drugSet_) {
		LOGGER.info("Finding all drug pairs for {} drugs.", drugSet_.size());

		// Create the set to return.
		Set<DrugPair> allDrugPairs = new HashSet<DrugPair>();

		// You can't have a pair if there is one or fewer drugs.
		int numDrugs = drugSet_.size();
		if (numDrugs <= 1) {
			LOGGER.info("Found 0 drug pairs for {} drugs.", drugSet_.size());
			// Return an empty set of drug pairs.
			return allDrugPairs;
		}

		// If there are two or more drugs, make all possible drug pairs.
		// Let's loop twice over a list (convert the set to a list).
		List<String> drugList = Lists.newArrayList(drugSet_);
		for (int a = 0; a < numDrugs -1; a++) {
			String drugA = drugList.get(a);
			LOGGER.debug("Drug A: {}.", drugA);
			for (int b = 1; b < numDrugs; b++) {
				String drugB = drugList.get(b);
				LOGGER.debug("Drug B: {}.", drugB);
				if (a < b){
					// This is a valid drug pair
					allDrugPairs.add(new DrugPair(drugA, drugB));

					// If a and b are pointing to the same drug, skip
					// because the drug pair (a, a) is not meaningful.
					// If a is pointing to a drug after b, skip
					// because we don't need both (a, b) and (b, a).
				}
			}
		}
		LOGGER.info("Found {} drug pairs.", allDrugPairs.size());

		return allDrugPairs;
	}
}
