package com.mycompany.testingjavafx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer 
{
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleIntegerProperty customerId;


    public Customer(String firstName, String lastName, int customerId)
    { 
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.customerId = new SimpleIntegerProperty(customerId);
    }

    public String getFirstName() 
    {
        return firstName.get();
    }
    
    public String getLastName() 
    {
        return lastName.get();
    }
    
    public int getCustomerId() 
    {
        return customerId.get();
    }
}
