package assignment1;

import java.util.*;

public class DataChangeUI {

	public static void main(String[] args) {
		// Define variables to manage user input

		String userCommand = "";
		String userArgument = "";
		Scanner userInput = new Scanner( System.in );

		// Define variables to catch the return values of the transformation methods

		boolean booleanOutcome;
		Integer integerOutcome;
	
		// Define a single data transformation object.
	
		DataTransformer data = new DataTransformer();

		// Process the user input until they provide the command "quit"
		
		do {
			// Find out what the user wants to do
			userCommand = userInput.next();
			userArgument = userInput.nextLine();
			userArgument = userArgument.trim();
			
			// Include a "hack" to provide null and empty strings for testing
			if (userArgument.equalsIgnoreCase("blank")) {
				userArgument = "";
			} else if (userArgument.equalsIgnoreCase("null")) {
				userArgument = null;
			}
			
			/* Do what the user asked for. */
			
			if (userCommand.equalsIgnoreCase("clear")) {
				booleanOutcome = data.clear( );
				System.out.println("Clear \""+userArgument+"\" outcome " + booleanOutcome );
			} else if (userCommand.equalsIgnoreCase("read")) {
				integerOutcome = data.read( userArgument );
				System.out.println("Read \""+userArgument+"\" outcome " + integerOutcome );
			} else if (userCommand.equalsIgnoreCase("new")) {
				booleanOutcome = data.newColumn( userArgument );
				System.out.println("new column \""+userArgument+"\" outcome " + booleanOutcome );
			} else if (userCommand.equalsIgnoreCase("calculate")) {
				integerOutcome = data.calculate( userArgument );
				System.out.println("Calculate \""+userArgument+"\" outcome " + integerOutcome );
			} else if (userCommand.equalsIgnoreCase("top")) {
				data.top( );
			} else if (userCommand.equalsIgnoreCase("print")) {
				data.print( );
			} else if (userCommand.equalsIgnoreCase("write")) {
				integerOutcome = data.write( userArgument );
				System.out.println("Write \""+userArgument+"\" outcome " + integerOutcome );
			} else if (userCommand.equalsIgnoreCase("quit")) {
				System.out.println ("Quit");
			} else {
				System.out.println ("Bad command: " + userCommand);
			}
		} while (!userCommand.equalsIgnoreCase("quit"));
	
		// The user is done so close the stream of user input before ending.
	
		userInput.close();
	}
}

