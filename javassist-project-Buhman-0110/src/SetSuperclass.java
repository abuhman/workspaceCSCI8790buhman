import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
/*import javassist.ClassClassPath;*/
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
/*import target.Rectangle;*/

public class SetSuperclass {
   static String _S = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + _S + "output";
   //static String CLASSPATH_DIR = WORK_DIR + _S + "classfiles";
   
   public static void main(String[] args) {
	   String superclass = "";
	   String subclass = "";
	   if(args.length != 2)
	   {
		   return;
	   }
	   else if(args[0].startsWith("Common") && args[1].startsWith("Common"))
	   {
		   if(args[0].length() < args[1].length())
		   {
			   superclass = args[1];
			   subclass = args[0];
		   }
		   else
		   {
			   superclass = args[0];
			   subclass = args[1];
		   }
	   }
	   else if(args[0].startsWith("Common"))
	   {
		   superclass = args[0];
		   subclass = args[1];
	   }
	   else if(args[1].startsWith("Common"))
	   {
		   superclass = args[1];
		   subclass = args[0];
	   }
	   else
	   {
		   superclass = args[0];
		   subclass = args[1];
	   }
      try {
         ClassPool pool = ClassPool.getDefault();

         /*boolean useRuntimeClass = true;
         if (useRuntimeClass) {
            insertClassPathRunTimeClass(pool);
         } else {*/
         insertClassPath(pool);
         /*}*/

         subclass = "target." + subclass;
         superclass = "target." + superclass;
         CtClass cc = pool.get(subclass);
         setSuperclass(cc, superclass, pool);
         
         //
         //CtClass ctClazSub = pool.get(subclass);
         //CtClass ctClazSuper = pool.get(superclass);
         //ctClazSub.setSuperclass(ctClazSuper);
         //System.out.println("[DBG] set superclass: " //
         //      + ctClazSub.getSuperclass().getName() //
         //      + ", subclass: " + ctClazSub.getName());
         //

         cc.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   /*static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException {
      ClassClassPath classPath = new ClassClassPath(new Rectangle().getClass());
      pool.insertClassPath(classPath);
      System.out.println("[DBG] insert classpath: " + classPath.toString());
   }*/

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = workDir + _S + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }

   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
            ", subclass: " + curClass.getName());
   }
}
