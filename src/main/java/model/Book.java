package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;

/**
 * Class containing methods for creating,
 * validating and handling books
 * 
 * @author Joel
 */
public class Book {
    
    private int bookId;
    private String isbn;
    private final String title;
    private final Date published;
    private String storyLine = "";
    private final Genre genre;
    private int rating;
    private static final Pattern ISBN_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^((19|2[0-9])"
            + "[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
    private List<Author> authors = new ArrayList();
    
    public Book(int bookId, String title, String isbn, String stroyLine, Genre genre, String published) {
        this.bookId = bookId;
        this.title = title;
        this.storyLine = stroyLine;
        this.genre = genre;
        if (!isValidDate(published)){
            Alert alert = new Alert(Alert.AlertType.ERROR, ("Illegal Date: " + published));
            alert.showAndWait();
            throw new IllegalArgumentException("Illegal Date: " + published);
        }
        this.published = Date.valueOf(published);
        if (!isValidIsbn(isbn)){
            Alert alert = new Alert(Alert.AlertType.ERROR, ("Illegal Isbn: " + isbn));
            alert.showAndWait();
            throw new IllegalArgumentException("Illegal Isbn: " + isbn);
        }
        this.isbn = isbn;   
    }
    
    /**
     * Adds author to author arraylist
     * @param a
     */
    public void setAuthorToList(Author a){
        authors.add(a);
    }
    
    /**
     * checks if the string from user input
     * is a valid ISBN
     */
    public static boolean isValidIsbn(String isbn) {
        return ISBN_PATTERN.matcher(isbn).matches();
    }
    
    /**
     * checks if the string from user input
     * is a valid date
     */
    public static boolean isValidDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }
    
    /**
     * Adds a new author into the author arraylist
     * @param author
     */
    public void addAuthor(Author author){
        authors.add(author);
    }
    
    /**
     * @return copy of author arraylist
     */
    public List<Author> getAuthors(){
        List<Author> copy = new ArrayList<>();
        for(int i = 0; i < authors.size(); i++){
            copy.add(authors.get(i));
        }
        return copy;
    }
    
    /**
     * Set methods for Book characteristics
     */
    public void setRating(int rating){ this.rating = rating; }
    public void setBookId(int bookId){ this.bookId = bookId; }
    public void setStoryLine(String storyLine) { this.storyLine = storyLine; }
    
    /**
     * Get methods for Book characteristics
     */
    public int getBookId() { return bookId; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public Date getPublished() { return published; }
    public String getStoryLine() { return storyLine; }
    public int getRating() { return rating; }
    public Genre getGenre(){ return genre; }
    
    @Override
    public String toString() {
        return "BookId: "+bookId+"\n"+"Title: "+title+"\n"+"ISBN: "+isbn+"\n"+
                "Genre: "+genre+"\n"+"Rating: "+rating+"\n"+"Published: "+published.toString();
    }
}