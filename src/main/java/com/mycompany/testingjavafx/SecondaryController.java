
package com.mycompany.testingjavafx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;




public class SecondaryController implements Initializable 
{
    
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField stateTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private TextField contractorTextField;
    @FXML
    private TextField orderedDateTextField;
    @FXML
    private TextField uploadedDateTextField;
    @FXML
    private TextField permitNumberTextFIeld;
    
    private int customerId;
    
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

    void setCustomerDetails(String firstName, String lastName, String phone, String email, int selectedCustomerId) 
    {
        firstNameTextField.setText(firstName);
        lastNameTextField.setText(lastName);
        phoneTextField.setText(phone);
        emailTextField.setText(email);
        this.customerId = customerId;
    }
    
    void setQuoteData(ResultSet rs) throws SQLException 
    {
        
        addressTextField.setText(rs.getString("address"));
        cityTextField.setText(rs.getString("city"));
        stateTextField.setText(rs.getString("state"));
        zipCodeTextField.setText(rs.getString("zip_code"));
        contractorTextField.setText(rs.getString("contractor_fee"));
        orderedDateTextField.setText(rs.getString("ordered_date"));
        uploadedDateTextField.setText(rs.getString("uploaded"));
        permitNumberTextFIeld.setText(rs.getString("permit_number"));


//        cityTextField.setText(lastName);
//        stateTextField.setText(phone);
//        zipCodeTextField.setText(email);       
    }
    
    @FXML
    public void saveButton() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        // Query to check if the customer already exists in the customers table
        String checkCustomerQuery = "SELECT customer_id FROM customers WHERE first_name = ? AND last_name = ? AND email = ?";

        // Query to insert a new customer into the customers table
        String insertCustomerQuery = "INSERT INTO customers (first_name, last_name, phone, email, address) VALUES (?, ?, ?, ?, ?)";

        // Query to insert a new entry into the blue_forms table
        String blueFormQuery = "INSERT INTO blue_forms (customer_id, first_name, last_name, phone, email, address, city, state,"
                                                                                        + " zip_code, contractor_fee, order_date, upload_date, permit_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Get the values from text fields
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String phone = phoneTextField.getText();
            String email = emailTextField.getText();
            String address = addressTextField.getText();
            String city = cityTextField.getText();
            String state = stateTextField.getText();
            String zipCode = zipCodeTextField.getText();
            String contractorFee = contractorTextField.getText();
            double contractorFeeDouble = Double.parseDouble(contractorFee);
            String orderedDate = orderedDateTextField.getText();
            String uploadedDate = uploadedDateTextField.getText();
            String permitNumber = permitNumberTextFIeld.getText();

            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                System.out.println("Please fill in all required fields");
                return;
            }

            // Check if the customer already exists in the customers table
            PreparedStatement checkCustomerStatement = connectDB.prepareStatement(checkCustomerQuery);
            checkCustomerStatement.setString(1, firstName);
            checkCustomerStatement.setString(2, lastName);
            checkCustomerStatement.setString(3, email);
            ResultSet customerResult = checkCustomerStatement.executeQuery();

            int customerId;

            if (customerResult.next()) {
                // Customer already exists, retrieve the existing customer_id
                customerId = customerResult.getInt("customer_id");
            } else {
                // Customer doesn't exist, insert a new customer into the customers table
                PreparedStatement insertCustomerStatement = connectDB.prepareStatement(insertCustomerQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                insertCustomerStatement.setString(1, firstName);
                insertCustomerStatement.setString(2, lastName);
                insertCustomerStatement.setString(3, phone);
                insertCustomerStatement.setString(4, email);
                insertCustomerStatement.setString(5, address);

                int rowsInserted = insertCustomerStatement.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = insertCustomerStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        customerId = generatedKeys.getInt(1);
                    } else {
                        System.out.println("Failed to retrieve generated customer_id.");
                        return;
                    }
                } else {
                    System.out.println("Inserting customer failed, no rows affected.");
                    return;
                }
            }

            // Insert into blue_forms table using the retrieved or newly generated customer_id
            PreparedStatement blueFormStatement = connectDB.prepareStatement(blueFormQuery);
            blueFormStatement.setInt(1, customerId);
            blueFormStatement.setString(2, firstName);
            blueFormStatement.setString(3, lastName);
            blueFormStatement.setString(4, phone);
            blueFormStatement.setString(5, email);
            blueFormStatement.setString(6, address);
            blueFormStatement.setString(7, city);
            blueFormStatement.setString(8, state);
            blueFormStatement.setString(9, zipCode);
            blueFormStatement.setDouble(10, contractorFeeDouble);
            blueFormStatement.setString(11, orderedDate);
            blueFormStatement.setString(12, uploadedDate);
            blueFormStatement.setString(13, permitNumber);

            int blueFormRowsInserted = blueFormStatement.executeUpdate();
            if (blueFormRowsInserted > 0) {
                System.out.println("A new blue form entry was inserted successfully!");
            } else {
                System.out.println("Inserting blue form entry failed, no rows affected.");
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
}
