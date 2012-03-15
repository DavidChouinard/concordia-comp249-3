import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookInventory1 {

	private static Book bkArr[];
	private static Scanner keyboard = new Scanner(System.in);
	
	public static void main(String[] args) {
		// Change this to modify input file name
		final String INPUT_FILENAME = "Initial_Book_Info.txt";
		
		String prompt = "Welcome to your book manager" + "\n\n" +
				"Please enter the name of the output file, which will have the corrected information: ";
		System.out.print(prompt);

		String filename = keyboard.nextLine();
		
		File fileObject = new File(filename);

		// Although not a requirement, we might as well check that we can write to that file
		while(fileObject.exists() && fileObject.canWrite()) {
			prompt = "There are already exists a file called: " + filename + ".\n" +
					"That file has a size of " + fileObject.length() + " bytes." + "\n\n" +
					"Please enter another file name to create: ";
			System.out.print(prompt);
			
			filename = keyboard.nextLine();
			fileObject = new File(filename);
		}
		
		int inventorySize = 0;
		
		try {
			inventorySize = getInventorySize(new FileInputStream(INPUT_FILENAME));
		} catch (IOException e) {
			System.out.print("Unable to count inventory in input file. Quitting…");
			System.exit(0);
		}
		
		if (inventorySize <= 1) {
			System.out.print("The input files has 0 or 1 lines, we can't help you. Quitting…");
			System.exit(0);
		}
		
		System.out.println("The input file has " + inventorySize + " records.");
		
		bkArr = new Book[inventorySize];

		try {
			fixInventory(new FileInputStream(INPUT_FILENAME), new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.print("Unable to fix inventory. Quitting…");
			System.exit(0);
		}

		try {
			System.out.println("\nHere is the current contents of the input file:\n");
			System.out.println(displayFileContents(new FileInputStream(INPUT_FILENAME)));
			
			System.out.println("\nHere is the current contents of the output file:\n");
			System.out.println(displayFileContents(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.print("Unable to output contents of file. Quitting…");
			System.exit(0);
		}
	}
	
	public static void fixInventory(FileInputStream inputStream, FileOutputStream outputStream) {
		Scanner input = new Scanner(inputStream);
		PrintWriter output = new PrintWriter(outputStream);
		
		int i = 0;
		while (input.hasNextLine()) {
			long ISBN = 0;
			String title = null;
			int issueYear = 0;
			String authorNames = null;
			double price = 0;
			int nbOfPages = 0;			
			
			try {
				ISBN = input.nextLong();
				title = input.next();
				issueYear = input.nextInt();
				authorNames = input.next();
				price = input.nextDouble();
				nbOfPages = input.nextInt();
			} catch (InputMismatchException e) {
				input.close();
				output.close();
				System.out.print("The input file is malformed. Quitting…");
				System.exit(0);
			}
			
			bkArr[i] = new Book(ISBN, title, issueYear, authorNames, price, nbOfPages);
			input.nextLine();
			i++;
		}

		for (i = 0; i < bkArr.length; i++) {
			if (isDuplicate(bkArr[i].getISBN(), i)) {
				System.out.print("The book " + bkArr[i].getTitle() + " has the same ISBN (" + bkArr[i].getISBN() + 
						") as another book. Enter the correct ISBN: ");
				long newISBN = keyboard.nextLong();
				
				boolean done = false;
				while (!done) {
					try {
						if (isDuplicate(newISBN, i)) {
							throw new DuplicateISBNException();
						}
						done = true;
					} catch (DuplicateISBNException e) {
						System.out.print("Your entered ISBN is still a duplicate. Enter the correct ISBN: ");
						newISBN = keyboard.nextLong();
					}
				}
				bkArr[i].setISBN(newISBN);
			}
			// All good, let's write that book to the output file
			output.println(bkArr[i]);
		}
		
		input.close();
		output.close();
		System.out.println("\nSucess, we've populated the output file.");
	}
	
	public static String displayFileContents(FileInputStream inputStream) {
		// This is a pretty clever trick: we set the token delimiter to a regular
		// expression matching the end of the file. Thus, the first and only token
		// is the entire file.
		
		Scanner input = new Scanner(inputStream).useDelimiter("\\A");
		String output = input.next();
		input.close();
		
		return output;
	}
	
	private static int getInventorySize(FileInputStream inputStream) {
		// We split the file into arrays (delimited by newlines) and return the size of the array 
		return displayFileContents(inputStream).split("\\n").length;
	}
	
	// Search up to, but excluding, maxIndex
	private static boolean isDuplicate(long otherISBN, int maxIndex) {
		for (int i = 0; i < maxIndex; i++) {
			if(bkArr[i].getISBN() == otherISBN) {
				return true;
			}
		}
		
		return false;
	}

}
