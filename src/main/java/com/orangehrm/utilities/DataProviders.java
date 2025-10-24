package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {

	private static final String FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/testdata/testData.xlsx";

	@DataProvider(name = "validLoginData")
	public static Object[][] validLoginData() {
		return getSheetData("validLoginData");
	}

	@DataProvider(name = "invalidLoginData")
	public static Object[][] invalidLoginData() {
		return getSheetData("invalidLoginData");
	}

	private static Object[][] getSheetData(String sheetName) {
		List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];

		for (int i = 0; i < sheetData.size(); i++) {
			data[i] = sheetData.get(i);
		}
		return data;
	}
	
	 // CSV file path
//    private static final String FILE_PATH = System.getProperty("user.dir") + "src/test/resources/testdata/testData.xlsx";
//
//    @DataProvider(name = "validLoginDataCSV")
//    public static Object[][] validLoginDataCSV() {
//        return getCSVData(true);  // true = skip header
//    }
//
//    @DataProvider(name = "invalidLoginDataCSV")
//    public static Object[][] invalidLoginDataCSV() {
//        return getCSVData(true);  // true = skip header
//    }
//
//    private static Object[][] getCSVData(boolean skipHeader) {
//        // Read CSV data using your CSVReaderUtility
//        List<String[]> csvData = CSVReaderUtility.getCSVData(FILE_PATH, skipHeader);
//
//        // Convert List<String[]> to Object[][]
//        Object[][] data = new Object[csvData.size()][csvData.get(0).length];
//        for (int i = 0; i < csvData.size(); i++) {
//            data[i] = csvData.get(i);
//        }
//        return data;
//    }
}
