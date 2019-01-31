package javassistloader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

public class JavassistLoaderExample {
private static final String WORK_DIR = System.getProperty("user.dir");
private static final String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
private static final String TARGET_POINT = "target.Point";
private static final String TARGET_RECTANGLE = "target.Rectangle";

public static void main(String[] args) {
	 Scanner input = new Scanner(System.in);  
	 String[] pastSelections = new String[10];
	 int option = 1;
	 int selectionIndex = 0;
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
	 	changeUsageMethod(usage, increment, getter);
	 }
//System.out.println();
// changeUsageMethod("add", "incX", "getX");
//changeUsageMethod("getVal", "move", "getX");
}

private static void changeUsageMethod(String uMethod, String iMethod, String gMethod) {
try {
	ClassPool cp = ClassPool.getDefault();
	cp.insertClassPath(INPUT_DIR);
	System.out.println("[DBG] insert classpath: " + INPUT_DIR);

	CtClass cc = cp.get(TARGET_RECTANGLE);
	cc.defrost();
	cc.setSuperclass(cp.get(TARGET_POINT));
	CtMethod m1 = cc.getDeclaredMethod(uMethod);
	m1.insertBefore("{ " //
				+ iMethod + "();" //
				+ "System.out.println(\"[TR] " + gMethod + " result : \" + " + gMethod + "()); }");

	Loader cl = new Loader(cp);
	Class<?> c = cl.loadClass(TARGET_RECTANGLE);
	Object rect = c.newInstance();
	System.out.println("[DBG] Created a Rectangle object.");

	Class<?> rectClass = rect.getClass();
	Method m = rectClass.getDeclaredMethod(uMethod, new Class[] {});
	System.out.println("[DBG] Called getDeclaredMethod.");
	Object invoker = m.invoke(rect, new Object[] {});
	System.out.println("[DBG] " + uMethod + " result: " + invoker);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

/*
* static void insertClassPath(ClassPool pool) throws NotFoundException { String
* strClassPath = WORK_DIR + File.separator + "classfiles";
* pool.insertClassPath(strClassPath);
* System.out.println("[DBG] insert classpath: " + strClassPath); }
*/
}

