package model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 * 
 * @author Joel
 */
public interface BooksDbInterface {
    
    /**
     * Connect to the database.
     * @param database
     * @return true on successful connection.
     */
    public boolean connect() throws IOException, SQLException;
    
    public void disconnect() throws IOException, SQLException;
    
    public List<Book> getBooksFromDb() throws IOException, SQLException;
    
    public List<Book> searchBooksByTitle(String searchFor) throws IOException, SQLException;
    
    public List<Book> searchBooksByAuthor(String searchFor) throws IOException, SQLException;
    
    public List<Book> searchBooksByISBN(String searchFor) throws IOException, SQLException;
    
    public List<Book> searchBooksByRating(int searchFor) throws IOException, SQLException;
    
    public List<Book> searchBooksByGenre(String searchFor) throws IOException, SQLException;
    
    public List<Book> searchBooksByBookId(int searchFor) throws IOException, SQLException;
    
    public void setRating(int bookId, int searchFor) throws IOException, SQLException;
    
    public void addBook(Book book)throws IOException, SQLException;
    
    public void removeBook(int bookId) throws IOException, SQLException;
    
    public Author addAuthor(Author author) throws IOException, SQLException;
}