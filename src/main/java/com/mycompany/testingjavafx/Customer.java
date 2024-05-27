package com.mycompany.testingjavafx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer 
{
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleIntegerProperty customerId;


    public Customer(String firstName, String lastName, String phone, String email, int customerId)
    { 
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.customerId = new SimpleIntegerProperty(customerId);
    }

    public String getPhone() 
    {          
        return phone.get();
    }

    public String getEmail() 
    {
        return email.get();
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
