import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class createClasses {
	
	public static void main(String[] args) {
		static String WORK_DIR = System.getProperty("user.dir");
		static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
		Scanner scanner = new Scanner(System.in);
		String[] inputs = new String[1];
		String input = "";
		while(inputs.length != 2)
		{
			System.out.println("Please input a subclass and superclass.");
    		input = scanner.nextLine();
    		inputs = input.split(" ");
    		System.out.println(Arrays.toString(inputs) );
    		if(inputs.length != 2)
    		{
    		    System.out.println("[WRN] Invalid Input");
    		}
		}
		new File(outputdir).mkdirs();
	}
    /*
    if (input < 0 || input > MENU_SIZE) {
       System.out.println("You have entered an invalid selection, please try again\n");
    } else if (input == MENU_QUIT) {
       System.out.println("You have quit the program\n");
       System.exit(1);
    } else {
       System.out.println("You have entered " + input + "\n");
       return input;
    }
    return 0;
*/
}
