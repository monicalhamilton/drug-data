package file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import api.AdministrationInstance;
import api.DrugPair;
import api.SingleDrugAdministration;

public class DrugPairFileWriter {

	protected static final String DELIMITER = ",";

	public DrugPairFileWriter() {
	}

	public void writePairs(final Set<DrugPair> drugPairs_,
			final String filename_) {
		PrintWriter w = null;
		try {
			w = new PrintWriter(new FileWriter(filename_));
			for (DrugPair drugPair : drugPairs_) {
				Set<String> drugs = drugPair.getDrugs();
				
				w.println();
			}
		} catch (FileNotFoundException e) {
			// TODO
			System.out.println("Caught exception.");
		} catch (IOException e) {
			// TODO
		} finally {
			if (w != null) {
				w.close();
			}
		}
	}

}
