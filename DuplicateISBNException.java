// -----------------------------------------------------
// Assignment 3
// Question: Part 1
// Written by: David Chouinard, 1695452
//
// Program to manage books in a bookstore
//
// -----------------------------------------------------

public class DuplicateISBNException extends Exception {

	private static final long serialVersionUID = -5755952900856958470L;

	public DuplicateISBNException() {
		super();
	}

	public DuplicateISBNException(String message) {
		super(message);
	}

}
