package view;

import model.SearchMode;
import model.Book;
import model.MockBooksDb;
import java.sql.Date;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Author;
import model.Genre;

/**
 * The main pane for the view, extending VBox and including the menus. An
 * internal BorderPane holds the TableView for books and a search utility.
 *
 * @author Joel
 */
public class BooksPane extends VBox {

    private TableView<Book> booksTable;
    private ObservableList<Book> booksInTable; // the data backing the table view

    private ComboBox<SearchMode> searchModeBox;
    private TextField searchField;
    private Button searchButton;

    private MenuBar menuBar;

    public BooksPane(MockBooksDb booksDb) {
        final Controller controller = new Controller(booksDb, this);
        this.init(controller);
    }

    /**
     * Display a new set of books, e.g. from a database select, in the
     * booksTable table view.
     *
     * @param books the books to display
     */
    public void displayBooks(List<Book> books) {
        booksInTable.clear();
        booksInTable.addAll(books);
    }
    
    /**
     * Notify user on input error or exceptions.
     * 
     * @param msg the message
     * @param type types: INFORMATION, WARNING et c.
     */
    protected void showAlertAndWait(String msg, Alert.AlertType type) {
        // types: INFORMATION, WARNING etc.
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }

    private void init(Controller controller) {

        booksInTable = FXCollections.observableArrayList();

        // init views and event handlers
        initBooksTable();
        initSearchView(controller);
        initMenus(controller);

        FlowPane bottomPane = new FlowPane();
        bottomPane.setHgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));
        bottomPane.getChildren().addAll(searchModeBox, searchField, searchButton);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(booksTable);
        mainPane.setBottom(bottomPane);
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(menuBar, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
    }

    private void initBooksTable() {
        booksTable = new TableView<>();
        booksTable.setEditable(false); // don't allow user updates (yet)

        // define columns
        TableColumn<Book, String> bookIdCol = new TableColumn<>("BookId");
        TableColumn<Book, String> ratingCol = new TableColumn<>("Rating");
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, Date> publishedCol = new TableColumn<>("Published");
        booksTable.getColumns().addAll(bookIdCol, titleCol, isbnCol, publishedCol, ratingCol);
        // give title column some extra space
        titleCol.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.5));

        // define how to fill data for each cell, 
        // get values from Book properties
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("published"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        
        // associate the table view with the data
        booksTable.setItems(booksInTable);
    }

    private void initSearchView(Controller controller) {
        searchField = new TextField();
        searchField.setPromptText("Search for...");
        searchModeBox = new ComboBox<>();
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);
        searchButton = new Button("Search");
        
        // event handling (dispatch to controller)
        searchButton.setOnAction((ActionEvent event) -> {
            String searchFor = searchField.getText();
            SearchMode mode = searchModeBox.getValue();
            controller.onSearchSelected(searchFor, mode);
        });
    }

    private void initMenus(Controller controller) {
        Menu fileMenu = new Menu("Options");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem connectItem = new MenuItem("Connect to Db");
        MenuItem disconnectItem = new MenuItem("Disconnect");
        MenuItem updateItem = new MenuItem("Update");
        fileMenu.getItems().addAll(exitItem, connectItem, disconnectItem, updateItem);

        Menu manageMenu = new Menu("Manage");
        MenuItem addItem = new MenuItem("Add");
        MenuItem removeItem = new MenuItem("Remove");
        MenuItem ratingItem = new MenuItem("Set Rating");
        manageMenu.getItems().addAll(addItem, removeItem, ratingItem);
        
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, manageMenu);
        
        exitItem.setOnAction((ActionEvent event) -> {controller.handleDisconnectEvent();Platform.exit();});
        connectItem.setOnAction((ActionEvent event) -> {controller.handleConnectEvent();});
        disconnectItem.setOnAction((ActionEvent event) -> {controller.handleDisconnectEvent();});
        updateItem.setOnAction((ActionEvent event) -> {controller.handleUpdateEvent();});
        addItem.setOnAction((ActionEvent event) -> {addBook(controller);});
        removeItem.setOnAction(e -> Manage.removeBook(controller));
        ratingItem.setOnAction(e -> Manage.setRating(controller));
    }
    
    public void addBook(Controller controller){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Book");
        ComboBox<Genre> genre = new ComboBox<>();
        genre.getItems().addAll(Genre.values());
        genre.setValue(Genre.Action);
        
        TextField title = new TextField();
        title.setPromptText("Title: ");
        TextField isbn = new TextField();
        isbn.setPromptText("ISBN: ");
        TextField storyLine = new TextField();
        storyLine.setPromptText("Storyline: ");
        TextField publishedYear = new TextField();
        publishedYear.setPromptText("YYYY: ");
        TextField publishedMonth = new TextField();
        publishedMonth.setPromptText("MM: ");
        TextField publishedDay = new TextField();
        publishedDay.setPromptText("DD: ");
        
        Button authorButton = new Button("Add author(s) to book");
        authorButton.setOnAction((ActionEvent event) -> {
            addAuthor(controller);
        });
        
        Button addButton = new Button("Add book to collection");
        addButton.setOnAction((ActionEvent event) -> {
            String date = (publishedYear.getText()+"-"+publishedMonth.getText()+"-"+publishedDay.getText());
            Book book = new Book(-1, title.getText(), isbn.getText(), storyLine.getText(), genre.getValue(), date);
            controller.addBook(book);
            window.close();
        });
        VBox layout = new VBox(20);
        layout.getChildren().addAll(title, isbn, storyLine, publishedYear,
                publishedMonth, publishedDay, genre, authorButton, addButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 430, 430);
        window.setScene(scene);
        window.showAndWait();
    }
    
    public void addAuthor(Controller controller){
        Stage authorWindow = new Stage();
        authorWindow.initModality(Modality.APPLICATION_MODAL);
        authorWindow.setTitle("Add author");
        TextField authorName = new TextField();
        authorName.setPromptText("Author name, ex: Albert Einstein...");
        TextField authorYear = new TextField();
        authorYear.setPromptText("YYYY: ");
        TextField authorMonth = new TextField();
        authorMonth.setPromptText("MM: ");
        TextField authorDay = new TextField();
        authorDay.setPromptText("DD: ");
        Button addButton = new Button("Add author to book");
        
        addButton.setOnAction((ActionEvent event) -> {
            String date = (authorYear.getText()+"-"+authorMonth.getText()+"-"+authorDay.getText());
            Author author = new Author(authorName.getText(), date);
            controller.addAuthor(author);
            authorWindow.close();
        });
        
        VBox layout = new VBox(20);
        layout.getChildren().addAll(authorName, authorYear, authorMonth, authorDay, addButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 300);
        authorWindow.setScene(scene);
        authorWindow.showAndWait();
    }
}