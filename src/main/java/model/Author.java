package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;

/**
 * Class containing methods for creating,
 * validating and handling authors
 * 
 * @author Joel
 */
public class Author{
    private final String name;
    private Date dateOfBirth;
    private int aId = -1;
    private List<Book> bookList = new ArrayList(); 
    private static final Pattern DATE_PATTERN = Pattern.compile("^((19|2[0-9])"
            + "[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
    
    public Author(String name, String date){
        this.name = name;
        if (!isValidDate(date)){
            Alert alert = new Alert(Alert.AlertType.ERROR, ("Illegal Date: " + date));
            alert.showAndWait();
            throw new IllegalArgumentException("Illegal Date: " + date);
        }
        this.dateOfBirth = Date.valueOf(date);
    }
    
    /**
     * checks if the string from user input
     * is a valid date
     */
    public static boolean isValidDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }
    
    public void addBook(Book book){
        this.bookList.add(book);
    }
    
    public void removeBook(Book book){
        this.bookList.remove(book);
    }

    public List<Book> getBooks() {
        List<Book> copy = new ArrayList<>();
        for (int i = 0; i < bookList.size(); i++) {
            copy.add(bookList.get(i));
        }
        return copy;
    }
    
    /**
     * Sets the aId for the author
     */
    public void setAId(int aId){
        this.aId = aId;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    /**
     * @return the aId
     */
    public int getAId() {
        return aId;
    }
    
    @Override
    public String toString(){
        String info;
        info = "Author: " + name + "\n" + " Date: " + dateOfBirth + "\n" + " aId: " + aId;
        info += "\n";
        return info;
    }
}