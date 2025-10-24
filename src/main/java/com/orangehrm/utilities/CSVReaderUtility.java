package com.orangehrm.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderUtility {

	/**
	 * Reads data from a CSV file and returns it as a List of String arrays. Each
	 * String[] represents one row.
	 *
	 * @param filePath   Path to the CSV file
	 * @param skipHeader true to skip the first line (header row)
	 * @return List of String[] containing CSV data
	 */
	public static List<String[]> getCSVData(String filePath, boolean skipHeader) {
		List<String[]> data = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			boolean isHeader = true;

			while ((line = br.readLine()) != null) {
				// Skip the first row if it's a header
				if (skipHeader && isHeader) {
					isHeader = false;
					continue;
				}

				// Split the line by comma
				String[] values = line.split(",");

				// Trim spaces around each value
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
				}

				data.add(values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
}
