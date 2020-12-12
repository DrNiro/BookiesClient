package com.dts.bookies;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class UserCredentials {

    private String email;
    private String hashedPassword;

    public UserCredentials() {

    }

    public UserCredentials(String email, String password) {
        setEmail(email);
        setPasswordHashed(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    private void setPasswordHashed(String password) {
        this.hashedPassword = hashString(password);
    }

    private String hashString(String password) {
        return Hashing.sha256().hashString(password, Charset.defaultCharset()).toString();
    }

    public boolean isCorrectPassword(String password) {
        return this.getHashedPassword().equals(hashString(password));
    }


//    sha1().hashString(yourValue, Charset.defaultCharset());

}
