package android.example.booklisting;

/**
 * {@Link Book} represents a Google Book the user wants to view information about.
 * It contains attributes about the Book such as the title and author(s)
 */
public class Book {

    /** title of the Book */
    private String mTitle;

    /** author of the Book */
    private String mAuthor;

    /**
     * Create a new Book object
     */
    public Book(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    /** get Book title */
    public String getTitle() {return mTitle;}

    /** get Book author */
    public String getAuthor() {return mAuthor;}
}
