package com.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.Status;

public class ExcelUtils {

//	private static final String FILE_PATH = "src/test/resources/TestData.xlsx";

	public static Object[][] getTestData(String filePath, String sheetName) {

		Object[][] data = null;
		FileInputStream fis = null;
		Workbook workbook = null;
		try {
			ExtentReportManager.log(Status.INFO,
					"Reading test data from Excel file: " + filePath + ", Sheet: " + sheetName);

			fis = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				ExtentReportManager.log(Status.FAIL, "Sheet '" + sheetName + "' not found in file: " + filePath);
				throw new RuntimeException("Sheet '" + sheetName + "' does not exist in the Excel file.");
			}

			int rowCount = sheet.getLastRowNum() + 1; // Get the total number of rows (including header)
			int colCount = sheet.getRow(0).getLastCellNum(); // Get the total number of columns

			if (rowCount <= 1 || colCount == 0) {
				ExtentReportManager.log(Status.WARNING, "No test data found in sheet: " + sheetName);
				throw new RuntimeException("Test data is missing in the provided Excel sheet.");
			}

			data = new Object[rowCount - 1][colCount]; // Excluding header

			for (int i = 1; i < rowCount; i++) { // Skip header row
				Row row = sheet.getRow(i);
				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j);
					data[i - 1][j] = (cell != null) ? cell.toString() : "";
				}
			}

			ExtentReportManager.log(Status.PASS, "Successfully read test data from Excel.");

		} catch (FileNotFoundException e) {
			ExtentReportManager.log(Status.INFO, "Excel file not found: " + filePath);
			throw new RuntimeException("Excel file not found: " + filePath, e);
		} catch (IOException e) {
			ExtentReportManager.log(Status.FAIL, "Error reading Excel file: " + e.getMessage());
			throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL, "Unexpected error while reading Excel file: " + e.getMessage());
			throw new RuntimeException("Unexpected error while reading Excel file: " + e.getMessage(), e);
		} finally {
			try {
				if (workbook != null) {
					workbook.close(); // Properly closing the workbook
				}
				if (fis != null) {
					fis.close(); // Properly closing the file stream
				}
			} catch (IOException e) {
				ExtentReportManager.log(Status.WARNING, "Failed to close Excel resources: " + e.getMessage());
			}
		}

		return data;
	}

	/*
	 * private static String getCellValueAsString(Cell cell) { switch
	 * (cell.getCellType()) { case STRING: return cell.getStringCellValue(); case
	 * NUMERIC: return String.valueOf(cell.getNumericCellValue()); case BOOLEAN:
	 * return String.valueOf(cell.getBooleanCellValue()); default: return ""; } }
	 */

}
