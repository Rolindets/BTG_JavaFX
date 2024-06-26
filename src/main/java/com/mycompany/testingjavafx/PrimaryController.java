package com.mycompany.testingjavafx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PrimaryController
{
    @FXML
    Pane myPane;

    @FXML
    ColorPicker myColorPicker;
    
    @FXML
    Button editButton;
    
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;    
    
    @FXML
    private TableView<Customer> dataDisplayTable;
    @FXML
    private TableColumn<Customer, String> firstNameColumn;
    @FXML
    private TableColumn<Customer, String> lastNameColumn;
    @FXML
    private TableColumn<Customer, Integer> idColumn;

    private ObservableList<Customer> data = FXCollections.observableArrayList();

    
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
            String firstName = (firstNameTextField.getText()!= null) ? firstNameTextField.getText().trim() : "";
            String lastName = (lastNameTextField.getText()!= null) ? lastNameTextField.getText().trim() : "";
            String phone = (phoneTextField.getText()!= null) ? phoneTextField.getText().trim() : "";
            String email = (emailTextField.getText()!= null) ? emailTextField.getText().trim() : "";
            
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
                System.out.println("\nA new customer was inserted successfully!");
            } else {
                System.out.println("\nInserting customer failed, no rows affected.");
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
    public void searchButton() 
    {
        // Establish a connection to the database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        // Get the input from text fields
        String firstNameInput = (firstNameTextField.getText()!= null) ? firstNameTextField.getText().trim() : "";
        String lastNameInput = (lastNameTextField.getText()!= null) ? lastNameTextField.getText().trim() : "";

        // Start building the SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT first_name, last_name, phone, email, customer_id FROM customers");

        // Check if first name or last name is provided
        boolean hasFirstName = !firstNameInput.isEmpty();
        boolean hasLastName = !lastNameInput.isEmpty();

        // Append conditions to the query based on input
        if (hasFirstName || hasLastName) 
        {
            queryBuilder.append(" WHERE");

            if (hasFirstName) 
            {
                queryBuilder.append(" first_name = ?");
            }

            if (hasLastName) 
            {
                if (hasFirstName) 
                {
                    queryBuilder.append(" AND");
                }
                queryBuilder.append(" last_name = ?");
            }
        }

        // Convert the query to a string
        String query = queryBuilder.toString();

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(query))
        {
            // Set parameters in the prepared statement
            int paramIndex = 1;
            if (hasFirstName) 
            {
                preparedStatement.setString(paramIndex++, firstNameInput);
            }
            if (hasLastName) 
            {
                preparedStatement.setString(paramIndex++, lastNameInput);
            }

            // Execute the query
            ResultSet queryResult = preparedStatement.executeQuery();

            // Clear previous data in the list
            data.clear();

            // Process query results
            while (queryResult.next()) 
            {
                // Retrieve data from the result set
                String firstName = queryResult.getString("first_name");
                String lastName = queryResult.getString("last_name");
                String phone = queryResult.getString("phone");
                String email = queryResult.getString("email");

                int customerId = queryResult.getInt("customer_id");

                // Add retrieved data to the list
                data.add(new Customer(firstName, lastName, phone, email, customerId));
            }

            // Map columns to Customer properties
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

            // Set data to the TableView
            dataDisplayTable.setItems(data);

        } catch (Exception e) 
        {
            // Print stack trace in case of exceptions
            e.printStackTrace();
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
    private void deleteButton()
    {
        Customer selectedPerson = dataDisplayTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) 
        {
            int selectedPersonId = selectedPerson.getCustomerId();
            data.remove(selectedPerson);

            try 
            {
                deletePersonFromDatabase(selectedPersonId);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
    }
       
    private void deletePersonFromDatabase(int id) throws SQLException 
    {
        // Establish a connection to the database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        
        // Start building the SQL query
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM Customers WHERE customer_id = ?");
        
        // Convert the query to a string
        String query = queryBuilder.toString();

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(query)) 
        {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    
    @FXML
    public void editButton() throws IOException, SQLException
    {
        Customer selectedCustomer = dataDisplayTable.getSelectionModel().getSelectedItem();
        
        if(selectedCustomer == null)
        {
            System.out.println("Please select a cusotmer to edit.");
            return;
        }
        
        int selectedCustomerId = selectedCustomer.getCustomerId();
        
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        
        // Start building the SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT first_name, last_name, phone, email, customer_id FROM customers WHERE customer_id = ? ");

        PreparedStatement preparedStatement = connectDB.prepareStatement(queryBuilder.toString());
        preparedStatement.setInt(1, selectedCustomerId);
        ResultSet resultSet = preparedStatement.executeQuery();
    
        StringBuilder quoteQueryBuilder = new StringBuilder("SELECT * FROM blue_forms WHERE customer_id = ? limit 1");
        
        PreparedStatement preparedStatementQuote = connectDB.prepareStatement(quoteQueryBuilder.toString());
        preparedStatementQuote.setInt(1, selectedCustomerId);
        ResultSet resultSetQuote = preparedStatementQuote.executeQuery();     
        
        if(resultSet.next())
        {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String phone = resultSet.getString("phone");
            String email = resultSet.getString("email");            
                        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Secondary.fxml"));
            Parent root = loader.load();
            
            SecondaryController secondaryController = loader.getController();
            secondaryController.setCustomerDetails(firstName, lastName, phone, email, selectedCustomerId);
            
            if(resultSetQuote.next())
            {
                secondaryController.setQuoteData(resultSetQuote);
            }
            
            Stage window = (Stage)editButton.getScene().getWindow();
            window.setScene(new Scene(root, 1645, 1069));
        }
        connectDB.close();
    }
}
