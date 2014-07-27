package file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import api.AdministrationInstance;
import api.SingleDrugAdministration;

public class DrugFileParser {

	protected static final String DELIMITER = ",";

	protected static final int PATIENT_ID_INDEX = 0;
	protected static final int ADMIN_DATE_INDEX = 1;
	protected static final int DRUG_NAME_INDEX = 2;

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
		List<SingleDrugAdministration> drugAdministrations = new LinkedList<SingleDrugAdministration>();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(filename_));
			String line;
			while ((line = r.readLine()) != null) {
				SingleDrugAdministration drugAdministration = parseLine(line);
				drugAdministrations.add(drugAdministration);
			}
		} catch (FileNotFoundException e) {
			// TODO
			System.out.println("Caught exception.");
		} catch (IOException e) {
			// TODO
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					// TODO
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

		return drugAdmin;
	}

}
