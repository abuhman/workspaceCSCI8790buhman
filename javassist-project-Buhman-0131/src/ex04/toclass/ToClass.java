package ex04.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import target.Hello;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import ex04.util.UtilMenu;

public class ToClass {
   static String[] arguments = new String[2];
   static String _S = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + _S + "output";
   public static void main(String[] args) {
      try {
         // Hello orig = new Hello(); // java.lang.LinkageError
  	 	 Scanner input = new Scanner(System.in);
  	 	 String inLine = "";
    	 while(arguments.length != 3)
    	 {
    		 System.out.println("Enter a class name and two field names separated by a comma.  For example (target.CommonServiceA, idA, nameA)");
    		 inLine = input.nextLine();    	 
    	 	 arguments = inLine.split(", ");//UtilMenu.getArguments();
    	 	 if(arguments.length != 3)
    	 	 {
    	 		 System.out.println("[WRN] Invalid Input");
    	 	 }
    	 }
    	 
    	 String className = arguments[0];
    	 String field1 = arguments[1];
    	 String field2 = arguments[2];
         ClassPool cp = ClassPool.getDefault();
         CtClass cc = cp.get(className);
         //CtMethod m = cc.getDeclaredMethod("say");
         //m.insertBefore("{ System.out.println(\"[TR] Hello.say: \" + i); }");

         CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
         /*String myString = "{ \n"
        		 + field1 + " = 92253197;\n"
        		 + field2 + " = \"Buhman\";\n"
        		 + "System.out.println(\"[TR] After calling a constructor: \" + field1 + ": \" + " + field1 + " + \" " + field2 + ": \" + " + field2 + " + \" "); }";*/

         //String myString2 = String.format("{\n %s = 92253197;\n %s = \"Buhman\"; \nSystem.out.println(\"[TR] After calling a constructor: %s: \" + %s + \" %s: \" + %s);}", field1, field2, field1, field1, field2, field2);
         String myString2 = String.format("{\nSystem.out.println(\"[TR] After calling a constructor: %s: \" + %s + \" %s: \" + %s);}", field1, field1, field2, field2);

         //System.out.println(myString2);                  
         declaredConstructor.insertAfter(myString2);

         Class<?> c = cc.toClass();
         c.newInstance();
         cc.writeFile(outputDir);
         //Hello h = (Hello) c.newInstance();
         //h.say();
      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException | IOException e) {
         System.out.println(e.toString());
      }
   }
}
