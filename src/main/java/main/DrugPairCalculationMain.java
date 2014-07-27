package main;

import java.util.List;
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

import api.DrugPair;
import api.SingleDrugAdministration;

import calc.DrugPairCalculator;
import file.DrugFileParser;
import file.DrugPairFileWriter;

/**
 * Main entry point for calculating drug pairs from a file of inputs and writing
 * to a file of outputs.
 * 
 * @author Monica
 */
public class DrugPairCalculationMain {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DrugPairCalculationMain.class);

	private static final int DEFAULT_MINIMUM_OCCURRENCES = 25;

	private static final DrugFileParser _drugFileParser = new DrugFileParser();
	private static final DrugPairFileWriter _drugPairFileWriter = new DrugPairFileWriter();

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
	public static void writeAllDrugPairs(final String inFilename_,
			final String outFilename_, final int minOccurrence_) {
		LOGGER.info(
				"Going to get all drug pairs from {} with minimum occurrence {} and write to file {}.",
				inFilename_, minOccurrence_, outFilename_);
		List<SingleDrugAdministration> drugAdmins = _drugFileParser
				.parseFile(inFilename_);
		Set<DrugPair> drugPairs = DrugPairCalculator
				.getDrugPairsWithMinOccurence(drugAdmins, minOccurrence_);
		_drugPairFileWriter.writePairs(drugPairs, outFilename_);
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
	 *            Required: --in <input filename>
	 *            Optional: --min <minimum occurrence>
	 *            Optional: --out <output filename>
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

			writeAllDrugPairs(inputFilename, outputFilename, minOccurrences);

		} catch (ParseException e) {
			LOGGER.error(
					"Could not calculate drug pairs due to parse exception.", e);
		}
	}
}
