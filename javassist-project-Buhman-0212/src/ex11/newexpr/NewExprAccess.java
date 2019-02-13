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

public class NewExprAccess extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
   static final String TARGET_MY_APP2 = "target.MyAppField";
   static String _L_ = System.lineSeparator();
   static String[] arguments = new String[3];
   
   static String className = "MyAppField";
   static String numIterations = "2";

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
      numIterations = arguments[1];
      NewExprAccess s = new NewExprAccess();
      Class<?> c = s.loadClass("target." + className);
      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
      mainMethod.invoke(null, new Object[] { args });
   }

   private ClassPool pool;

   public NewExprAccess() throws NotFoundException {
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
         cc = pool.get("target." + className);
         cc.instrument(new ExprEditor() {
            public void edit(NewExpr newExpr) throws CannotCompileException {
               try {
                  String longName = newExpr.getConstructor().getLongName();
                  if (longName.startsWith("java.")) {
                     return;
                  }
               } catch (NotFoundException e) {
                  e.printStackTrace();
               }
               //String fieldName = newExpr.getFieldName();
               String log = String.format(
                     "[Edited by ClassLoader] new expr: %s, " //
                           + "line: %d, signature: %s",
                     newExpr.getEnclosingClass().getName(), //
                     newExpr.getLineNumber(), newExpr.getSignature());
               System.out.println(log);

               String block1 = "{ " + _L_; //
                     //+ "  $_ = $proceed($$);" + _L_ //
                     //+ "  System.out.println(" + "\"new expr: \" + " //
                     //+ "  $_.getClass().getName() + " + "\", \" );" + _L_; //
              /* for(int i = 0; i < 2; i++)
               {
            	   block1 = block1 + "System.out.println($_.getClass().getDeclaredFields()[" + i + "].getName());\n";
               }*/
               /*block1 = block1 
               + "for(int i = 0; i < 2; i++)"
               + "{";
               
            	   block1 = block1
                     + "  $_ = $proceed($$);"
                     + "  System.out.println($_.getClass().getDeclaredFields()[i].getName());" //
                     //+ "  System.out.println($0.$_);"
                     //+ " + \": \"  + " + "$0.getClass().getDeclaredFields()[i].getName() + " + "\", \" );" + _L_ //
                     + "}";*/
               	   CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
               	   String fieldName = fields[0].getName();
               	   //String ctFieldType = fields[0].getType.getName();
                   block1 = block1 
                   //+ "CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();"
                   + "for(int i = 0; i < " + numIterations + "; i++)"
                   + "{"; 
                   
            	   block1 = block1 + "$_ = $proceed($$);"
            	   + "\n{"
            	   //+ "\nString fieldName = fields[i].getName();"
            	   //+ "\nString ctFieldType = fields[0].getType.getName();"
            	   //+  "\nString cName = $_.getClass.getName();"
            	   +  "\nString cName = $_.getClass().getName();"
            	   +  "\nString fName = $_.getClass().getDeclaredFields()[i].getName();"
            	   +  "\nString fieldFullName = cName + \".\" + fName;"
            	   //+  "\nObject fieldValue = (Object)$0." + fieldName + ";"//getClass().getDeclaredFields()[0].getName();"
            	   +  "\nSystem.out.println(\" [Instrument] \" + fieldFullName + \": \");"// + $0." + fieldName + ");"//fieldValue);"
            	   + "\n}";
                   //  + "  System.out.println($_.getClass().getDeclaredFields()[1].getName()" //
                   //  + " + \": \"  + " + "$_.y);" + _L_ //*/
               block1 = block1 + "\n}";
               block1 = block1 + "}";
               
               
               
               
               
               
               
               
               
               
               
               
               
               
               System.out.println(block1);
               newExpr.replace(block1.toString());
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