package calc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import file.DrugFileParser;
import file.DrugPairFileWriter;

import api.AdministrationInstance;
import api.DrugPair;
import api.SingleDrugAdministration;

public class DrugPairCalculator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DrugPairCalculator.class);

	private static final int DEFAULT_MINIMUM_OCCURRENCES = 25;

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
	public void writeAllDrugPairs(final String inFilename_,
			final String outFilename_, final int minOccurrence_) {
		LOGGER.info(
				"Going to get all drug pairs from {} with minimum occurrence {} and write to file {}.",
				inFilename_, minOccurrence_, outFilename_);
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
		LOGGER.info(
				"Going to calculate the drug pairs with minimum occurrence {} "
						+ "on a list of drug administrations of size {}.",
				minOccurrence_, drugAdministrations_.size());

		Map<AdministrationInstance, Set<String>> drugAdminMap = getMapOfAdminInstanceToDrugsAdministered(drugAdministrations_);
		Map<DrugPair, Integer> drugPairMap = getMapOfDrugPairToOccurrence(drugAdminMap);

		// Now only return those that meet the minimum occurrence requirements
		LOGGER.info(
				"Going to calculate which of the {} drug pairs occurred more than {} times.",
				drugPairMap.size(), minOccurrence_);
		Set<DrugPair> drugPairsWithMinOccurrence = new HashSet<DrugPair>();
		for (Map.Entry<DrugPair, Integer> drugPairEntry : drugPairMap
				.entrySet()) {
			Integer drugPairOccurrence = drugPairEntry.getValue();
			if (drugPairOccurrence >= minOccurrence_) {
				DrugPair drugPair = drugPairEntry.getKey();
				drugPairsWithMinOccurrence.add(drugPair);
			}
		}
		LOGGER.info(
				"Found {} drug pairs that occurred more than {} times.",
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
	protected Map<AdministrationInstance, Set<String>> getMapOfAdminInstanceToDrugsAdministered(
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
	protected Map<DrugPair, Integer> getMapOfDrugPairToOccurrence(
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
	protected Set<DrugPair> findAllDrugPairs(final Set<String> drugSet_) {
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
		// Let's iterate twice over a list (convert the set to a list).
		List<String> drugList = new LinkedList<String>(drugSet_);
		int pointerA = 0;
		for (String drugA : drugList) {
			LOGGER.debug("Drug A: {}. Pointer A: {}.", drugA, pointerA);
			int pointerB = 0;
			for (String drugB : drugList) {
				LOGGER.debug("Drug B: {}. Pointer B: {}.", drugB, pointerB);
				if (pointerA < pointerB) {
					// This is a valid drug pair
					allDrugPairs.add(new DrugPair(drugA, drugB));
					
					// If a and b are pointing to the same drug, skip
					// because the drug pair (a, a) is not meaningful.
					// If a is pointing to a drug after b, skip
					// because we don't need both (a, b) and (b, a).
				} 
				pointerB++;
			}
			pointerA++;
		}
		LOGGER.info("Found {} drug pairs.", allDrugPairs.size());

		return allDrugPairs;
	}

	/**
	 * Create an output filename given an input filename and a minimum number of
	 * occurrences.
	 * 
	 * @param inputFilename_
	 *            The name of the input file.
	 * @param minOccurrences_
	 *            The minimum number of occurrences of a drug pair.
	 * @return The output filename.
	 */
	private static String createOutputFilename(final String inputFilename_,
			final Integer minOccurrences_) {
		return inputFilename_ + ".min" + minOccurrences_ + ".pairs";
	}

	private static Options getOptions() {
		Options options = new Options();
		// Input file option
		Option inputFilename = OptionBuilder.withLongOpt("in")
				.withArgName("in").hasArg().isRequired(true)
				.withDescription("input filename").create();
		options.addOption(inputFilename);
		Option outputFilename = OptionBuilder.withLongOpt("out")
				.withArgName("out").hasArg().isRequired(false)
				.withDescription("output filename").create();
		options.addOption(outputFilename);
		Option minimumOccurrences = OptionBuilder.withLongOpt("min")
				.withArgName("min").hasArg().isRequired(false)
				.withDescription("minimum occurrences").create();
		options.addOption(minimumOccurrences);
		return options;
	}

	/**
	 * Runs a drug pair calculation.
	 * 
	 * @param args_
	 *            The arguments to provide. arg 0: The input file; arg 1: The
	 *            minimum number of occurrences; arg 2 (optional): The output
	 *            file.
	 */
	public static void main(final String[] args_) {
		CommandLineParser parser = new BasicParser();
		Options options = getOptions();
		try {

			// Input file is NOT optional
			CommandLine line = parser.parse(options, args_);
			String inputFilename = null;
			if (line.hasOption("in")) {
				inputFilename = line.getOptionValue("in");
			} else {
				LOGGER.error("Could not calculate drug pairs due to missing input filename.");
				System.exit(1);
			}

			// Min occurrences is optional
			Integer minOccurrences;
			if (line.hasOption("min")) {
				minOccurrences = Integer.valueOf(line.getOptionValue("min"));
			} else {
				minOccurrences = DEFAULT_MINIMUM_OCCURRENCES;
			}

			// Output is optional
			String outputFilename;
			if (line.hasOption("out")) {
				outputFilename = line.getOptionValue("out");
			} else {
				outputFilename = createOutputFilename(inputFilename,
						minOccurrences);
			}

			DrugPairCalculator calculator = new DrugPairCalculator();
			calculator.writeAllDrugPairs(inputFilename, outputFilename,
					minOccurrences);

		} catch (ParseException e) {
			LOGGER.error(
					"Could not calculate drug pairs due to parse exception.", e);
		}

	}
}
