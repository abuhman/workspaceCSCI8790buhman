package ex04.toclass;

import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ToClass {
   public static void main(String[] args) {
      try {
    	  
    	 String[] classLines = new String[2];
    	 Scanner input = new Scanner(System.in);
    	 String classLine;
    	 while(classLines.length != 1)
    	 {
    		System.out.println("Please give a class:");
    	 	classLine = input.nextLine();
    	 	classLines = classLine.split(" ");
    	 	if(classLines.length != 1)
    	 	{
    	 		System.out.println("[WRN] Invalid Input");
    	 	}
    	 }
    	 String className = classLines[0];
    	 
    	 
         ClassPool cp = ClassPool.getDefault();
         CtClass cc = cp.get("target." + className);
         cc.defrost();

         CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
         
         declaredConstructor.insertAfter("{ " //
               + "System.out.println(\"[TR] After calling a constructor: id:\" + id + \" year: \" + year); }");

         Class<?> c = cc.toClass();
         c.newInstance();
      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      }
   }
}
