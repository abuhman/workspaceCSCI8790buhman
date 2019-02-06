package ex07.insertmethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
//import target.Hello;
import util.UtilFile;
import util.UtilMenu;

public class InsertMethodBody {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   static String _L_ = System.lineSeparator();

   public static void main(String[] args) {
	   String[] arguments = new String[3];
	   while(true)
	   {
		   UtilMenu.showMenuOptions();
		   int option = UtilMenu.getOption();
		   switch(option)
		   {
			   case 1:
				  System.out.println("Please enter a class name, a method name, and a parameter index.  For example ComponentApp,foo,1");
				  arguments = UtilMenu.getArguments();
				  update(arguments[0], arguments[1], arguments[2]);
				  break;
			   case 2:
				  break;
		   }
	   }
   }
   
   public static void update(String classname, String methodName, String index)
   {
      try {
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(INPUT_DIR);
         CtClass cc = pool.get("target." + classname);
         cc.defrost();
         CtMethod m = cc.getDeclaredMethod(methodName);
         String block1 = "{ " + _L_ //
               + "System.out.println(\"[DBG] param1: \" + $" + index +"); " + _L_ //
               //+ "System.out.println(\"[DBG] param2: \" + $2); " + _L_ + //
               + "}";
         System.out.println(block1);
         m.insertBefore(block1);
         cc.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
         System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
         String[] args2 = new String[1];
         
        /* Loader cl = new Loader(pool);
         Class<?> c = cl.loadClass("target." + classname);
         Object rect = c.newInstance();
         System.out.println("[DBG] Created an object.");

         Class<?> rectClass = rect.getClass();
         Method m2 = rectClass.getDeclaredMethod(methodName, new Class[] {});
         System.out.println("[DBG] Called getDeclaredMethod.");
         Object invoker = m2.invoke(rect, new Object[] {});
         System.out.println("[DBG] method result: " + invoker);*/
         Class<?> c = cc.toClass();
         Object h = c.newInstance();
         if( h instanceof target.ComponentApp )
         {
             target.ComponentApp newThing = (target.ComponentApp) c.newInstance();
             newThing.main(args2);
         }
         else if(h instanceof target.ServiceApp)
         {
             target.ServiceApp newThing = (target.ServiceApp) c.newInstance();
             newThing.main(args2);        	 
         }
         //h.main();
      } catch (NotFoundException | CannotCompileException | IOException 
    		  | IllegalAccessException | InstantiationException e) {
         e.printStackTrace();
      }
   }
}
