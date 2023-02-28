package com.mycompany.db1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BooksDbInterface;
import model.MockBooksDb;
import view.BooksPane;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        
        BooksDbInterface booksDb = new MockBooksDb(); //model
        BooksPane booksView = new BooksPane((MockBooksDb) booksDb); // view
        
        Scene scene=new Scene(booksView, 650, 650);
        stage.setTitle("Collection of books");
        stage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
            }
            catch (Exception e) {}
        });
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show(); 
    }
    
    public static void main(String[] args) {
        launch();
    }

}