package org.example.brickbreaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.example.brickbreaker.R;

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

    public void register() {
        TextInputLayout firstNameInput = findViewById(R.id.firstname);
        TextInputLayout lastNameInput = findViewById(R.id.lastname);
        TextInputLayout emailInput = findViewById(R.id.email);
        TextInputLayout usernameInput = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);

        String firstName = firstNameInput.getEditText().getText().toString();
        String lastName = lastNameInput.getEditText().getText().toString();
        String email = emailInput.getEditText().getText().toString();
        String username = usernameInput.getEditText().getText().toString();
        String password = passwordText.getText().toString();

        //Create Account
        handler.removeCallbacksAndMessages(null);
        account = new Account(firstName, lastName, username, password, email, 0);

        //Validate Email, Password and Username
        Account accountFound = Account.searchUsername(this.account.getUsername());
        String alertMessage;

        if (accountFound != null) {
            alertMessage = "Username is taken";
        } else if (Account.isValidEmail(email) && Account.isValidPassword(password) && Account.isValidUsername(username)) {
            alertMessage = "Signup successful";
        } else {
            alertMessage = "Login failed";
        }

        //update alert message
        TextView alertTextView = findViewById(R.id.alertTextView);
        alertTextView.setText(alertMessage);
        System.out.println(alertMessage);

        if(alertMessage.equals("Signup successful")) {

            //Update Account.accounts variable
            Intent accountIntent = new Intent(this, AccountActivity.class);
            accountIntent.putExtra("org.example.brickbreaker.account", account);
            accountIntent.putExtra("org.example.brickbreaker.update", true);
            startActivity(accountIntent);

            //Pause for 3 seconds then close activity
            handler.postDelayed(this::closeActivity, 3000);
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
