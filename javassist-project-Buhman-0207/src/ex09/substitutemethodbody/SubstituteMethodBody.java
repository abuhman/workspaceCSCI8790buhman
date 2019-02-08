package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
   static final String WORK_DIR      = System.getProperty("user.dir");
   static final String INPUT_PATH    = WORK_DIR + File.separator + "classfiles";

   static String TARGET_MY_APP = "target.MyApp";
   static final String MOVE_METHOD   = "move";
   static final String DRAW_METHOD   = "draw";
   static final String FILL_METHOD   = "fill";
   static String PARAMETER_INDEX  = "1";
   static String PARAMETER_VALUE = "0";

   static String _L_ = System.lineSeparator();
   
   public static void main(String[] args) throws Throwable {
	   String[] arguments = new String[3];
	   int option = 0;
	   String[] alreadyModified = new String[10];
	   int modifiedIndex = 0;
	   while(option != 2)
	   {
		   UtilMenu.showMenuOptions();
		   option = UtilMenu.getOption();
		   switch(option)
		   {
			   case 1:
				  System.out.println("Please enter a class name, a method name, a parameter index, and an assigned value.  For example ComponentApp,move,1,0 or ServiceApp,fill,2,10");
				  arguments = UtilMenu.getArguments();
				  if(arguments.length == 4)
				  {
					if(Arrays.asList(alreadyModified).contains(arguments[0]))
				  	{
					  System.out.println("[WRN] This method '" + arguments[0] + "' has been modified!!");
				  	}
				  	else
				  	{
					  alreadyModified[modifiedIndex] = arguments[0];
					  modifiedIndex++;
					  
					  SubstituteMethodBody s = new SubstituteMethodBody();
					  TARGET_MY_APP = "target." + arguments[0];
					  PARAMETER_INDEX = arguments[2];
					  PARAMETER_VALUE = arguments[3];
					  Class<?> c = s.loadClass(TARGET_MY_APP);
					  Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
					  mainMethod.invoke(null, new Object[] { args });
				  	}
				  }
				  else
				  {
					  System.out.println("[WRN] Invalid input size!!");
				  }
				  break;
			   case 2:
				  break;
		   }
	   }

   }

   private ClassPool pool;

   public SubstituteMethodBody() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(INPUT_PATH); // "target" must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
               String className = m.getClassName();
               String methodName = m.getMethodName();

               if (className.equals(TARGET_MY_APP) && methodName.equals(DRAW_METHOD)) {
                  System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
                  String block1 = "{" + _L_ //
                        + "System.out.println(\"Before a call to " + methodName + ".\"); " + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "System.out.println(\"After a call to " + methodName + ".\"); " + _L_ //
                        + "}";
                  System.out.println("[DBG] BLOCK1: " + block1);
                  System.out.println("------------------------");
                  m.replace(block1);
               } else if (className.equals(TARGET_MY_APP) && methodName.equals(MOVE_METHOD)) {
                  System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
                  String block2 = "{" + _L_ //
                        + "System.out.println(\"\tReset param.\"); " + _L_ //
                        + "$" + PARAMETER_INDEX + " = " + PARAMETER_VALUE + "; " + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "}";
                  System.out.println("[DBG] BLOCK2: " + block2);
                  System.out.println("------------------------");
                  m.replace(block2);
               }
               else if (className.equals(TARGET_MY_APP) && methodName.equals(FILL_METHOD)) {
                   System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
                   String block3 = "{" + _L_ //
                         + "System.out.println(\"\tReset param.\"); " + _L_ //
                         + "$" + PARAMETER_INDEX + " = " + PARAMETER_VALUE + "; " + _L_ //
                         + "$proceed($$); " + _L_ //
                         + "}";
                   System.out.println("[DBG] BLOCK3: " + block3);
                   System.out.println("------------------------");
                   m.replace(block3);
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
