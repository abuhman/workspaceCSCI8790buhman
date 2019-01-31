package classloader;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import util.UtilMenu;

public class SampleLoader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String TARGET_APP = "MyApp";
   private ClassPool pool;
   static String[] arguments = new String[2];

   public static void main(String[] args) throws Throwable {
	  System.out.println("Enter an application class name and a field name.");
	  arguments = UtilMenu.getArguments();
      SampleLoader s = new SampleLoader();
      TARGET_APP = arguments[0];
      Class<?> c = s.loadClass(TARGET_APP);
      c.getDeclaredMethod("main", new Class[] { String[].class }). //
            invoke(null, new Object[] { arguments });
   }

   public SampleLoader() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
   }

   /* 
    * Find a specified class, and modify the bytecode.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         CtClass cc = pool.get(arguments[0]);
         if (name.equals("MyApp")) {
            CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
        	//System.out.println("Adding " + arguments[1]);
        	//CtField f = new CtField(CtClass.intType, arguments[1], cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f);
         }
         else
         {
         //CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
     	    System.out.println("Adding " + arguments[1]);
     	    CtField f = new CtField(CtClass.intType, arguments[1], cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f);
            /* CtField f = new CtField(CtClass.intType, arguments[1], cc);
             f.setModifiers(Modifier.PUBLIC);
             System.out.println("Inside findClass:");
             System.out.println(f);
             System.out.println(cc);
             cc.addField(f);
             System.out.println(cc);*/
             
         }
         byte[] b = cc.toBytecode();
         return defineClass(arguments[0], b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         throw new ClassNotFoundException();
      }
   }
}
