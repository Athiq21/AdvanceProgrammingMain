package model;

import java.sql.Timestamp;


public class User {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String otp;
    private Timestamp otpExpiration;
    private boolean isActivated;

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean isIsActivated() {
        return isActivated;
    }


    public void setOtpExpiration(Timestamp otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    public Timestamp getOtpExpiration() {
        return otpExpiration;
    }
}
