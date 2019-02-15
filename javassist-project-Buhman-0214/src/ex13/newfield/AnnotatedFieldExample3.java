package ex13.newfield;

import java.io.File;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import target.Author;
import target.Column;
import target.Row;
import target.Table;
import util.UtilMenu;

public class AnnotatedFieldExample3 {
   static String workDir = System.getProperty("user.dir");
   static String inputDir = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";
   
   static String className = "target.ComponentApp";
   static String annotationName = "target.Column";
   static String[] arguments = new String[3];
   static int option = 0;

   public static void main(String[] args) {
      try {
    	  
    	  while(true)
    	  {
    		arguments = new String[3];
      		UtilMenu.showMenuOptions();
      		option = UtilMenu.getOption();
    	  while(arguments.length != 2)
    	  {

    		System.out.println("Please enter a class name and annotation name, for example, ComponentApp,Column or ServiceApp,Row");
          	arguments = UtilMenu.getArguments();
          	if(arguments.length != 2)
          	{
        	  System.out.println("[WRN] Invalid input size!!");
          	}
    	  }
    	  className = "target." + arguments[0];
    	  annotationName = "target." + arguments[1];
    	  
    	  
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(inputDir);
         
         CtClass ct = pool.get(className);
  	     CtField fields[] = ct.getDeclaredFields();
         //CtField cf = ct.getField("x");

         //process(ct.getAnnotations());
         System.out.println();
         for(int i = 0; i < fields.length; i++)
         {
        	 process(fields[i].getAnnotations());
         }
    	  }
      } catch (NotFoundException | ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   static void process(Object[] annoList) {
      for (int i = 0; i < annoList.length; i++) {
         if (annoList[i] instanceof Table && annotationName.contains(Table.class.getName())) {
        	 if(annoList[i + 1] instanceof Author)
        	 {
                 Author author = (Author) annoList[i + 1];
                 System.out.println("Name: " + author.name() + ", Year: " + author.year()); 
        	 }
         } else if (annoList[i] instanceof Column && annotationName.contains(Column.class.getName())) {
        	 if(annoList[i + 1] instanceof Author)
        	 {
                 Author author = (Author) annoList[i + 1];
                 System.out.println("Name: " + author.name() + ", Year: " + author.year()); 
        	 }
         } else if (annoList[i] instanceof Author && annotationName.contains(Author.class.getName())) {
        	 if(annoList[i + 1] instanceof Author)
        	 {
                 Author author = (Author) annoList[i + 1];
                 System.out.println("Name: " + author.name() + ", Year: " + author.year()); 
        	 }
         } else if (annoList[i] instanceof Row && annotationName.contains(Row.class.getName())) {
        	 if(annoList[i + 1] instanceof Author)
        	 {
                 Author author = (Author) annoList[i + 1];
                 System.out.println("Name: " + author.name() + ", Year: " + author.year()); 
        	 }
         }
      }
   }
}
