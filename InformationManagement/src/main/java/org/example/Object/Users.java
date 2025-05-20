package org.example.Object;

import java.util.Date;

public class Users {
    private String firstname;
    private String lastname;
    private String email_address;
    private String password;
    private Date birthday;

    public Users(String firstname, String lastname, String email_address, String password, Date birthday) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email_address = email_address;
        this.password = password;
        this.birthday = birthday;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail_address() {
        return email_address;
    }

    public String getPassword() {
        return password;
    }

    public java.sql.Date getBirthday() {
        return new java.sql.Date(birthday.getTime());
    }
}

