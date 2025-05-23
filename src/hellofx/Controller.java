package hellofx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    private Hyperlink linkLinkedin;

    @FXML
    private TableView<TodoItem> todoTable;

    @FXML
    private TableColumn<TodoItem, String> titleColumn;

    @FXML
    private TableColumn<TodoItem, String> statusColumn;

    ObservableList<TodoItem> todoItems = FXCollections.observableArrayList();

    @FXML
    private void openLinkedin() {
        String url = "https://www.linkedin.com/in/rendy-elang-lesmana/";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadTodosFromFiles();
        todoTable.setItems(todoItems);
    }

    private void loadTodosFromFiles() {
        File folder = new File("D:\\ALL ABOUT NP University\\PERKULIAHAN\\Semester 4\\OOP\\practice\\todolist-deskapp\\src\\output\\");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line = br.readLine();
                    if (line != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length == 2) {
                            todoItems.add(new TodoItem(parts[0], parts[1]));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void refreshTable() {
        todoItems.clear();           // hapus dulu semua data dari tabel
        loadTodosFromFiles();        // baca ulang semua file txt
        todoTable.setItems(todoItems);  // tampilkan lagi
    }
}