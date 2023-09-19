package Practise;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Keywordutil {

	public static String currentDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dt = dtf.format(now).replace("/", "-").replace(":", "-").replace(" ", "_");
		return dt;
		//Current Date and Time : 2023-09-11_21-37-22
	}
	
	public static void main(String args[])
	{
		String current=currentDateTime();
		System.out.println("Current Date and Time : "+current);
		//Current Date and Time : 2023-09-11_21-37-22
	}
}
