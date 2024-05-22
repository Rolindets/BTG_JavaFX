package com.mycompany.testingjavafx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.fxml.FXML;
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
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
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
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "INSERT INTO customers(first_name, last_name, phone, email) VALUES(?,?,?,?)";

        try
        {
            // Ensure the text field is accessible and get the value from it
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String phone = phoneTextField.getText();
            String email = emailTextField.getText();

            if (firstName == null || firstName.isEmpty()) 
            {
                System.out.println("First name is empty");
                return;
            }
            else if (lastName == null || lastName.isEmpty()) 
            {
                System.out.println("Last name is empty");
                return;
            }
            else if (phone == null || phone.isEmpty()) 
            {
                System.out.println("Phone number is empty");
                return;
            }
            else if (email == null || email.isEmpty()) 
            {
                System.out.println("Email is empty");
                return;
            }

            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);


            // Execute the prepared statement
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new customer was inserted successfully!");
            } else {
                System.out.println("Inserting customer failed, no rows affected.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    public void clearButton()
    {
        firstNameTextField.clear();
        lastNameTextField.clear();
        phoneTextField.clear();
        emailTextField.clear();
    }
    
    @FXML
    private void exitButton()
    {
        Platform.exit();
    }
}
