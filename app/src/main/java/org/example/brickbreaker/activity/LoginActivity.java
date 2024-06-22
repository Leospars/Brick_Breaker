package org.example.brickbreaker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.example.brickbreaker.R;

public class LoginActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Context context;
    Account account;

    public LoginActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        //On login button click
        findViewById(R.id.loginButton).setOnClickListener(v -> login());
    }

    public void login() {
        //Get account info from the login_activity in account objects
        TextInputLayout usernameInput = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);

        String username = usernameInput.getEditText().getText().toString();
        String password = passwordText.getText().toString();

        //Update Account.accounts variable
        handler.removeCallbacksAndMessages(null);
        Intent accountIntent = new Intent(this, AccountActivity.class);
        activityResultLauncher.launch(accountIntent); // Use the launcher to start AccountActivity

        //Run game with the account info
        account = Account.searchUsername(username);
        if (account == null) {
            System.out.println("Username not found");
            return;
        } else if (account.getPassword().equals(password)) {
            System.out.println("Login successful");
            Intent intent = new Intent(this, MainActivity.class);
            this.finish(); // close the current activity
        } else {
            System.out.println("Login failed");
        }

        //Go back to startScreenActivity
        Intent startScreenIntent = new Intent(this, StartScreenActivity.class);
        startScreenIntent.putExtra("org.example.brickbreaker.account", account);
        startActivity(startScreenIntent);

        this.finish(); // close the current activity
    }


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // The Intent that was used to start the Activity is available
                    Intent intent = result.getData();
                    // Handle the Intent
                }
            });
}
