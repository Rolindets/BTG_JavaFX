package com.mycompany.testingjavafx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import static javafx.application.Platform.exit;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PrimaryController
{
    
    @FXML
    Pane myPane;

    @FXML
    ColorPicker myColorPicker;
    
    @FXML
    Button saveButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    
    @FXML
    public void changeColor()
    {
        Color myColor = myColorPicker.getValue();
        myPane.setBackground(new Background(new BackgroundFill(myColor, null, null)));
    }
    
    @FXML
    public void saveButton()
    {
        //testing if button works
        System.out.println("Saved!");
        
         // Retrieve text from text fields
        String name = nameTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();
        
        // Generate a unique filename using timestamp. 
        //file will always save with month/day/seconds... this way it never overwrites existing file 
        String timestamp = new SimpleDateFormat("MMdd_ss").format(new Date());
        String filename = "Estimate_" + timestamp + "_.txt";
        
        //save the file path in file variable
        String userHome = System.getProperty("user.home");
        File file = new File(userHome + "\\OneDrive\\Desktop\\" +filename);

        //writing to the file
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
            writer.write("Name: " + name + "\n");
            writer.write("Phone: " + phone + "\n");
            writer.write("Email: " + email + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void clearButton()
    {
        nameTextField.clear();
        phoneTextField.clear();
        emailTextField.clear();
    }
    
    @FXML
    private void exitButton()
    {
        Platform.exit();
    }
}
