package hellofx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller implements Initializable {

    Connection conn = DbModel.getConnection();

    @FXML
    private Hyperlink linkLinkedin;

    @FXML
    private TableView<TodoItem> todoTable;

    @FXML
    private TableColumn<TodoItem, String> titleColumn;

    @FXML
    private TableColumn<TodoItem, String> statusColumn;

    @FXML
    private TableColumn<TodoItem, Void> actionColumn;

    ObservableList<TodoItem> todoItems = FXCollections.observableArrayList();


    // Function for opening the LinkedIn profile
    @FXML
    private void openLinkedin() {
        String url = "https://www.linkedin.com/in/rendy-elang-lesmana/";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function for opening add popup
    @FXML
    private void openAddPopup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-task-popup.fxml"));
        try {
            Parent root = fxmlLoader.load();

            TodoController todoController = fxmlLoader.getController();
            todoController.setMainController(this); // kirim referensi ke controller utama

            Stage stage = new Stage();
            stage.setTitle("Add Task");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEditPopup(int id, String title, String status) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-task-popup.fxml"));
        try {
            Parent root = fxmlLoader.load();

            TodoController todoController = fxmlLoader.getController();
            todoController.setMainController(this); // kirim referensi ke controller utama
            todoController.setEditData(id, title, status);

            todoController.title.setText(title);
            todoController.status.setText(status);

            Stage stage = new Stage();
            stage.setTitle("Edit Task");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        System.out.println("Database connected");
        if (conn != null) {
            todoItems = DbModel.readData(conn);
            todoTable.setItems(todoItems);

            actionColumn.setCellFactory(col -> new javafx.scene.control.TableCell<TodoItem, Void>() {
                private final javafx.scene.control.Button editButton;
                private final javafx.scene.control.Button deleteButton;
                private final javafx.scene.layout.HBox pane;
                
                {
                    FontIcon trashIcon = new FontIcon("fa-trash");
                    trashIcon.setIconSize(21);
                    trashIcon.setIconColor(javafx.scene.paint.Color.WHITE);

                    deleteButton = new javafx.scene.control.Button("", trashIcon);
                    deleteButton.setCursor(javafx.scene.Cursor.HAND);
                    deleteButton.setStyle("-fx-background-color: #FF6D6D;");

                    FontIcon editIcon = new FontIcon("fa-pencil-square-o");
                    editIcon.setIconSize(21);
                    editIcon.setIconColor(javafx.scene.paint.Color.WHITE);

                    editButton = new javafx.scene.control.Button("", editIcon);
                    editButton.setCursor(javafx.scene.Cursor.HAND);
                    editButton.setStyle("-fx-background-color: #96ACFF;");

                    // Action when edit button clicked
                    /**
                     * Belum beres karena malah nambah record baru bukannya edit
                     */
                editButton.setOnAction(event -> {
                    TodoItem item = getTableView().getItems().get(getIndex());
                    // TODO: buka popup edit, isi dengan data item
                    System.out.println("Edit: " + item.getId());
                    openEditPopup(item.getId() ,item.getTitle(), item.getStatus());
                    
                });

                // Action when delete button clicked
                deleteButton.setOnAction(event -> {
                    TodoItem item = getTableView().getItems().get(getIndex());
                    // TODO: hapus dari database dan refresh tabel
                    // System.out.println("Delete: " + item.getId());
                    DbModel.deleteData(conn, item.getId());
                    refreshTable(); // refresh tabel setelah penghapusan
                });
                pane = new javafx.scene.layout.HBox(5, editButton, deleteButton);
                pane.setStyle("-fx-alignment: CENTER;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
        } else {
            System.out.println("Failed to connect to the database");
        }
    }

    // private void loadTodosFromFiles() {
    //     File folder = new File("D:\\ALL ABOUT NP University\\PERKULIAHAN\\Semester 4\\OOP\\practice\\todolist-deskapp\\src\\output\\");
    //     File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

    //     if (listOfFiles != null) {
    //         for (File file : listOfFiles) {
    //             try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    //                 String line = br.readLine();
    //                 if (line != null) {
    //                     String[] parts = line.split("\\|");
    //                     if (parts.length == 2) {
    //                         todoItems.add(new TodoItem(parts[0], parts[1]));
    //                     }
    //                 }
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    // }

    public void refreshTable() {
        todoItems.clear();           // hapus dulu semua data dari tabel
        
        if (conn != null) {
            todoItems.addAll(DbModel.readData(conn)); // baca ulang data dari database
            todoTable.setItems(todoItems); // set ulang data ke tabel
        } else {
            System.out.println("Failed to connect to the database");
        }
    }
}