
package com.mycompany.testingjavafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;




public class SecondaryController implements Initializable 
{
    
    @FXML
    Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    public void backButton() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        
        Stage window = (Stage)backButton.getScene().getWindow();
        window.setScene(new Scene(root, 1645, 1069));
    }
    
}
