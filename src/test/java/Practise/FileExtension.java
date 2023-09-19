package Practise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileExtension {

	public static void main(String[] args) throws FileNotFoundException {
		String aa="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		String fileString="D:\\Project\\tx_automate_web\\src\\test\\resources\\testData\\TestData.xlsx";
		File file=new File(fileString);
		FileInputStream fileInput=new FileInputStream(file);
		String fileExtension=fileString.substring(fileString.indexOf("."));
		System.out.println(fileString.substring(5));
		System.out.println(fileString.substring(5,10));
		System.out.println(fileString.substring(5,0));
		System.out.println(fileExtension);

	}

}
