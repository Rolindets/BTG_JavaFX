
package com.mycompany.testingjavafx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
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
    private TextField permitNumberTextField;
    @FXML
    private TextField quoteTextField;
    @FXML
    private TextField costTextField;
    @FXML
    private TextField installTextField;
    @FXML
    private TextField totalTextField;
    @FXML
    private TextField soldTextField;
    @FXML
    private TextField profitTextField;
    @FXML
    private TextField fiftyTextField;
    @FXML
    private TextField miscTextField;
    
    @FXML
    private CheckBox hasPermit;
    @FXML
    private CheckBox hasNOC;
    @FXML
    private CheckBox hasNOA;
    @FXML
    private CheckBox hasHOA;
    @FXML
    private CheckBox hasLICINS;
    @FXML
    private CheckBox hasNotary;
    @FXML
    private CheckBox hasRamms;
    @FXML
    private CheckBox hasRetrofit;
    @FXML
    private CheckBox hasDrawing;
    
    @FXML
    private TextArea textAreaNotes;
    
    @FXML
    Button backButton;
    
    private int customerId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Add listeners to the cost and sold text fields
        costTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                calculateProfit();
            }

        });

        soldTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                calculateProfit();
            }
        });
        
        installTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                calculateProfit();
            }

        });
    }
    
    
    
    private void calculateProfit() 
    {
            try 
            {
                double cost = Double.parseDouble(costTextField.getText());
                double sold = Double.parseDouble(soldTextField.getText());
                double install = Double.parseDouble(installTextField.getText());

                double total = cost + install;
                double profit = (sold - cost) - install;
                double fiftyPercent = (sold - install) / 2;
                
                profitTextField.setText(String.format("%.2f", profit));
                totalTextField.setText(String.format("%.2f", total));
                fiftyTextField.setText(String.format("%.2f", fiftyPercent));

            } catch (NumberFormatException e) {
                profitTextField.setText("Invalid input");
            }
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
        orderedDateTextField.setText(rs.getString("order_date"));
        uploadedDateTextField.setText(rs.getString("upload_date"));
        permitNumberTextField.setText(rs.getString("permit_number")); 
        textAreaNotes.setText(rs.getString("notes")); 
        hasPermit.setSelected(rs.getBoolean("has_permit"));
        hasNOC.setSelected(rs.getBoolean("has_noc"));
        hasNOA.setSelected(rs.getBoolean("has_noa"));
        hasHOA.setSelected(rs.getBoolean("has_hoa"));
        hasLICINS.setSelected(rs.getBoolean("has_licins"));
        hasNotary.setSelected(rs.getBoolean("has_notary"));
        hasRamms.setSelected(rs.getBoolean("has_ramms"));
        hasRetrofit.setSelected(rs.getBoolean("has_retrofit"));
        hasDrawing.setSelected(rs.getBoolean("has_drawing"));
        quoteTextField.setText(rs.getString("quote")); 
        costTextField.setText(rs.getString("cost")); 
        installTextField.setText(rs.getString("install")); 
        totalTextField.setText(rs.getString("total_cost")); 
        soldTextField.setText(rs.getString("sold")); 
        profitTextField.setText(rs.getString("profit")); 
        fiftyTextField.setText(rs.getString("fifty_percent")); 
        miscTextField.setText(rs.getString("misc")); 
    }
    
    @FXML
