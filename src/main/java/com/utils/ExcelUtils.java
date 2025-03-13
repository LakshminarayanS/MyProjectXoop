package com.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.Status;
import com.opencsv.CSVReader;

public class ExcelUtils {

	public static Object[][] readExcelData(String filePath, String sheetName) {
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

			if (sheet.getRow(0) == null) {
				throw new RuntimeException("Header row is missing in the Excel sheet.");
			}

			int rowCount = sheet.getLastRowNum() + 1; // Includes header
			int colCount = sheet.getRow(0).getLastCellNum(); // Get total columns

			if (rowCount <= 1 || colCount == 0) {
				ExtentReportManager.log(Status.WARNING, "No test data found in sheet: " + sheetName);
				throw new RuntimeException("Test data is missing in the provided Excel sheet.");
			}

			data = new Object[rowCount - 1][colCount]; // Exclude header row

			Row headerRow = sheet.getRow(0);
			System.out.print("Column Headers: ");
			for (int j = 0; j < colCount; j++) {
				System.out.print(headerRow.getCell(j) + " | ");
			}
			System.out.println();

			for (int i = 1; i < rowCount; i++) { // Skip header row
				Row row = sheet.getRow(i);
				if (row == null) { // Handle empty row
					for (int j = 0; j < colCount; j++) {
						data[i - 1][j] = "";
					}
					continue;
				}
				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (cell.getCellType()) {
						case STRING:
							data[i - 1][j] = cell.getStringCellValue().trim();
							break;
						case NUMERIC:
							if (cell.getNumericCellValue() % 1 == 0) {
								data[i - 1][j] = (int) cell.getNumericCellValue();
							} else {
								data[i - 1][j] = cell.getNumericCellValue();
							}
							break;
						case BOOLEAN:
							data[i - 1][j] = cell.getBooleanCellValue();
							break;
						default:
							data[i - 1][j] = "";
						}
					} else {
						data[i - 1][j] = "";
					}
				}
			}

			ExtentReportManager.log(Status.PASS, "Successfully read test data from Excel.");

		} catch (FileNotFoundException e) {
			throw new RuntimeException("Excel file not found: " + filePath, e);
		} catch (IOException e) {
			throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
		} finally {
			try {
				if (workbook != null)
					workbook.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				ExtentReportManager.log(Status.WARNING, "Failed to close Excel resources: " + e.getMessage());
			}
		}
		return data;
	}

	public static Object[][] readCSVData(String filePath) {
		List<Object[]> data = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
			String[] nextLine;
			boolean isFirstRow = true;
			while ((nextLine = reader.readNext()) != null) {
				if (isFirstRow) {
					isFirstRow = false;
					continue;
				}
				if (nextLine.length == 0 || (nextLine.length == 1 && nextLine[0].trim().isEmpty())) {
					continue;
				}
				data.add(nextLine);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading CSV file: " + e.getMessage());
		}
		return data.toArray(new Object[0][]);
	}
}
