package ex02.setsuper;

import java.io.File;
import java.io.IOException;

import ex02.util.UtilMenu;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import target.Rectangle;

public class SetSuperclass {
   static String _S = File.separator;
   static String WORK_DIR = System.getProperty("user.dir");
   // static String CLASSPATH_DIR = WORK_DIR + _S + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + _S + "output";

   public static void main(String[] clazNames) {
      try {
         while (true) {
             UtilMenu.showMenuOptions();
             int option = UtilMenu.getOption();
             switch (option) {
             case 1:
         	   String superclass = "";
        	   String subclass = "";
        	   String[] clazNames1 = new String[1];
        	   while(clazNames1.length != 2)
        	   {
        		   if(option == 1)
        		   {
        			   	System.out.println("Enter two class names:");
               	   		clazNames1 = UtilMenu.getArguments();
               	   		if(clazNames1.length != 2)
               	   		{
               		   		System.out.println("[WRN] Invalid Input");
                 		    UtilMenu.showMenuOptions();
                		    option = UtilMenu.getOption();
               	   		}
        		   }
        		   else if(option == 2)
        		   {
        			   return;
        		   }
        	   }
               
               new File(OUTPUT_DIR).mkdirs();
               new File(OUTPUT_DIR + "/target").mkdirs();
               
               ClassPool pool = ClassPool.getDefault();
               
               String[] clazNames2 = clazNames1.clone();
               
               clazNames1[0] = "target." + clazNames1[0];
               clazNames1[1] = "target." + clazNames1[1];
               
               
               CtClass cc = pool.makeClass(clazNames1[0]);
               System.out.println("[DBG] make class: " + cc.getName());
               
               cc.writeFile(OUTPUT_DIR);
               System.out.println("[DBG] write output to: " + OUTPUT_DIR);

               CtClass cc2 = pool.makeClass(clazNames1[1]);
               System.out.println("[DBG] make class: " + cc2.getName());
               
               cc2.writeFile(OUTPUT_DIR);
               System.out.println("[DBG] write output to: " + OUTPUT_DIR);

               cc.defrost();
               System.out.println("[DBG] modifications of the class definition will be permitted.");
               
               cc2.defrost();
               System.out.println("[DBG] modifications of the class definition will be permitted.");
               
               if(clazNames2[0].startsWith("Common") && clazNames2[1].startsWith("Common"))
        	   {
        		   if(clazNames2[0].length() < clazNames2[1].length())
        		   {
        			   superclass = clazNames2[1];
        			   subclass = clazNames2[0];
        		   }
        		   else
        		   {
        			   superclass = clazNames2[0];
        			   subclass = clazNames2[1];
        		   }
        	   }
        	   else if(clazNames2[0].startsWith("Common"))
        	   {
        		   superclass = clazNames2[0];
        		   subclass = clazNames2[1];
        	   }
        	   else if(clazNames1[1].startsWith("Common"))
        	   {
        		   superclass = clazNames2[1];
        		   subclass = clazNames2[0];
        	   }
        	   else
        	   {
        		   superclass = clazNames2[0];
        		   subclass = clazNames2[1];
        	   }
               
               setSuperClass(subclass, superclass);
               break;
            default:
               break;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void setSuperClass(String clazSub, String clazSuper) {
      try {
         ClassPool pool = ClassPool.getDefault();
         insertClassPathRunTimeClass(pool);

         CtClass ctClazSub = pool.get("target." + clazSub);
         CtClass ctClazSuper = pool.get("target." + clazSuper);
         ctClazSub.setSuperclass(ctClazSuper);
         System.out.println("[DBG] set superclass: " //
               + ctClazSub.getSuperclass().getName() //
               + ", subclass: " + ctClazSub.getName());

         ctClazSub.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException {
      Rectangle rectangle = new Rectangle();
      Class<?> runtimeObject = rectangle.getClass();
      ClassClassPath classPath = new ClassClassPath(runtimeObject);
      pool.insertClassPath(classPath);
      System.out.println("[DBG] insert classpath: " + classPath.toString());
   }

   /*static void insertClassPath(ClassPool pool) throws NotFoundException {
      pool.insertClassPath(CLASSPATH_DIR);
      System.out.println("[DBG] insert classpath: " + CLASSPATH_DIR);
   }*/

   /*static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) //
         throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " //
            + curClass.getSuperclass().getName() //
            + ", subclass: " + curClass.getName());
   }*/
}
