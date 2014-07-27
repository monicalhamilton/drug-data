package calc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import file.DrugFileParser;
import file.DrugPairFileWriter;

import api.AdministrationInstance;
import api.DrugPair;
import api.SingleDrugAdministration;

public class DrugPairCalculator {

	private final DrugFileParser _drugFileParser = new DrugFileParser();
	private final DrugPairFileWriter _drugPairFileWriter = new DrugPairFileWriter();

	/**
	 * Get all drug pairs administered together with a given minimum occurrence
	 * from a file.
	 * 
	 * @param inFilename_
	 *            Name of a file containing drug administration information.
	 * @param outFilename_
	 *            Name of a file to write drug pairs to.
	 * @param minOccurrence_
	 *            The minimum number of times a pair of drugs must be
	 *            administered together in order to be returned.
	 * @return A set of all drug pairs administered together with a given
	 *         minimum occurrence.
	 */
	public void getAllDrugPairs(final String inFilename_,
			final String outFilename_, final int minOccurrence_) {
		List<SingleDrugAdministration> drugAdmins = _drugFileParser
				.parseFile(inFilename_);
		Set<DrugPair> drugPairs = getDrugPairsWithMinOccurence(drugAdmins,
				minOccurrence_);
		_drugPairFileWriter.writePairs(drugPairs, outFilename_);
	}

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
	public Set<DrugPair> getDrugPairsWithMinOccurence(
			final List<SingleDrugAdministration> drugAdministrations_,
			final int minOccurrence_) {
		Map<AdministrationInstance, Set<String>> drugAdminMap = getMapOfAdminInstanceToDrugsAdministered(drugAdministrations_);
		Map<DrugPair, Integer> drugPairMap = getMapOfDrugPairtoOccurrence(drugAdminMap);

		// Now only return those that meet the minimum occurrence requirements
		Set<DrugPair> drugPairsWithMinOccurrence = new HashSet<DrugPair>();
		for (Map.Entry<DrugPair, Integer> drugPairEntry : drugPairMap
				.entrySet()) {
			Integer drugPairOccurrence = drugPairEntry.getValue();
			if (drugPairOccurrence >= minOccurrence_) {
				DrugPair drugPair = drugPairEntry.getKey();
				drugPairsWithMinOccurrence.add(drugPair);
			}
		}
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
	protected Map<AdministrationInstance, Set<String>> getMapOfAdminInstanceToDrugsAdministered(
			final List<SingleDrugAdministration> drugAdministrations_) {
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
	protected Map<DrugPair, Integer> getMapOfDrugPairtoOccurrence(
			final Map<AdministrationInstance, Set<String>> drugAdminMap_) {
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
	protected Set<DrugPair> findAllDrugPairs(final Set<String> drugSet_) {
		// Create the set to return.
		Set<DrugPair> allDrugPairs = new HashSet<DrugPair>();

		// You can't have a pair if there is one or fewer drugs.
		int numDrugs = drugSet_.size();
		if (numDrugs <= 1) {
			// Return an empty set of drug pairs.
			return allDrugPairs;
		}

		// If there are two or more drugs, make all possible drug pairs.
		// Let's iterate twice over a list (convert the set to a list).
		List<String> drugList = new LinkedList<String>(drugSet_);
		// a and b are pointers.
		int a = 0;
		for (String drugA : drugList) {
			int b = 0;
			for (String drugB : drugList) {
				if (a >= b) {
					// If a and b are pointing to the same drug, skip
					// because the drug pair (a, a) is not meaningful.
					// If a is pointing to a drug after b, skip
					// because we don't need both (a, b) and (b, a).
					continue;
				} else {
					// This is a valid drug pair
					allDrugPairs.add(new DrugPair(drugA, drugB));
				}
				b++;
			}
			a++;
		}

		return allDrugPairs;
	}
}
