package hellofx;

import java.io.File;
import java.io.FileWriter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
// import io.github.cdimascio.dotenv.Dotenv;

public class TodoController {
    String filePath = "D:\\ALL ABOUT NP University\\PERKULIAHAN\\Semester 4\\OOP\\practice\\todolist-deskapp\\src\\output\\";

    @FXML TextField title, status;
    
    @FXML Button closeButton, saveTodo;

    @FXML Label errorMessage;

    private Controller mainController;

    
    @FXML
    private void closePopup() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveTodo() {
        try {
            String titleText = title.getText();
            String statusText = status.getText();
            
            if (titleText.isEmpty() || statusText.isEmpty()) {
                System.out.println("Please fill in all fields");
                errorMessage.setText("Please fill in all fields");
                return;
            }

            FileWriter fileObj = new FileWriter(filePath + titleText + ".txt");
            fileObj.write(titleText + "|" + statusText);
            fileObj.close();
            if (mainController != null) {
                mainController.refreshTable();
            }
            closePopup();
            System.out.println("Successfully wrote to the file");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void setMainController(Controller controller) {
        this.mainController = controller;
    }
}