public void saveButton() {
    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();

    // Query to check if the customer already exists in the customers table
    String checkCustomerQuery = "SELECT customer_id FROM customers WHERE first_name = ? AND "
            + "last_name = ? AND email = ?";

    // Query to insert a new customer into the customers table
    String insertCustomerQuery = "INSERT INTO customers (first_name, last_name, phone, "
            + "email, address) VALUES (?, ?, ?, ?, ?)";

    // Query to check if the permit_number already exists in the blue_forms table
    String checkPermitQuery = "SELECT permit_number FROM blue_forms WHERE permit_number = ?";

    // Query to insert a new entry into the blue_forms table
    String insertBlueFormQuery = "INSERT INTO blue_forms (customer_id, first_name, last_name, phone, "
        + "email, address, city, state, zip_code, contractor_fee, order_date, upload_date, "
        + "permit_number, quote, cost, install, total_cost, sold, profit, fifty_percent, misc, has_permit, "
        + "has_noc, has_noa, has_hoa, has_licins, has_notary, has_ramms, has_retrofit, "
        + "has_drawing, notes) VALUES (?, ?, ?, ?, ?, ?,"
            + " ?, ?, ?, ?, ?, ?,"
            + " ?, ?, ?, ?, ?, ?,"
            + " ?, ?, ?, ?, ?, ?,"
            + " ?, ?, ?, ?, ?, ?, ?)";


    // Query to update an existing entry in the blue_forms table to avoid adding and new entry with same permit number
    String updateBlueFormQuery = "UPDATE blue_forms SET customer_id = ?, first_name = ?, "
        + "last_name = ?, phone = ?, email = ?, address = ?, city = ?, state = ?, zip_code = ?, "
        + "contractor_fee = ?, order_date = ?, upload_date = ?, quote = ?, cost = ?, install = ?, "
        + "total_cost = ?, sold = ?, profit = ?, fifty_percent = ?, misc = ?, has_permit = ?, has_noc = ?, "
        + "has_noa = ?, has_hoa = ?, has_licins = ?, has_notary = ?, has_ramms = ?, has_retrofit = ?, "
        + "has_drawing = ?, notes = ? WHERE permit_number = ?";

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
        String permitNumber = permitNumberTextField.getText();
        String notes = textAreaNotes.getText();
        String quote = quoteTextField.getText();
        String cost = costTextField.getText();
        String install = installTextField.getText();
        String totalCost = totalTextField.getText();
        String sold = soldTextField.getText();
        String profit = profitTextField.getText();
        String fiftyPercent = fiftyTextField.getText();
        String misc = miscTextField.getText();

        // Get checkbox states
        boolean hasPermitState = hasPermit.isSelected();
        boolean hasNOCState = hasNOC.isSelected();
        boolean hasNOAState = hasNOA.isSelected();
        boolean hasHOAState = hasHOA.isSelected();
        boolean hasLICINSState = hasLICINS.isSelected();
        boolean hasNotaryState = hasNotary.isSelected();
        boolean hasRammsState = hasRamms.isSelected();
        boolean hasRetrofitState = hasRetrofit.isSelected();
        boolean hasDrawingState = hasDrawing.isSelected();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || permitNumber.isEmpty()) {
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

        // Check if the permit_number already exists in the blue_forms table
        PreparedStatement checkPermitStatement = connectDB.prepareStatement(checkPermitQuery);
        checkPermitStatement.setString(1, permitNumber);
        ResultSet permitResult = checkPermitStatement.executeQuery();

        if (permitResult.next()) {
            // Permit number already exists, update the existing record
            PreparedStatement updateBlueFormStatement = connectDB.prepareStatement(updateBlueFormQuery);
        updateBlueFormStatement.setInt(1, customerId);
        updateBlueFormStatement.setString(2, firstName);
        updateBlueFormStatement.setString(3, lastName);
        updateBlueFormStatement.setString(4, phone);
        updateBlueFormStatement.setString(5, email);
        updateBlueFormStatement.setString(6, address);
        updateBlueFormStatement.setString(7, city);
        updateBlueFormStatement.setString(8, state);
        updateBlueFormStatement.setString(9, zipCode);
        updateBlueFormStatement.setDouble(10, contractorFeeDouble);
        updateBlueFormStatement.setString(11, orderedDate);
        updateBlueFormStatement.setString(12, uploadedDate);
        updateBlueFormStatement.setString(13, quote);
        updateBlueFormStatement.setString(14, cost);
        updateBlueFormStatement.setString(15, install);
        updateBlueFormStatement.setString(16, totalCost);
        updateBlueFormStatement.setString(17, sold);
        updateBlueFormStatement.setString(18, profit);
        updateBlueFormStatement.setString(19, fiftyPercent);
        updateBlueFormStatement.setString(20, misc);
        updateBlueFormStatement.setBoolean(21, hasPermitState);
        updateBlueFormStatement.setBoolean(22, hasNOCState);
        updateBlueFormStatement.setBoolean(23, hasNOAState);
        updateBlueFormStatement.setBoolean(24, hasHOAState);
        updateBlueFormStatement.setBoolean(25, hasLICINSState);
        updateBlueFormStatement.setBoolean(26, hasNotaryState);
        updateBlueFormStatement.setBoolean(27, hasRammsState);
        updateBlueFormStatement.setBoolean(28, hasRetrofitState);
        updateBlueFormStatement.setBoolean(29, hasDrawingState);
        updateBlueFormStatement.setString(30, notes);
        updateBlueFormStatement.setString(31, permitNumber);
        
            int rowsUpdated = updateBlueFormStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Existing blue form entry was updated successfully!");
            } else {
                System.out.println("Updating blue form entry failed, no rows affected.");
            }
        } else {
            // Permit number does not exist, insert a new record
            PreparedStatement insertBlueFormStatement = connectDB.prepareStatement(insertBlueFormQuery);
        insertBlueFormStatement.setInt(1, customerId);
        insertBlueFormStatement.setString(2, firstName);
        insertBlueFormStatement.setString(3, lastName);
        insertBlueFormStatement.setString(4, phone);
        insertBlueFormStatement.setString(5, email);
        insertBlueFormStatement.setString(6, address);
        insertBlueFormStatement.setString(7, city);
        insertBlueFormStatement.setString(8, state);
        insertBlueFormStatement.setString(9, zipCode);
        insertBlueFormStatement.setDouble(10, contractorFeeDouble);
        insertBlueFormStatement.setString(11, orderedDate);
        insertBlueFormStatement.setString(12, uploadedDate);
        insertBlueFormStatement.setString(13, permitNumber);
        insertBlueFormStatement.setString(14, quote);
        insertBlueFormStatement.setString(15, cost);
        insertBlueFormStatement.setString(16, install);
        insertBlueFormStatement.setString(17, totalCost);
        insertBlueFormStatement.setString(18, sold);
        insertBlueFormStatement.setString(19, profit);
        insertBlueFormStatement.setString(20, fiftyPercent);
        insertBlueFormStatement.setString(21, misc);
        insertBlueFormStatement.setBoolean(22, hasPermitState);
        insertBlueFormStatement.setBoolean(23, hasNOCState);
        insertBlueFormStatement.setBoolean(24, hasNOAState);
        insertBlueFormStatement.setBoolean(25, hasHOAState);
        insertBlueFormStatement.setBoolean(26, hasLICINSState);
        insertBlueFormStatement.setBoolean(27, hasNotaryState);
        insertBlueFormStatement.setBoolean(28, hasRammsState);
        insertBlueFormStatement.setBoolean(29, hasRetrofitState);
        insertBlueFormStatement.setBoolean(30, hasDrawingState);
        insertBlueFormStatement.setString(31, notes);
        
            int blueFormRowsInserted = insertBlueFormStatement.executeUpdate();
            if (blueFormRowsInserted > 0) {
                System.out.println("A new blue form entry was inserted successfully!");
            } else {
                System.out.println("Inserting blue form entry failed, no rows affected.");
            }
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
