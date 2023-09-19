package utilities;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ExcelDateUtil class is refer to read and write in excel
 *
 */
public class ExcelDataUtil {

	private static FileInputStream fs = null;
	private static Workbook workbook = null;
	private static Sheet sheet = null;

	private static int columnToLookTestCaseID = Integer.parseInt(ConfigReaderWriter.getValue("columnToLookTestCaseID"));
	private static String testDatafilePath = ConfigReaderWriter.getValue("testDataExcelPath");
	protected static List<String> testsList = new ArrayList<>();
	private static String excelextensionxlsx = ".xlsx";
	public static final String TESTRESULTSHEET = "testResultSheet";
	public static final String Y = "Y";
	public static final String EXCEPTIONCAUGHT = "Exception caught";
	private static String excelextensionxls = ".xls";
	private static String automationcontrolexcelpath = "AutomationControlExcelPath";
	private static final String INVALID_SHEET_MESSAGE = "Error! No such sheet available in Excel file";

	/**
	 * <H1>Excel initialize</H1>
	 * 
	 * @param filePath
	 * @param sheetName
	 */
	public static void init(String filePath, String sheetName) {
		String fileExtensionName = filePath.substring(filePath.indexOf('.'));
		try {
			fs = new FileInputStream(filePath);
			if (fileExtensionName.equals(excelextensionxlsx)) {
				// If it is xlsx file then create object of XSSFWorkbook class
				workbook = new XSSFWorkbook(fs);
			}
			// Check condition if the file is xls file
			else if (fileExtensionName.equals(excelextensionxls)) {
				// If it is xls file then create object of XSSFWorkbook class
				workbook = new HSSFWorkbook(fs);
			}
			sheet = workbook.getSheet(sheetName);
		} catch (Exception e) {
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}

	}

	/**
	 * <H1>Get test data with test case id</H1>
	 * 
	 * @param testCaseID
	 * @return
	 */
	public static HashMap<String, String> getTestDataWithTestCaseID(String testCaseID, String sheetName) {
		boolean found = false;
		boolean isfirstRow = false;
		Row firstrow = null;
		String cellValue = null;
		// Initialize class
		// Get Path and Sheet Name from Property File
		final HashMap<String, String> currentRowData = new HashMap<String, String>();
		init(testDatafilePath, sheetName);
		Iterator<Row> rowIterator = sheet.iterator();
		try {
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (!isfirstRow) {
					firstrow = row;
					isfirstRow = true;
				}
				if (row.getCell(columnToLookTestCaseID).getStringCellValue().equalsIgnoreCase(testCaseID)) {
					found = true;
					for (int i = 0; i < row.getLastCellNum(); i++) {

						if (row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING) {
							cellValue = row.getCell(i).getStringCellValue();
							LogUtil.infoLog(ExcelDataUtil.class, cellValue);
						} else if (row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC) {
							cellValue = NumberToTextConverter.toText(row.getCell(i).getNumericCellValue());
							LogUtil.infoLog(ExcelDataUtil.class, cellValue);
						}
						if (cellValue == null) {
							throw new Exception("Cell value is null.");
						}
						cellValue = getUniqueString(cellValue);
						currentRowData.put(firstrow.getCell(i).getStringCellValue(), cellValue);
					}
					break;
				}

			}

			fs.close();

		} catch (Exception e) {
			LogUtil.errorLog(ExcelDataUtil.class, "caught exception", e);
		}
		if (!found)
			LogUtil.errorLog(ExcelDataUtil.class, "No data found with given key-> " + testCaseID);

