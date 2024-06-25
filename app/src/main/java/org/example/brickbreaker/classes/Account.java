package org.example.brickbreaker.classes;

import android.accounts.AccountsException;
import android.os.Build;
import android.security.identity.IdentityCredentialException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.example.brickbreaker.activity.AccountActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class Account implements Serializable {
    //Package private
    int highScore;
    String firstName, lastName, username, email;
    private int ID = -1;
    private String password;
    private static List<Account> accounts = AccountsDB.getAccounts();
    private static final String TAG = "Account";

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
        setUsername(username);
        setPassword(password);
        setEmail(email);
        this.highScore = highScore;
        this.ID = (!accounts.isEmpty()) ? (accounts.get(accounts.size()-1).ID + 1) : -1;;
    }

    public Account() {
        this("guest", "", "Guest", "", "x@y.z", 0);
    }

    public static Account searchUsername(String username) {
        for (Account account : accounts) {
            if (account.username.equalsIgnoreCase(username)) {
                Log.d(TAG, "Username found");
                return account;
            }
        }
        return null;
    }

    //Getters
    public int getHighScore() {
        return highScore;
    }

    public int getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    //Setters
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //Validation
    public static boolean isValidEmail(String email){
        try {
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
            else throw new AccountsException("Invalid email format");
        }
        catch (Exception e){
            return false;
        }
    }

    ///Password must be between 8 and 20 characters and contain at least one number
    public static boolean isValidPassword(String password) {
        try {
            if (password.length() >= 8 && password.length() <= 20
                    && password.matches(".*\\d.*")) {
                return true;
            }

            throw new AccountsException("Password must be between 8 and 20 characters and contain at least one number");
        }
        catch (Exception e){
            return false;
        }
    }

    public static boolean isValidUsername(String username) {
        try {
            if (username.length() >= 6 && username.length() <= 20) {
                return true;
            }
            throw new AccountsException("Username must be between 6 and 20 characters");
        }
        catch (Exception e){
            return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{ ID: " + ID + "," +
                "First Name: " + firstName + "," +
                "Last Name: " + lastName + "," +
                "Username: " + username + "," +
                "Password: " + password + "," +
                "Email: " + email + "," +
                "High Score: " + highScore + " }";
    }

}
