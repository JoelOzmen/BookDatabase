package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 * Handles the connection with the database
 *
 * @author Joel
 */
public class MockBooksDb implements BooksDbInterface {

    private final List<Book> books;
    private final List<Author> authorList;
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rset;

    public MockBooksDb() {
        books = new ArrayList<>();
        authorList = new ArrayList<>();
    }

    /**
     * Connects the program to a database
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public boolean connect() throws IOException, SQLException {
        String database = "db1";
        String url = "jdbc:mysql://localhost:3306/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useClientEnc=UTF8";
        String user = "Client";
        String psw = "Client123";
        conn = DriverManager.getConnection(url, user, psw);
        return true;
    }

    /**
     * Disconnects the program from the database
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public void disconnect() throws IOException, SQLException {
        books.clear();
        conn.close();
    }

    /**
     * @return All the books from the database
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public List<Book> getBooksFromDb() throws IOException, SQLException {
        Statement stmt = conn.createStatement();
        try {
            String sql = "SELECT * FROM Book";
            rset = stmt.executeQuery(sql);
            while (rset.next()) {
                int tmpId = rset.getInt("bookId");
                String tmpT = rset.getString("title");
                String tmpS = rset.getString("storyLine");
                String tmpI = rset.getString("isbn");
                String tmpG = rset.getString("genre");
                int tmpR = rset.getInt("rating");
                Date tmpD = rset.getDate("published");
                Book book = new Book(tmpId, tmpT, tmpI, tmpS, Genre.valueOf(tmpG), tmpD.toString());
                book.setRating(tmpR);
                books.add(book);
            }
            List<Book> result=createResult();
            return result;
        }
        finally {
            stmt.close();
            rset.close();
            books.clear();
        }
    }
    
    /**
     * @param searchTitle
     * @return Get specifik books from the database
     * by searching for the title
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws IOException, SQLException {
        try {
            String sql = "SELECT * FROM Book WHERE title = ? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, searchTitle);
            rset = pst.executeQuery();
            while (rset.next()) {
                int tmpId = rset.getInt("bookId");
                String tmpT = rset.getString("title");
                String tmpS = rset.getString("storyLine");
                String tmpI = rset.getString("isbn");
                String tmpG = rset.getString("genre");
                int tmpR = rset.getInt("rating");
                Date tmpD = rset.getDate("published");
                Book book = new Book(tmpId, tmpT, tmpI, tmpS, Genre.valueOf(tmpG), tmpD.toString());
                book.setRating(tmpR);
                books.add(book);
            }
            List<Book> result=createResult();
            return result;
        }
        finally {
            rset.close();
            books.clear();
        }
    }
    
    /**
     * @param searchAuthor
     * @return Get specifik books from the database
     * by searching for a authors name
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public List<Book> searchBooksByAuthor(String searchAuthor) throws IOException, SQLException {
        try {
            String sql = "SELECT * FROM Book WHERE bookId IN "
                    + "(SELECT  book FROM AB_TBL WHERE author IN "
                    + "(SELECT aId FROM Author WHERE name = ?))";
            pst = conn.prepareStatement(sql);
            pst.setString(1, searchAuthor);
            rset = pst.executeQuery();
            while (rset.next()) {
                int tmpId = rset.getInt("bookId");
                String tmpT = rset.getString("title");
                String tmpS = rset.getString("storyLine");
                String tmpI = rset.getString("isbn");
                String tmpG = rset.getString("genre");
                int tmpR = rset.getInt("rating");
                Date tmpD = rset.getDate("published");
                Book book = new Book(tmpId, tmpT, tmpI, tmpS, Genre.valueOf(tmpG), tmpD.toString());
                book.setRating(tmpR);
                books.add(book);
            }
            List<Book> result = createResult();
            return result;            
        } 
        finally {
            rset.close();
            books.clear();
        }
    }

    /**
     * @param searchIsbn
     * @return Get specifik books from the database
     * by searching for the isbn code
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public List<Book> searchBooksByISBN(String searchIsbn) throws IOException, SQLException {
        try {
            String sql = "SELECT * FROM Book WHERE isbn = ? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, searchIsbn);
            rset = pst.executeQuery();
            while (rset.next()) {
                int tmpId = rset.getInt("bookId");
                String tmpT = rset.getString("title");
                String tmpS = rset.getString("storyLine");
                String tmpI = rset.getString("isbn");
                String tmpG = rset.getString("genre");
                int tmpR = rset.getInt("rating");
                Date tmpD = rset.getDate("published");
                Book book = new Book(tmpId, tmpT, tmpI, tmpS, Genre.valueOf(tmpG), tmpD.toString());
                book.setRating(tmpR);
                books.add(book);
            }
            List<Book> result = createResult();
            return result;
        }
        finally {
            rset.close();
            books.clear();
        }
    }

    /**
     * @param searchRating
     * @return Get specifik books from the database
     * by searching for a rating
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public List<Book> searchBooksByRating(int searchRating) throws IOException, SQLException {
        try {
            String sql = "SELECT * FROM Book WHERE rating = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, searchRating);
            rset = pst.executeQuery();
            while (rset.next()) {
                int tmpId = rset.getInt("bookId");
                String tmpT = rset.getString("title");
                String tmpS = rset.getString("storyLine");
                String tmpI = rset.getString("isbn");
                String tmpG = rset.getString("genre");
                int tmpR = rset.getInt("rating");
                Date tmpD = rset.getDate("published");
                Book book = new Book(tmpId, tmpT, tmpI, tmpS, Genre.valueOf(tmpG), tmpD.toString());
                book.setRating(tmpR);
                books.add(book);
            }
            List<Book> result = createResult();
            return result;  
        }
        finally {
            rset.close();
            books.clear();
        }
    }

    /**
     * @param searchGenre
     * @return Get specifik books from the database
     * by searching for a genre
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public List<Book> searchBooksByGenre(String searchGenre) throws IOException, SQLException {
        try {
            String sql = "SELECT * FROM Book WHERE genre = ? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, searchGenre);
            rset = pst.executeQuery();
            while (rset.next()) {
                int tmpId = rset.getInt("bookId");
                String tmpT = rset.getString("title");
                String tmpS = rset.getString("storyLine");
                String tmpI = rset.getString("isbn");
                String tmpG = rset.getString("genre");
                int tmpR = rset.getInt("rating");
                Date tmpD = rset.getDate("published");
                Book book = new Book(tmpId, tmpT, tmpI, tmpS, Genre.valueOf(tmpG), tmpD.toString());
                book.setRating(tmpR);
                books.add(book);
            }
            List<Book> result = createResult();
            return result;
        }
        finally {
            rset.close();
            books.clear();
        }
    }
    
    @Override
    public List<Book> searchBooksByBookId(int searchBookId) throws IOException, SQLException {
        try {
            String sql = "SELECT * FROM Book WHERE bookId = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, searchBookId);
            rset = pst.executeQuery();
            while (rset.next()) {
                int tmpId = rset.getInt("bookId");
                String tmpT = rset.getString("title");
                String tmpS = rset.getString("storyLine");
                String tmpI = rset.getString("isbn");
                String tmpG = rset.getString("genre");
                int tmpR = rset.getInt("rating");
                Date tmpD = rset.getDate("published");
                Book book = new Book(tmpId, tmpT, tmpI, tmpS, Genre.valueOf(tmpG), tmpD.toString());
                book.setRating(tmpR);
                books.add(book);
            }
            List<Book> result = createResult();
            return result;  
        }
        finally {
            rset.close();
            books.clear();
        }
    }

    /**
     * Adds books to a list through a loop
     * @return result
     */
    public List<Book> createResult(){
        List<Book> result = new ArrayList<>();
        books.forEach(b -> {
            result.add(b);
        });
        return result;
    }
    
