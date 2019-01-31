package ex04.toclassB;

import java.util.Arrays;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ToClass {
   public static void main(String[] args) {
      try {
         // Hello orig = new Hello(); // java.lang.LinkageError
     	 Scanner input = new Scanner(System.in);  
     	 int option = 0;
     	 String methodToModify = "";
     	 boolean flag1 = false;
     	 boolean flag2 = false;
     	 boolean superclassFlag = false;
     	 String[] pastSelections = new String[10];
     	 int selectionIndex = 0;
     	 target.Rectangle h = null;
     	 Class<?> c = null;
     	 while(option != 3)
     	 {
     		System.out.println("Enter 1 to modify a method. Enter 3 to quit.");
     	 	option = input.nextInt();
     	 	if(option == 3)
     	 	{
     	 		break;
     	 	}
     	 	
     	 	System.out.println("Please enter a usage method, an increment method, and a getter method:");
    	 	String usage = input.next();
    	 	String increment = input.next();
    	 	String getter = input.next();

    	 	if(Arrays.asList(pastSelections).contains(usage))
    	 	{
    	 		System.out.println("[WRN] This method '" + usage + "' has been modified!!");
    	 		continue;
    	 	}
    	 	
    	 	pastSelections[selectionIndex] = usage;
    	 	selectionIndex++;
    	 	//scanner 2 options to modify 1 class
    	 	//if user selects first option, modify rectangle
    	 	//show menu again
    	 	//if user selects second option, modify rectangle again
    	 	//have to un-freeze it.

         	ClassPool cp = ClassPool.getDefault();
            CtClass rectangle = cp.get("target.Rectangle");
            rectangle.defrost();
            CtClass point = cp.get("target.Point");
            point.defrost();
            //if(superclassFlag == false)
            //{
            	rectangle.setSuperclass(point);
            	superclassFlag = true;
            //}
            rectangle.defrost();
         	CtMethod m = rectangle.getDeclaredMethod(usage);
         	//CtConstructor declaredConstructor = rectangle.getDeclaredConstructor(new CtClass[0]);
         	rectangle.defrost();
         	//String theString = String.format("{ %s();\nSystem.out.println(\"value: \" + %s());", increment, getter);
         	//System.out.println(theString);
         	String stmt = "{ " //
           		 + increment + "();\nSystem.out.println(\"value: \" + " + getter + "());}";
         	//System.out.println("[DBG] STMT: " + stmt);
         	//System.out.println(m);
         	m.insertAfter(stmt);
         	rectangle.defrost();
         	Object invoker = m.invoke(rectangle, new Object[] {});
         	if(c == null)
         	{
         		c = rectangle.toClass();
         	}
         	if(h == null)
         	{
         		h = (target.Rectangle) c.newInstance();
         	}
            if(usage.equals("add"))
            {
            	h.add();
            }
            else if(usage.equals("remove"))
            {
            	h.remove();
            }
         	//System.out.println("I got here.");
     	 }
     	 
      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      }
   }
}
