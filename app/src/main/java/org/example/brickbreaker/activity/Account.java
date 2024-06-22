package org.example.brickbreaker.activity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    public static ArrayList<Account> accounts = new ArrayList<>();
    //Package private
    int highScore;
    String firstName, lastName, username, email;
    private int ID = -1;
    private String password;

    /**
     * @implNote Remember to start {@link AccountActivity AccountActivity} after calling this constructor to update Accounts
     * file and Account.accounts.
     * @code Intent intent = new Intent(this, AccountActivity.class);
     * intent.putExtra("org.example.brickbreaker.account", account);
     * intent.putExtra("org.example.brickbreaker.update", true);
     * startActivity(intent);
     * </code>
     */
    public Account(String firstName, String lastName, String username, String password, String email, int highScore) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.highScore = highScore;
        this.ID = (!accounts.isEmpty()) ? (accounts.get(accounts.size()-1).ID + 1) : -1;
    }

    public Account() {
        this.username = "Guest";
        this.email = "x@y.z";
        this.highScore = 0;
        this.ID = -1;
    }

    public static Account searchUsername(String username) {
        for (Account account : accounts) {
            if (account.username.equalsIgnoreCase(username)) {
                System.out.println("Username found");
                return account;
            }
        }
        return null;
    }

    //Getters
    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public int getHighScore() {
        return highScore;
    }

    void setHighScore(int highScore) {
            this.highScore = highScore;
    }

    public int getID() {
        return ID;
    }

    //Setters
    void setID(int ID) {
        this.ID = ID;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    static boolean isValidUsername(String username){
        if (username.length() >= 6 && username.length() <= 20) {
            return true;
        }
        System.out.println("Username must be between 6 and 20 characters");
        return false;
    }

    static boolean isValidEmail(String email){
        for (int i = 0; i < email.length(); i++) {
            char c = email.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '@' && c != '.') {
                System.out.println("Email must only contain letters, numbers, underscores, @ and periods");
                return false;
            }
        }

        if (email.matches("^(.+)@(.+)$")) {
            return true;
        }

        System.out.println("Invalid email format");
        return false;
    }

    static boolean isValidPassword(String password){
        //Password must be between 8 and 20 characters and contain at least one number
        if (password.length() >= 8 && password.length() <= 20
                && password.matches(".*\\d.*")) {
            return true;
        }
        System.out.println("Password must be between 8 and 20 characters and contain at least one number");
        return false;
    }

    void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        String accountInfo = "ID: " + ID + "," +
                "First Name: " + firstName + "," +
                "Last Name: " + lastName + "," +
                "Username: " + username + "," +
                "Password: " + password + "," +
                "Email: " + email + "," +
                "High Score: " + highScore + "\n";
        return accountInfo;
    }

    public static String allAccounts() {
        StringBuilder allAccounts = new StringBuilder();
        for (Account account : accounts) allAccounts.append(account.toString());
        return allAccounts.toString();
    }
}
