package org.example.brickbreaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.example.brickbreaker.R;
import org.example.brickbreaker.classes.Account;
import org.example.brickbreaker.classes.AccountsDB;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Account account;

    public SignUpActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Read Acc file to update Account.accounts variable
        handler.removeCallbacksAndMessages(null);
        Intent accountIntent = new Intent(this, AccountActivity.class);
        accountIntent.putExtra("org.example.brickbreaker.account", account);
        accountIntent.putExtra("org.example.brickbreaker.update", false);
        startActivity(accountIntent);

        //On signup button click
        findViewById(R.id.signupButton).setOnClickListener(v -> register());
    }

    private String getTextInput(TextInputLayout inputLayout) {
        return Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
    }

    public void register() {
        TextInputLayout firstNameInput = findViewById(R.id.firstname);
        TextInputLayout lastNameInput = findViewById(R.id.lastname);
        TextInputLayout emailInput = findViewById(R.id.email);
        TextInputLayout usernameInput = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);

        String firstName = getTextInput(firstNameInput);
        String lastName = getTextInput(lastNameInput);
        String email = getTextInput(emailInput);
        String username = getTextInput(usernameInput);
        String password = passwordText.getText().toString();

        //Create Account
        handler.removeCallbacksAndMessages(null);
        account = new Account(firstName, lastName, username, password, email, 0);

        //Validate Email, Password and Username
        Account accountFound = Account.searchUsername(this.account.getUsername());
        String alertMessage;

        alertMessage = (accountFound != null) ? "Username is taken" :
                !Account.isValidPassword(email) ? "Password must be between 8 and 20 characters and contain at least one number" :
                        !Account.isValidEmail(email) ? "Invalid email format" :
                                "Signup successful";

        //update alert message
        TextView alertTextView = findViewById(R.id.alertTextView);
        alertTextView.setText(alertMessage);
        System.out.println(alertMessage);

        if (alertMessage.equals("Signup successful")) {
            AccountsDB.addAccount(account);
        }
    }

    private void closeActivity() {
        //Go back to startScreenActivity
        Intent startScreenIntent = new Intent(this, StartScreenActivity.class);
        startScreenIntent.putExtra("org.example.brickbreaker.account", account);
        startActivity(startScreenIntent);

        this.finish(); // close the current activity
    }
}
