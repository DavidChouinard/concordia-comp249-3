import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookInventory2 {
	private static Book bkArr[];
	private static Scanner keyboard = new Scanner(System.in);
	// Change this to modify input file name
	private static final String FILENAME = "Sorted_Book_Info.txt";
	private static final String OUTPUT_FILENAME = "Books.dat";

	public static void main(String[] args) {
		String prompt = "Welcome to your book manager" + "\n\n" +
				"You can now add entries to your inventory. ";
		System.out.print(prompt);
		
		FileOutputStream outputStream = null;
		
		try {
			outputStream = new FileOutputStream(FILENAME, true);
		} catch (FileNotFoundException e) {
			System.out.print("Unable to find file. Quitting…");
			System.exit(0);
		}

		addRecords(outputStream);
	
		System.out.println("\n\nHere is the content of the file after the operations you performed: \n");
		
		try {
			displayFileContents(new FileReader(FILENAME));
		} catch (IOException e) {
			System.out.print("Unable to read file. Quitting…");
			System.exit(0);
		}
		
		try {
			populateArray();
		} catch (FileNotFoundException e) {
			System.out.print("Unable to read the input file to populate the array. Quitting…");
			System.exit(0);
		}
		
		System.out.print("\n\nEnter an ISBN to search for: ");
		
		long searchISBN = 0;
		try {
			searchISBN = keyboard.nextLong();
		} catch (InputMismatchException e) {
			System.out.print("You entered an invalid character. Quitting…");
			System.exit(0);
		}

		sequentialBookSearch(searchISBN);
		binaryBookSearch(searchISBN);

		try {
			saveToBinaryFile();
		} catch (IOException e1) {
			System.out.print("Unable to save the backup binary file. Quitting…");
			System.exit(0);
		}
		
		System.out.print("\n\nSuccessfully completed all operations. Quitting…");
		System.exit(0);
	}
	
	public static void addRecords(FileOutputStream outputStream) {
		PrintWriter output = new PrintWriter(outputStream);
		
		boolean done = false;
		
		while (!done) {
			
			System.out.println("Enter data for the new book: \n");
			
			long ISBN = 0;
			String title = null;
			int issueYear = 0;
			String authorNames = null;
			double price = 0;
			int nbOfPages = 0;
			
			try {
				System.out.print("ISBN: ");
				ISBN = keyboard.nextLong();
				
				System.out.print("Title: ");
				title = keyboard.next();
				 
				System.out.print("Issue Year: ");
				issueYear = keyboard.nextInt();
				
				System.out.print("Author names: ");
				authorNames = keyboard.next();
				
				System.out.print("Price: ");
				price = keyboard.nextDouble();
				
				System.out.print("Number of Pages: ");
				nbOfPages = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.out.print("You entered an invalid character. Quitting…");
				System.exit(0);
			}
			
			output.println(ISBN + " " + title + " " + issueYear + " " + authorNames + " "
					+ price + " " + nbOfPages);
			
			System.out.print("\nAdd another book? Enter 'y' for yes: ");
			
			if (!keyboard.next().equals("y")) {
				done = true;
			}			
		}
		output.close();		
	}
	
	public static void displayFileContents(FileReader inputStream) throws IOException {
		BufferedReader input = new BufferedReader(inputStream);
		
		String line = input.readLine();
		
		while (line != null) {
			// Output line by line
			System.out.println(line);
			line = input.readLine();
		}
		
		input.close();
	}
	
	private static void populateArray() throws FileNotFoundException {
		int inventorySize = 0;
		
		inventorySize = getInventorySize(new FileInputStream(FILENAME));
		
		if (inventorySize <= 1) {
			System.out.print("The input files has 0 or 1 lines, we can't help you. Quitting…");
			System.exit(0);
		}
		
		bkArr = new Book[inventorySize];
		
		Scanner input = new Scanner(new FileInputStream(FILENAME));
		
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
				System.out.print("The input file is malformed. Quitting…");
				System.exit(0);
			}
			
			bkArr[i] = new Book(ISBN, title, issueYear, authorNames, price, nbOfPages);
			input.nextLine();
			i++;
		}
	}
	
	private static int getInventorySize(FileInputStream inputStream) {
		Scanner input = new Scanner(inputStream).useDelimiter("\\A");
		int output = input.next().split("\\n").length;
		input.close();
		
		return output;
	}
	
	private static void sequentialBookSearch(long ISBN) {
		long endTime;
		long startTime = System.nanoTime();		
		
		for (int i = 0; i < bkArr.length; i++) {
			if(bkArr[i].getISBN() == ISBN) {
				endTime = System.nanoTime();
				System.out.println("Using sequential search, we found this book: " + bkArr[i]);
				System.out.println("Search completed in " + (i + 1) + " iterations and took " + (endTime - startTime) + " nanoseconds.");
				return;
			}
		}
		
		System.out.println("\nNo book found.");
		return;
	}
	
	private static void binaryBookSearch(long ISBN) {		
		long endTime;
		long startTime = System.nanoTime();	
		
		int low = 0;
		int middle;
		int high = bkArr.length - 1;
		int i = 0;

		while(low <= high) {
			i++;
			// This next line is not seen in most binary search implementation, it addresses an issue with integer overflow
			// See Joshua Bloch: http://googleresearch.blogspot.com/2006/06/extra-extra-read-all-about-it-nearly.html
			middle = (low + high) >>> 1; 
			if (ISBN > bkArr[middle].getISBN()){
				low = middle + 1;
			} else if (ISBN < bkArr[middle].getISBN()){
				high = middle - 1;
			} else {
				// found the element
				endTime = System.nanoTime();
				System.out.println("Using binary search, we found this book: " + bkArr[middle]);
				System.out.println("Search completed in " + i + " iterations and took " + (endTime - startTime) + " nanoseconds.");
				return;
			}
		}
		
		System.out.println("\nNo book found.");
		return;
	}

	public static void saveToBinaryFile() throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILENAME));
		output.writeObject(bkArr);
		output.close( );
	}
}
