package Practise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadAndWriteDataInExcelSheet {

	public static void main(String[] args) throws IOException {
		ReadAndWriteDataInExcelSheet read = new ReadAndWriteDataInExcelSheet();
		String file1 = System.getProperty("user.dir");
		System.out.println("file name = " + file1);
		String filepath = file1 + "/src/test/resources/testData/TestData.xlsx";
		System.out.println("file name = " + filepath);
		read.readDataExcel(filepath, "Sheet2");
		read.writeExcel(filepath,"Sheet2","Pass");

	}

	public void readDataExcel(String filename, String sheetname) throws IOException {
		File file = new File(filename);
		FileInputStream inputStream = new FileInputStream(file);

		Workbook book = null;
		String fileExtension = filename.substring(filename.indexOf("."));
		System.out.println("fileExtension= " + fileExtension);
		if (fileExtension.equals(".xlsx")) {
			book = new XSSFWorkbook(inputStream);
		} else if (fileExtension.equals(".xls")) {
			book = new HSSFWorkbook(inputStream);
		}

		Sheet sheet = book.getSheet(sheetname);

		int rowFirst = sheet.getFirstRowNum();
		System.out.println("rowFirst : " + rowFirst);

		int rowLast = sheet.getLastRowNum();
		System.out.println("rowLast : " + rowLast);

		int totalRowsCount = rowLast - rowFirst;
		System.out.println("Total No of rows : " + totalRowsCount);

		for (int i = 0; i <= rowLast; i++) {
			Row row = sheet.getRow(i);
			System.out.println("cell count = " + row.getLastCellNum());
			for (int j = 0; j < row.getLastCellNum(); j++) {
				System.out.print(row.getCell(j).getStringCellValue() + " ");
			}
			System.out.println(" ");

		}

	}

	public void writeExcel(String filename,String sheetname, String status) throws IOException
	{
		File file = new File(filename);
		FileInputStream inputStream = new FileInputStream(file);

		Workbook book = null;
		String fileExtension = filename.substring(filename.indexOf("."));
		System.out.println("fileExtension= " + fileExtension);
		if (fileExtension.equals(".xlsx")) {
			book = new XSSFWorkbook(inputStream);
		} else if (fileExtension.equals(".xls")) {
			book = new HSSFWorkbook(inputStream);
		}

		Sheet sheet = book.getSheet(sheetname);
		int rowFirst = sheet.getFirstRowNum();
		System.out.println("rowFirst : " + rowFirst);

		int rowLast = sheet.getLastRowNum();
		System.out.println("rowLast : " + rowLast);

		int totalRowsCount = rowLast - rowFirst;
		System.out.println("Total No of rows : " + totalRowsCount);
		
		
		for(int i=1;i<=rowLast;i++)
		{
			Row row=sheet.getRow(i);
			int cellength=row.getLastCellNum();
			Cell cell=row.createCell(cellength);
			cell.setCellValue(status);
					
		}
		
		 //Close input stream

	    inputStream.close();

	    //Create an object of FileOutputStream class to create write data in excel file

	    FileOutputStream outputStream = new FileOutputStream(filename);

	    //write data in the excel file

	    book.write(outputStream);

	    //close output stream

	    outputStream.close();
		
		
	}
}
