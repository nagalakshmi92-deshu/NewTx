package Practise;

public class ClassName {

	public static void main(String[] args) {
		//System.out.println(ClassName.class);     //output:     class Practise.ClassName

		try
		{
			int i=9;
			int dic=i/0;
		}
		catch(Exception e)
		{
			System.out.println(e.getClass());   //class java.lang.ArithmeticException
			System.out.println(e.getMessage());  /// by zero
			System.out.println(e.getLocalizedMessage());  // by zero
			System.out.println(e.getCause());   //null
			System.out.println(e.getStackTrace());   //[Ljava.lang.StackTraceElement;@5acf9800
		}
	}

}
/*
class java.lang.ArithmeticException
/ by zero
/ by zero
null
[Ljava.lang.StackTraceElement;@5acf9800
*/