package view;

import model.SearchMode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class showing views for adding,
 * removing and updating a book
 * 
 * @author Joel
 */
public class Manage {
    public static void setRating(Controller controller){
        Stage window = new Stage();
        ComboBox<SearchMode> searchModeBox = new ComboBox<>();
        Label l1 = new Label();
        TextField textBox= new TextField();
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Button addButton = new Button("Add Rating");
        BorderPane mainPane = new BorderPane();
        
        searchField.setPromptText("Search for...");
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchFor = searchField.getText();
                SearchMode mode = searchModeBox.getValue();
                controller.getRating(searchFor, mode);
            }
        });
        
        FlowPane topPane = new FlowPane();
        topPane.setHgap(10);
        topPane.setPadding(new Insets(10, 10, 10, 10));
        topPane.getChildren().addAll(searchModeBox, searchField, searchButton);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Set Rating");
        l1.setText("Rate the book: ");
        textBox.setMaxWidth(100);
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newRating = textBox.getText();
                controller.setNewRating(newRating);
                window.close();
            }
        });
    
        GridPane bottomPane = new GridPane();
        bottomPane.setPadding(new Insets(20, 20, 20, 20));
    	bottomPane.setVgap(10);
    	bottomPane.setHgap(10);
        bottomPane.add(l1, 12, 0);
        bottomPane.add(textBox, 12, 1);
        bottomPane.add(addButton, 12, 2);

        VBox layout = new VBox(20);
        mainPane.setCenter(topPane);
        mainPane.setBottom(bottomPane);
        layout.getChildren().add(mainPane);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 400,300);
        window.setScene(scene);
        window.showAndWait();
    }
    
    public static void removeBook(Controller controller){
        Stage window = new Stage();
        ComboBox<SearchMode> searchModeBox = new ComboBox<>();
        Label l1 = new Label("Which book do you want to delete?");
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        BorderPane mainPane = new BorderPane();
        
        searchField.setPromptText("Search for...");
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchFor = searchField.getText();
                SearchMode mode = searchModeBox.getValue();
                controller.searchBookToRemove(searchFor, mode);
                controller.removeBook();
                controller.handleUpdateEvent();
                window.close();
            }
        });
        
        FlowPane topPane = new FlowPane();
        topPane.setHgap(10);
        topPane.setPadding(new Insets(10, 10, 10, 10));
        topPane.getChildren().addAll(searchModeBox, searchField, searchButton);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Remove Book");

        VBox layout = new VBox(20);
        mainPane.setCenter(topPane);
        mainPane.setTop(l1);
        layout.getChildren().add(mainPane);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 350,150);
        window.setScene(scene);
        window.showAndWait();
    }
}