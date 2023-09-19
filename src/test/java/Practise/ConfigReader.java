package Practise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import utilities.LogUtil;

public class ConfigReader {
	
	public static String getValueAndChange(String fileName, String key,String newOne)
	{
		String valueFromConfigReader="";
		try {
			FileInputStream fis=new FileInputStream(new File(fileName));
			Properties pro=new Properties();
			pro.load(fis);
			
			valueFromConfigReader=pro.getProperty(key);
			System.out.println("Value before change : "+valueFromConfigReader);
			
			FileOutputStream fos=new FileOutputStream(new File(fileName));
			
			if(newOne!=null)
			{
				pro.setProperty(key, newOne);
			}
			else 
			{
				newOne="";
				pro.setProperty(key, newOne);
			}
			
			pro.store(fos, null);
		
		} catch (Exception e) {
		
			LogUtil.errorLog(ConfigReader.class, " Caught the exception "+e.getMessage());
			e.printStackTrace();
		}
		
		return newOne;		
	}

	public static void main(String[] args) {
	
		String value=getValueAndChange("src/test/java/Practise/config.properties","HusbandName","suman Lagisetty");
		//String value=read.getValue("src\\test\\java\\Practise\\config.properties","nameOfHer");
		System.out.println(value);
	}

}