    /**
     * Sets rating for a specifik 
     * book in the database
     * @param bookId
     * @param newRating
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public void setRating(int bookId, int newRating) throws IOException, SQLException{
        try{
            String sql = "update Book set rating = ? where bookId = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, newRating);
            pst.setInt(2, bookId);
            pst.executeUpdate(); 
        } 
        finally{
            rset.close();
            pst.close();
            books.clear();
        }
    }
    
    /**
     * Adds a book and it's authors
     * to the database
     * @param book
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public void addBook(Book book) throws IOException, SQLException {
        ResultSet bookIdRes = null;
        ResultSet aIdRes = null;
        try {
            conn.setAutoCommit(false);
            String sql = "insert into Book(title, isbn, storyLine, genre, published) values (?,?,?,?,?)";
            String sql2 = "SELECT LAST_INSERT_ID()";
            String sql3 = "insert into Author(name, birth) values (?,?)";
            String sql4 = "insert into AB_TBL(book, author) values (?,?)";

            pst = conn.prepareStatement(sql);
            pst.setString(1, book.getTitle());
            pst.setString(2, book.getIsbn());
            pst.setString(3, book.getStoryLine());
            pst.setString(4, book.getGenre().name());
            pst.setDate(5, book.getPublished());
            pst.executeUpdate();
            for (int i = 0; i < authorList.size(); i++) {
                book.setAuthorToList(authorList.get(i));
            }
            pst = conn.prepareStatement(sql2);
            bookIdRes = pst.executeQuery();
            bookIdRes.next();
            book.setBookId(bookIdRes.getInt(1));
            for (int i = 0; i < authorList.size(); i++) {
                pst = conn.prepareStatement(sql3);
                pst.setString(1, book.getAuthors().get(i).getName());
                pst.setDate(2, book.getAuthors().get(i).getDateOfBirth());
                pst.execute();
                pst = conn.prepareStatement(sql2);
                aIdRes = pst.executeQuery();
                aIdRes.next();
                book.getAuthors().get(i).setAId(aIdRes.getInt(1));
                pst = conn.prepareStatement(sql4);
                pst.setInt(1, book.getBookId());
                pst.setInt(2, book.getAuthors().get(i).getAId());
                pst.execute();
            }
            conn.commit();
            pst.close();
            bookIdRes.close();
            aIdRes.close();
        } catch (SQLException e) {
            if (conn != null) {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
                throw e;
            }
        } finally {
            conn.setAutoCommit(true);
            rset.close();
            authorList.clear();
        }
    }
    
    /**
     * Removes a specifik book from the database
     *
     * @param bookId
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public void removeBook(int bookId) throws IOException, SQLException {
        try {
            String sql = "delete from Book where bookId = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, bookId);
            pst.executeUpdate();
        } finally {
            pst.close();
        }
    }

    /**
     * Adds a author to a book in the book arraylist
     *
     * @param author
     * @return author
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Override
    public Author addAuthor(Author author) throws IOException, SQLException {
        authorList.add(author);
        return author;
    }
}