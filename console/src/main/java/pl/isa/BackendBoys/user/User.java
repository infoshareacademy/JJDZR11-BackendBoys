package pl.isa.BackendBoys.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private final String loginEmail;
    private final String password;
    private final String name;
    private final String city;
    private final String contactNumber;

    public User() {
        this.name = null;
        this.city = null;
        this.contactNumber = null;
        this.loginEmail = null;
        this.password = null;
    }


    public User(String name, String contactNumber, String loginEmail, String password, String city) {
        this.name = name;
        this.city = city;
        this.contactNumber = contactNumber;
        this.loginEmail = loginEmail;
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return name + " (" + loginEmail + ")";
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

}