		return currentRowData;

	}

	public static String getUniqueString(String string) {
		return string.replaceAll("UNIQUE", "" + System.currentTimeMillis());
	}

	/**
	 * <H1>Get common settings</H1>
	 * 
	 * @return
	 */
	public static CommonSettings getCommonSettings() {
		// 1. Read Excel File
		CommonSettings commonSettings = new CommonSettings();

		String sheetName = ConfigReaderWriter.getValue("AutomationControlSheet");
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + ConfigReaderWriter.getValue(automationcontrolexcelpath));
				Workbook wb = WorkbookFactory.create(fis);) {

			if (wb.getSheetIndex(wb.getSheet(sheetName)) == -1) {
				LogUtil.infoLog(ExcelDataUtil.class, INVALID_SHEET_MESSAGE + sheetName);

			}

			Sheet sheet = wb.getSheet(sheetName);

			// Set Project name from Column B1
			commonSettings.setProjectName(sheet.getRow(0).getCell(1).getStringCellValue());

			// Set Fixed Common Settings

			// Set Application type Column[B2] Row =2, Column =1
			String val = sheet.getRow(1).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "App Type: " + val);
			commonSettings.setAppType(val);

			// Set Application environment type Column[B3] Row =3, Column =1
			val = sheet.getRow(2).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "App Environment: " + val);
			commonSettings.setAppEnviornment(val);

			// Set BuildNumber Column[B4] Row =4, Column =1
			val = sheet.getRow(3).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Build Number: " + val);
			commonSettings.setBuildNumber(val);

			// Set Email Id Comma List Column[B5] Row =5, Column =1
			val = sheet.getRow(4).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "URL: " + val);
			commonSettings.setUrl(val);

			// Set ExecutionEnv Column[B7] Row =7, Column =1
			val = sheet.getRow(6).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Execetion Environment: " + val);
			commonSettings.setExecutionEnv(val);
			commonSettings.setUrl(val);

			// Set ExecutionEnv Column[B7] Row =7, Column =1
			val = sheet.getRow(6).getCell(1).getStringCellValue();
			commonSettings.setExecutionEnv(val);

			// Set CloudProvider Column[B8] Row =8, Column =1
			val = sheet.getRow(7).getCell(1).getStringCellValue();
			commonSettings.setCloudProvider(val);

			// Set HostName Column[B9] Row =9, Column =1
			val = sheet.getRow(8).getCell(1).getStringCellValue();
			commonSettings.setHostName(val);

			// Set Key Column[B10] Row =10, Column =1
			val = sheet.getRow(9).getCell(1).getStringCellValue();
			commonSettings.setKey(val);

			// Set RemoteOS Column[B11] Row =11, Column =1
			val = sheet.getRow(10).getCell(1).getStringCellValue();
			commonSettings.setRemoteOS(val);

			// Set Browser Column[B12] Row =12, Column =1
			val = sheet.getRow(11).getCell(1).getStringCellValue();
			commonSettings.setBrowser(val);

			// Set ManageToolName Column[B14] Row =14, Column =1
			val = sheet.getRow(13).getCell(1).getStringCellValue();
			commonSettings.setManageToolName(val);

			// Set TestlinkTool Column[B15] Row =15, Column =1
			val = sheet.getRow(14).getCell(1).getStringCellValue();
			commonSettings.setTestlinkTool(val);

			// Set TestLinkHostName Column[B16] Row =16, Column =1
			val = sheet.getRow(15).getCell(1).getStringCellValue();
			commonSettings.setTestLinkHostName(val);

			// Set TestlinkAPIKey Column[B17] Row =17, Column =1
			val = sheet.getRow(16).getCell(1).getStringCellValue();
			commonSettings.setTestlinkAPIKey(val);

			// Set TestlinkProjectName Column[B18] Row =18, Column =1
			val = sheet.getRow(17).getCell(1).getStringCellValue();
			commonSettings.setTestlinkProjectName(val);

			// Set TestlinkPlanName Column[B19] Row =19, Column =1
			val = sheet.getRow(18).getCell(1).getStringCellValue();
			commonSettings.setTestlinkPlanName(val);

			// Set BugToolName Column[B21] Row =21, Column =1
			val = sheet.getRow(20).getCell(1).getStringCellValue();
			commonSettings.setBugToolName(val);

			// Set bugTool Column[B22] Row =22, Column =1
			val = sheet.getRow(21).getCell(1).getStringCellValue();
			commonSettings.setbugTool(val);

			// Set bugToolHostName Column[B23] Row =23, Column =1
			val = sheet.getRow(22).getCell(1).getStringCellValue();
			commonSettings.setbugToolHostName(val);

			// Set bugToolUserName Column[B24] Row =24, Column =1
			val = sheet.getRow(23).getCell(1).getStringCellValue();
			commonSettings.setbugToolUserName(val);

			// Set bugToolPassword Column[B25] Row =25, Column =1
			val = sheet.getRow(24).getCell(1).getStringCellValue();
			commonSettings.setbugToolPassword(val);

			// Set bugToolProjectName Column[B26] Row =26, Column =1
			val = sheet.getRow(25).getCell(1).getStringCellValue();
			commonSettings.setbugToolProjectName(val);

			// Set JiraTestManagement Column[B29] Row =29, Column =1
			val = sheet.getRow(28).getCell(1).getStringCellValue();
			commonSettings.setJiraTestManagement(val);

			// Set JiraCycleID Column[B30] Row =30, Column =1
			val = sheet.getRow(29).getCell(1).getStringCellValue();
			commonSettings.setJiraCycleID(val);
			LogUtil.infoLog(ExcelDataUtil.class, "URL: " + val);
			commonSettings.setUrl(val);

			// Set ExecutionEnv Column[B7] Row =7, Column =1
			val = sheet.getRow(6).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Execetion Environment: " + val);
			commonSettings.setExecutionEnv(val);

			// Set CloudProvider Column[B8] Row =8, Column =1
			val = sheet.getRow(7).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Cloud Provider: " + val);
			commonSettings.setCloudProvider(val);

			// Set HostName Column[B9] Row =9, Column =1
			val = sheet.getRow(8).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Host Name: " + val);
			commonSettings.setHostName(val);

			// Set Key Column[B10] Row =10, Column =1
			val = sheet.getRow(9).getCell(1).getStringCellValue();
			commonSettings.setKey(val);

			// Set RemoteOS Column[B11] Row =11, Column =1
			val = sheet.getRow(10).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Remote OS: " + val);
			commonSettings.setRemoteOS(val);

			// Set Browser Column[B12] Row =12, Column =1
			val = sheet.getRow(11).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Browser: " + val);
			commonSettings.setBrowser(val);

			// Set ManageToolName Column[B14] Row =14, Column =1
			val = sheet.getRow(13).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Manage Tool Name: " + val);
			commonSettings.setManageToolName(val);

			// Set TestlinkTool Column[B15] Row =15, Column =1
			val = sheet.getRow(14).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Test Link Tool: " + val);
			commonSettings.setTestlinkTool(val);

			// Set TestLinkHostName Column[B16] Row =16, Column =1
			val = sheet.getRow(15).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Test Link Host Name: " + val);
			commonSettings.setTestLinkHostName(val);

			// Set TestlinkAPIKey Column[B17] Row =17, Column =1
			val = sheet.getRow(16).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Test Link API Key: " + val);
			commonSettings.setTestlinkAPIKey(val);

			// Set TestlinkProjectName Column[B18] Row =18, Column =1
			val = sheet.getRow(17).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Test Link Project Name: " + val);
			commonSettings.setTestlinkProjectName(val);

			// Set TestlinkPlanName Column[B19] Row =19, Column =1
			val = sheet.getRow(18).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Test Link Plan Name: " + val);
			commonSettings.setTestlinkPlanName(val);

			// Set BugToolName Column[B21] Row =21, Column =1
			val = sheet.getRow(20).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Bug Tool Name: " + val);
			commonSettings.setBugToolName(val);

			// Set bugTool Column[B22] Row =22, Column =1
			val = sheet.getRow(21).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Bug Tool: " + val);
			commonSettings.setbugTool(val);

			// Set bugToolHostName Column[B23] Row =23, Column =1
			val = sheet.getRow(22).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Bug Tool Host Name: " + val);
			commonSettings.setbugToolHostName(val);

			// Set bugToolUserName Column[B24] Row =24, Column =1
			val = sheet.getRow(23).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Bug Tool User Name: " + val);
			commonSettings.setbugToolUserName(val);

			// Set bugToolPassword Column[B25] Row =25, Column =1
			val = sheet.getRow(24).getCell(1).getStringCellValue();
			commonSettings.setbugToolPassword(val);

			// Set bugToolProjectName Column[B26] Row =26, Column =1
			val = sheet.getRow(25).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "Bug Tool Project Name: " + val);
			commonSettings.setbugToolProjectName(val);

			// Set JiraTestManagement Column[B29] Row =29, Column =1
			val = sheet.getRow(28).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "JIRA Test Management: " + val);
			commonSettings.setJiraTestManagement(val);

			// Set JiraCycleID Column[B30] Row =30, Column =1
			val = sheet.getRow(29).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "JIRA Cycle ID: " + val);
			commonSettings.setJiraCycleID(val);

			// Set JiraProjectID Column[B31] Row =31, Column =1
			val = sheet.getRow(30).getCell(1).getStringCellValue();
			LogUtil.infoLog(ExcelDataUtil.class, "JIRA Project ID: " + val);
			commonSettings.setUrl(val);

			// Set ExecutionEnv Column[B7] Row =7, Column =1
			val = sheet.getRow(6).getCell(1).getStringCellValue();
			commonSettings.setExecutionEnv(val);

			// Set CloudProvider Column[B8] Row =8, Column =1
			val = sheet.getRow(7).getCell(1).getStringCellValue();
			commonSettings.setCloudProvider(val);

			// Set HostName Column[B9] Row =9, Column =1
			val = sheet.getRow(8).getCell(1).getStringCellValue();
			commonSettings.setHostName(val);

			// Set Key Column[B10] Row =10, Column =1
			val = sheet.getRow(9).getCell(1).getStringCellValue();
			commonSettings.setKey(val);

			// Set RemoteOS Column[B11] Row =11, Column =1
			val = sheet.getRow(10).getCell(1).getStringCellValue();
			commonSettings.setRemoteOS(val);

			// Set Browser Column[B12] Row =12, Column =1
			val = sheet.getRow(11).getCell(1).getStringCellValue();
			commonSettings.setBrowser(val);

			// Set ManageToolName Column[B14] Row =14, Column =1
			val = sheet.getRow(13).getCell(1).getStringCellValue();
			commonSettings.setManageToolName(val);

			// Set TestlinkTool Column[B15] Row =15, Column =1
			val = sheet.getRow(14).getCell(1).getStringCellValue();
			commonSettings.setTestlinkTool(val);

			// Set TestLinkHostName Column[B16] Row =16, Column =1
			val = sheet.getRow(15).getCell(1).getStringCellValue();
			commonSettings.setTestLinkHostName(val);

			// Set TestlinkAPIKey Column[B17] Row =17, Column =1
			val = sheet.getRow(16).getCell(1).getStringCellValue();
			commonSettings.setTestlinkAPIKey(val);

			// Set TestlinkProjectName Column[B18] Row =18, Column =1
			val = sheet.getRow(17).getCell(1).getStringCellValue();
			commonSettings.setTestlinkProjectName(val);

			// Set TestlinkPlanName Column[B19] Row =19, Column =1
			val = sheet.getRow(18).getCell(1).getStringCellValue();
			commonSettings.setTestlinkPlanName(val);

			// Set BugToolName Column[B21] Row =21, Column =1
			val = sheet.getRow(20).getCell(1).getStringCellValue();
			commonSettings.setBugToolName(val);

			// Set bugTool Column[B22] Row =22, Column =1
			val = sheet.getRow(21).getCell(1).getStringCellValue();
			commonSettings.setbugTool(val);

			// Set bugToolHostName Column[B23] Row =23, Column =1
			val = sheet.getRow(22).getCell(1).getStringCellValue();
			commonSettings.setbugToolHostName(val);

			// Set bugToolUserName Column[B24] Row =24, Column =1
			val = sheet.getRow(23).getCell(1).getStringCellValue();
			commonSettings.setbugToolUserName(val);

			// Set bugToolPassword Column[B25] Row =25, Column =1
			val = sheet.getRow(24).getCell(1).getStringCellValue();
			commonSettings.setbugToolPassword(val);

			// Set bugToolProjectName Column[B26] Row =26, Column =1
			val = sheet.getRow(25).getCell(1).getStringCellValue();
			commonSettings.setbugToolProjectName(val);

			// Set JiraTestManagement Column[B29] Row =29, Column =1
			val = sheet.getRow(28).getCell(1).getStringCellValue();
			commonSettings.setJiraTestManagement(val);

			// Set JiraCycleID Column[B30] Row =30, Column =1
			val = sheet.getRow(29).getCell(1).getStringCellValue();
			commonSettings.setJiraCycleID(val);

			// Set JiraProjectID Column[B31] Row =31, Column =1
			val = sheet.getRow(30).getCell(1).getStringCellValue();
			commonSettings.setJiraProjectID(val);

		} // End try
		catch (Exception e) {
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}
		return commonSettings;

	}

	/**
	 * <H1>Get Flag from excel</H1>
	 * 
	 * @param sheetName
	 * @param searchValue
	 * @return
	 */
	public static String getFlagExcel(String sheetName, String searchValue) {
		int sheetSize = 0;
		int rowNum = 1;
		String strVal = "";
		String strflag = "NA";
		try (FileInputStream fis = new FileInputStream(ConfigReaderWriter.getValue(automationcontrolexcelpath));
				Workbook wb = WorkbookFactory.create(fis);) {

			if (wb.getSheetIndex(wb.getSheet(sheetName)) == -1) {
				LogUtil.errorLog(ExcelDataUtil.class, INVALID_SHEET_MESSAGE + sheetName);
				throw new InvalidSheetException("No such sheet available in Excel file: " + sheetName);
			}

			Sheet sheet = wb.getSheet(sheetName);
			sheetSize = sheet.getLastRowNum();

			for (int i = rowNum; i <= sheetSize; i++) {

				strVal = sheet.getRow(i).getCell(0).getStringCellValue();

				if (searchValue.equalsIgnoreCase(strVal)) {
					strflag = wb.getSheet(sheetName).getRow(i).getCell(1).getStringCellValue();
					break;
				}
			}

		} catch (Exception e) {
			strflag = "NA";
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}

		return strflag;
	}

	// Read Data from Excel File AutomationControlSheet.xls(SuiteControlSheet),
	// Reading Y/N for including a test case in suite to run.
	/**
	 * <H1>Read Data from Excel File
	 * AutomationControlSheet.xls(SuiteControlSheet)</H1>
	 * 
	 * @param suiteName
	 * @return
	 */
	public static boolean isSuiteRunnable(String suiteName) {
		boolean isRunnable = false;
		String strVal;
		try {
			strVal = getFlagExcel("SuiteControlSheet", suiteName);
			if (strVal.equalsIgnoreCase(Y)) {
				isRunnable = true;
			} else {
				isRunnable = false;
			}
		} catch (Exception e) {
			isRunnable = false;
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}

		return isRunnable;
	}

	// Read Excel file for Script to run. Like Regression, Smoke, Functional
	/**
	 * <H1>Read Excel file for Script to run. Like Regression, Smoke,
	 * Functional</H1>
	 * 
	 * @param suiteName
	 * @param scriptName
	 * @return
	 */
	public static boolean isScriptRunnable(String suiteName, String scriptName) {
		boolean isRunnable = false;
		String strVal = null;
		try {
			strVal = getFlagExcel(suiteName, scriptName);
			if (strVal.equalsIgnoreCase(Y)) {
				isRunnable = true;
			} else {
				isRunnable = false;
			}
		} catch (Exception e) {
			isRunnable = false;
			LogUtil.errorLog(ExcelDataUtil.class, EXCEPTIONCAUGHT, e);
		}
		return isRunnable;
	}

	/**
	 * <H1>Print test status</H1> @param suiteName @param searchValue @param
	 * testStatus @exception
	 */

	/**
	 * <H1>Print test status</H1> @param suiteName @param testCaseID @exception
	 */
	public static boolean getControls(String suiteName, String testCaseID) {

		if (suiteName.trim().isEmpty()) {
			LogUtil.errorLog(ExcelDataUtil.class, "Suite name not found!!!");
			return false;
		}

		if (testCaseID.trim().isEmpty()) {
			LogUtil.errorLog(ExcelDataUtil.class, "Test name is not specified!!!");
			return false;
		}

		return ExcelDataUtil.isScriptRunnable(suiteName.trim(), testCaseID.trim());
	}

	// Get browsers List
	/**
	 * <H1>Get browsers List</H1>
	 * 
	 * @exception @return
	 */

}// End Excel class

class InvalidSheetException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	InvalidSheetException(String s) {
		super(s);
	}
}
