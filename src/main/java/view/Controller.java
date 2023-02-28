package view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import model.SearchMode;
import model.Book;
import model.BooksDbInterface;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.Alert.AlertType.*;
import javafx.scene.control.ButtonType;
import model.Author;

/**
 * The controller is responsible for handling user requests and update the view
 * (and in some cases the model).
 *
 * @author Joel
 */
public class Controller {
    private final BooksPane booksView; // view
    private final BooksDbInterface booksDb; // model
    private int tmpBookId;

    public Controller(BooksDbInterface booksDb, BooksPane booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
        this.tmpBookId = 0;
    }

    protected void onSearchSelected(String searchFor, SearchMode mode) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (searchFor != null && searchFor.length() > 0) {
                        List<Book> result = null;
                        switch (mode) {
                            case BookId:
                                int intSearchFor = Integer.parseInt(searchFor);
                                result = booksDb.searchBooksByBookId(intSearchFor);
                                break;
                            case Title:
                                result = booksDb.searchBooksByTitle(searchFor);
                                break;
                            case ISBN:
                                result = booksDb.searchBooksByISBN(searchFor);
                                break;
                            case Author:
                                result = booksDb.searchBooksByAuthor(searchFor);
                                break;
                            case Rating:
                                intSearchFor = Integer.parseInt(searchFor);
                                result = booksDb.searchBooksByRating(intSearchFor);
                                break;
                            case Genre:
                                result = booksDb.searchBooksByGenre(searchFor);
                                break;
                            default:
                        }
                        if (result == null || result.isEmpty()) {
                            Platform.runLater(() -> booksView.showAlertAndWait("No "
                                    + "results found.", INFORMATION));
                        } else {
                            booksView.displayBooks(result);
                        }
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("Enter "
                                + "a search string!", WARNING));
                    }
                }
                catch (Exception ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }

    protected void getRating(String searchFor, SearchMode mode) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (searchFor != null && searchFor.length() > 0) {
                        List<Book> result = null;
                        switch (mode) {
                            case BookId:
                                int intSearchFor = Integer.parseInt(searchFor);
                                result = booksDb.searchBooksByBookId(intSearchFor);
                                break;
                            case Title:
                                result = booksDb.searchBooksByTitle(searchFor);
                                break;
                            case ISBN:
                                result = booksDb.searchBooksByISBN(searchFor);
                                break;
                            case Author:
                                result = booksDb.searchBooksByAuthor(searchFor);
                                break;
                            case Rating:
                                intSearchFor = Integer.parseInt(searchFor);
                                result = booksDb.searchBooksByRating(intSearchFor);
                                break;
                            case Genre:
                                result = booksDb.searchBooksByGenre(searchFor);
                                break;
                            default:
                        }
                        if (result.size() > 1) {
                            booksView.displayBooks(result);
                            Platform.runLater(() -> booksView.showAlertAndWait("No "
                                    + "specifik book found, Try again.", INFORMATION));
                        }
                        else if (result.size() == 1) {
                            tmpBookId = result.get(0).getBookId();
                            booksView.displayBooks(result);
                        }
                        else {
                            Platform.runLater(() -> booksView.showAlertAndWait("No "
                                    + "results found.", INFORMATION));
                        }
                    }
                    else {
                        Platform.runLater(() -> booksView.showAlertAndWait("Enter "
                                + "a search string!", WARNING));
                    }
                }
                catch (IOException | NumberFormatException | SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }

    protected void setNewRating(String newRating) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int ratingInt = Integer.parseInt(newRating);
                    if (tmpBookId != 0) {
                        if (ratingInt >= 0 && ratingInt <= 5) {
                            booksDb.setRating(tmpBookId, ratingInt);
                            Platform.runLater(() -> booksView.showAlertAndWait("The "
                                    + "rating is updated succesfully!", INFORMATION));
                        }
                        else {
                            Platform.runLater(() -> booksView.showAlertAndWait("Your "
                                    + "rating must be between 1-5!", ERROR));
                        }
                        tmpBookId = 0;
                    }
                    else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You "
                                + "must search for a specif book first!", ERROR));
                    }
                }
                catch (IOException | SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }

    protected void addBook(Book book) {
        new Thread() {
            @Override
            public void run() {
                try {
                    booksDb.addBook(book);
                    Platform.runLater(() -> booksView.showAlertAndWait("Book succesfully uploaded", INFORMATION));
                } catch (IOException | SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }

    protected void addAuthor(Author author) {
        new Thread() {
            @Override
            public void run() {
                try {
                    booksDb.addAuthor(author);
                    Platform.runLater(() -> booksView.showAlertAndWait("Author added to book", INFORMATION));
                } catch (IOException | SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }

    protected void handleConnectEvent() {
        new Thread() {
            @Override
            public void run() {
                try {
                    booksDb.connect();
                    booksView.displayBooks(booksDb.getBooksFromDb());
                }
                catch (IOException | SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }

    protected void handleDisconnectEvent() {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<Book> emptyList = new ArrayList<>();
                    booksView.displayBooks(emptyList);
                    booksDb.disconnect();
                    Platform.runLater(() -> booksView.showAlertAndWait("Disconnected "
                            + "successfully", INFORMATION));
                }
                catch (IOException | SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }
    
    protected void handleUpdateEvent() {
        new Thread() {
            @Override
            public void run() {
                try {
                    booksView.displayBooks(booksDb.getBooksFromDb());
                }
                catch (IOException | SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR));
                }
            }
        }.start();
    }
    
    protected void searchBookToRemove(String searchFor, SearchMode mode) {
        try {
            if (searchFor != null && searchFor.length() > 0) {
                List<Book> result = null;
                switch (mode) {
                    case BookId:
                        int intSearchFor = Integer.parseInt(searchFor);
                        result = booksDb.searchBooksByBookId(intSearchFor);
                        break;
                    case Title:
                        result = booksDb.searchBooksByTitle(searchFor);
                        break;
                    case ISBN:
                        result = booksDb.searchBooksByISBN(searchFor);
                        break;
                    case Author:
                        result = booksDb.searchBooksByAuthor(searchFor);
                        break;
                    case Rating:
                        intSearchFor = Integer.parseInt(searchFor);
                        result = booksDb.searchBooksByRating(intSearchFor);
                        break;
                    case Genre:
                        result = booksDb.searchBooksByGenre(searchFor);
                        break;
                    default:
                }
                if (result.size() > 1) {
                    booksView.displayBooks(result);
                    booksView.showAlertAndWait("No "
                            + "specifik book found, try again.", INFORMATION);
                }
                else if (result.size() == 1) {
                    tmpBookId = result.get(0).getBookId();
                    booksView.displayBooks(result);
                }
                else {
                    booksView.showAlertAndWait("No "
                            + "results found.", INFORMATION);
                }
            }
            else {
                booksView.showAlertAndWait("Enter "
                        + "a search string!", WARNING);
            }
        }
        catch (IOException | NumberFormatException | SQLException ex) {
            booksView.showAlertAndWait(ex.getMessage(), ERROR);
        }
    }

    protected void removeBook() {
        try {
            if (tmpBookId != 0) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation!");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this book?");
                Optional<ButtonType> action = alert.showAndWait();
                if (action.get() == ButtonType.OK) {
                    booksDb.removeBook(tmpBookId);
                }
            }
        }
        catch (IOException | SQLException ex) {
            booksView.showAlertAndWait(ex.getMessage(), ERROR);
        }
    }
}