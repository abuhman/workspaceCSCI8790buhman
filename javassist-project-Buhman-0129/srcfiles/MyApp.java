import java.lang.reflect.Field;

public class MyApp {
   
   public static void main(String[] paramArrayOfString) throws Exception {
	  System.out.println("Hello world");
      System.out.println("1: " + paramArrayOfString[0]);
      System.out.println("2: " + paramArrayOfString[1]);
      System.out.println("Run...");
      MyApp localMyApp = new MyApp();
      localMyApp.foo();
      System.out.println("1) getClass() is used to show the name: " + localMyApp.getClass().getField(paramArrayOfString[1]).getName());
      System.out.println("2) .class is used to show the name: " + MyApp.class.getField(paramArrayOfString[1]).getName());
      System.out.println("3) Class.forName() is used to show the name: " + Class.forName("MyApp").getField(paramArrayOfString[1]).getName());
      System.out.println("Done.");
   }

   public void foo() {
      System.out.println("Called foo.");
   }
}
