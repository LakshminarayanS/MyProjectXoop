package com.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

//	private static final String FILE_PATH = "src/test/resources/TestData.xlsx";

	public static Object[][] getTestData(String filePath, String sheetName) {

		Object[][] data = null;
		try {

			FileInputStream fis = new FileInputStream(filePath);
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheet(sheetName);

			int rowCount = sheet.getLastRowNum() + 1; // Get the total number of rows (including header)
			int colCount = sheet.getRow(0).getLastCellNum(); // Get the total number of columns

			data = new Object[rowCount - 1][colCount]; // Excluding header

			for (int i = 1; i < rowCount; i++) { // Skip header row
				Row row = sheet.getRow(i);
				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j);
					data[i - 1][j] = (cell != null) ? cell.toString() : "";
				}
			}

			workbook.close();
			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
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
