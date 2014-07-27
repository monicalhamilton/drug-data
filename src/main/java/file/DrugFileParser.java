package file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.AdministrationInstance;
import api.SingleDrugAdministration;

public class DrugFileParser {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DrugFileParser.class);

	private static final String DELIMITER = ",";
	private static final int DEBUG_INTERVAL = 10;

	private static final int PATIENT_ID_INDEX = 0;
	private static final int ADMIN_DATE_INDEX = 1;
	private static final int DRUG_NAME_INDEX = 2;

	public DrugFileParser() {
	}

	/**
	 * Parse a file of drug administration data into a list of drug
	 * administration objects.
	 * 
	 * @param filename_
	 *            The name of the file to parse.
	 * @return A list of drug administrations.
	 * @throws FileNotFoundException
	 *             If given filename was not found.
	 * @throws IOException
	 *             If the
	 */
	public List<SingleDrugAdministration> parseFile(final String filename_) {
		LOGGER.info("Parsing file {} into drug administration objects.",
				filename_);
		List<SingleDrugAdministration> drugAdministrations = new LinkedList<SingleDrugAdministration>();
		BufferedReader r = null;

		try {
			r = new BufferedReader(new FileReader(filename_));
			String line;
			int lineCount = 0;
			while ((line = r.readLine()) != null) {
				SingleDrugAdministration drugAdministration = parseLine(line);
				drugAdministrations.add(drugAdministration);
				lineCount++;
				if (lineCount % DEBUG_INTERVAL == 0) {
					LOGGER.info("Parsed {} lines.", lineCount);
				}
			}
			LOGGER.info("Finished parsing file {} with {} total lines.", filename_, lineCount);
		} catch (FileNotFoundException e) {
			LOGGER.error("Could not parse file {} because file was not found.",
					filename_, e);
		} catch (IOException e) {
			LOGGER.error("Could not parse file {} due to I/O exception.",
					filename_, e);
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					LOGGER.warn("Could not close file {}.", filename_, e);
				}
			}
		}

		return drugAdministrations;
	}

	/**
	 * Parse a delimited file line into a drug administration object.
	 * 
	 * @param line_
	 *            The file line containing administration data.
	 * @return The drug administration.
	 */
	protected SingleDrugAdministration parseLine(final String line_) {
		String[] splitLine = line_.split(DELIMITER);
		String patientId = splitLine[PATIENT_ID_INDEX];
		String adminDate = splitLine[ADMIN_DATE_INDEX];
		AdministrationInstance adminInstance = new AdministrationInstance(
				patientId, adminDate);

		String drugName = splitLine[DRUG_NAME_INDEX];

		SingleDrugAdministration drugAdmin = new SingleDrugAdministration(
				adminInstance, drugName);

		LOGGER.debug("Parsed line into {}", drugAdmin);

		return drugAdmin;
	}

}
