import java.io.Serializable;

public class Book implements Serializable {
	private static final long serialVersionUID = -2171192255227758615L;
	private long ISBN;
	private String title;
	private int issueYear;
	private String authorNames;
	private double price;
	private int nbOfPages;
	
	public Book() {
		this(0, "", 0, "", 0, 0);
	}
	
	public Book(long ISBN, String title, int issueYear, String authorNames, double price, int nbOfPages) {
		super();
		this.ISBN = ISBN;
		this.title = title;
		this.issueYear = issueYear;
		this.authorNames = authorNames;
		this.price = price;
		this.nbOfPages = nbOfPages;
	}

	@Override
	public String toString() {
		return ISBN + " " + title + " "
				+ issueYear + " " + authorNames + " "
				+ price + " " + nbOfPages;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (ISBN != other.ISBN)
			return false;
		if (authorNames == null) {
			if (other.authorNames != null)
				return false;
		} else if (!authorNames.equals(other.authorNames))
			return false;
		if (issueYear != other.issueYear)
			return false;
		if (nbOfPages != other.nbOfPages)
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public long getISBN() {
		return ISBN;
	}

	public void setISBN(long ISBN) {
		this.ISBN = ISBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIssueYear() {
		return issueYear;
	}

	public void setIssueYear(int issueYear) {
		this.issueYear = issueYear;
	}

	public String getAuthorNames() {
		return authorNames;
	}

	public void setAuthorNames(String authorNames) {
		this.authorNames = authorNames;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getNbOfPages() {
		return nbOfPages;
	}

	public void setNbOfPages(int nbOfPages) {
		this.nbOfPages = nbOfPages;
	}

}
