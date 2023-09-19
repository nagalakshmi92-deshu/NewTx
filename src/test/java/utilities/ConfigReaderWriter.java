
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This ConfigReader file will read the config file
 *
 */

public class ConfigReaderWriter {

	/**
	 * will read the properties file with this function
	 * 
	 * @param filePath
	 * @return
	 */
	private ConfigReaderWriter() {

	}

	public static Properties loadPropertyFile(String filePath) {
		// Read from properties file
		File file = new File(filePath);
		Properties prop = new Properties();

		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
			prop.load(fileInput);
		} catch (Exception e) {
			LogUtil.errorLog(ConfigReaderWriter.class, "Caught the exception", e);
		}
		return prop;

	}

	/**
	 * will get sting value from properties file
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		String valueFromConfigReader = null;
		Properties prop;
		try {
			prop = loadPropertyFile("src/main/resources/Config/config.properties");
			valueFromConfigReader = prop.getProperty(key);
			LogUtil.infoLog(ConfigReaderWriter.class, "Key is: " + key + " , Value is: " + valueFromConfigReader);
		} catch (Exception e) {
			LogUtil.errorLog(ConfigReaderWriter.class, e.getMessage());
			e.printStackTrace();
		}
		return valueFromConfigReader;
	}

	/**
	 * will get int value from properties file
	 * 
	 * @param key
	 * @return
	 */
	public static int getIntValue(String key) {
		String valueFromConfigReader = null;
		int convertedStringtoInt = 0;
		Properties prop;
		try {
			prop = loadPropertyFile("src/main/resources/Config/config.properties");
			valueFromConfigReader = prop.getProperty(key);
			convertedStringtoInt = Integer.parseInt(valueFromConfigReader);
			LogUtil.infoLog(ConfigReaderWriter.class, "Key is: " + key + " , Value is: " + convertedStringtoInt);
		} catch (Exception e) {
			LogUtil.errorLog(ConfigReaderWriter.class, e.getMessage());
			e.printStackTrace();
		}

		return convertedStringtoInt;
	}

	public static void updateKeyValue(String keyname, String updatedValue) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("src/main/resources/Config/config.properties");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Properties prop = new Properties();
		try {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream("src/main/resources/Config/config.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (updatedValue != null) {
			prop.setProperty(keyname, updatedValue);
			LogUtil.infoLog(ConfigReaderWriter.class, "Key is: " + keyname + " , Value is: " + updatedValue);
		} else {
			updatedValue = " ";
			prop.setProperty(keyname, updatedValue);
			LogUtil.errorLog(ConfigReaderWriter.class, "Updated Value in key: " + keyname + " is blank.");
		}
		try {
			prop.store(out, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
