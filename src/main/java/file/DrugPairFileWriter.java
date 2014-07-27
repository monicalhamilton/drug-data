package file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import api.DrugPair;

/**
 * Utility to write drug pairs to file
 * 
 * @author Monica
 */
public class DrugPairFileWriter {

	// Default delimiter
	protected static final String DELIMITER = ",";

	/**
	 * Empty constructor
	 */
	public DrugPairFileWriter() {
	}

	/**
	 * Write the given drug pair set to file.
	 * 
	 * @param drugPairs_
	 *            The drug pairs.
	 * @param filename_
	 *            The filename to write to.
	 */
	public void writePairs(final Set<DrugPair> drugPairs_,
			final String filename_) {
		PrintWriter w = null;
		try {
			w = new PrintWriter(new FileWriter(filename_));
			for (DrugPair drugPair : drugPairs_) {
				Set<String> drugs = drugPair.getDrugs();
				w.println(StringUtils.join(drugs, DELIMITER));
			}
		} catch (IOException e) {
			System.out.println("Could not write drug pairs to file "
					+ filename_ + " due to exception: " + e);
		} finally {
			if (w != null) {
				w.close();
			}
		}
	}

}
