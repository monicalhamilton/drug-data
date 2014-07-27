package file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.DrugPair;

/**
 * Utility to write drug pairs to file
 * 
 * @author Monica
 */
public class DrugPairFileWriter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DrugPairFileWriter.class);
	
	private static final int DEBUG_INTERVAL = 10;

	// Default delimiter
	private static final String DELIMITER = ",";

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
		LOGGER.info("Writing {} drug pairs to file {}.", drugPairs_, filename_);
		PrintWriter w = null;
		try {
			w = new PrintWriter(new FileWriter(filename_));
			int count = 0;
			for (DrugPair drugPair : drugPairs_) {
				Set<String> drugs = drugPair.getDrugs();
				w.println(StringUtils.join(drugs, DELIMITER));
				count++;
				if (count % DEBUG_INTERVAL == 0) {
					LOGGER.info("Wrote {} drug pairs so far.", count);
				}
			}
			LOGGER.info("Finished writing {} drug pairs to file {}.",
					drugPairs_, filename_);
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
