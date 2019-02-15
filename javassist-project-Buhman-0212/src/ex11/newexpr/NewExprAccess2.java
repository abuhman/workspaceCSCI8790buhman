package ex11.newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess2 extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
   static final String TARGET_MY_APP2 = "target.MyAppField";
   static String _L_ = System.lineSeparator();
   static String[] arguments = new String[3];
   
   static String className = "MyAppField";
   static int numIterations = 2;

   public static void main(String[] args) throws Throwable {
	  while(arguments.length != 2)
	  {
		System.out.println("Please enter a class name and number of fields, for example, ComponentApp,1 or ServiceApp,100");
      	arguments = UtilMenu.getArguments();
      	if(arguments.length != 2)
      	{
    	  System.out.println("[WRN] Invalid input size!!");
      	}
	  }
      className = arguments[0];
      numIterations = Integer.parseInt(arguments[1]);
      NewExprAccess2 s = new NewExprAccess2();
      Class<?> c = s.loadClass("target." + className);
      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
      mainMethod.invoke(null, new Object[] { args });
   }

   private ClassPool pool;

   public NewExprAccess2() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(CLASS_PATH); // TARGET must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
	   CtClass cc = null;
	   try {
	   cc = pool.get(name);
	   cc.instrument(new ExprEditor() {
	   public void edit(NewExpr newExpr) throws CannotCompileException {
	   try {
	   CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
	   if (fields.length < numIterations)
	   {
		   numIterations = fields.length;
	   }
       //String block1 = "{ " + _L_;
       String block1 = "\n{";
	   block1 = block1 + "\nCtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();";
	   for (int i = 0; i < numIterations; i++) {
		   String ctFieldName = fields[i].getName();
		   String ctFieldType = fields[i].getType().getName();
		   block1 = block1 + "\nString ctFieldName = fields[i].getName();";
	   	   block1 = block1 + "\nString ctFieldType = fields[i].getType().getName();";
		   block1 = block1 + "\n$_ = $proceed($$);"
		   +  "\nString cName = $_.getClass().getName();"
		   +  "\nString fName = $_.getClass().getDeclaredFields()[i].getName();"
		   +  "\nString fieldFullName = cName + \".\" + fName;"
		   +  "\n" + ctFieldType + " fieldValue = $_." + ctFieldName + ";"//"// + fName;";//(Object)$0." + fieldName + ";"//getClass().getDeclaredFields()[0].getName();"
		   +  "\nSystem.out.println(\" [Instrument] \" + fieldFullName + \": \");";// + $0." + fieldName + ");"//fieldValue);"
	   }
	   block1 = block1 + "\n}";
       System.out.println(block1);
       newExpr.replace(block1.toString());
	   } catch (Exception e) {
	   e.printStackTrace();
	   }
	   }
	   });
	   byte[] b = cc.toBytecode();
	   return defineClass(name, b, 0, b.length);
	   } catch (NotFoundException e) {
	   throw new ClassNotFoundException();
	   } catch (IOException e) {
	   throw new ClassNotFoundException();
	   } catch (CannotCompileException e) {
	   e.printStackTrace();
	   throw new ClassNotFoundException();
	   }
	   }
}