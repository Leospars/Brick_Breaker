package org.example.brickbreaker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.units.qual.A;
import org.example.brickbreaker.R;
import org.example.brickbreaker.classes.Account;
import org.example.brickbreaker.classes.AccountsDB;

public class LoginActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Context context;
    Account account;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // The Intent that was used to start the Activity is available
                    Intent intent = result.getData();
                    // Handle the Intent
                }
            });

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

        System.out.println("Accounts registered: " + Account.allAccounts);
        account = Account.searchUsername(username);
        if (account == null) {
            setAlert("Username not found");
        } else if (account.getPassword().equals(password)) {
            setAlert("Login successful");
            closeActivity();
        } else {
            setAlert("Login failed");
        }
    }

    private void closeActivity() {
        this.finish();

        //Go back to startScreenActivity with the account info
        Intent startScreenIntent = new Intent(this, StartScreenActivity.class);
        startScreenIntent.putExtra("org.example.brickbreaker.account", account);
        handler.postDelayed(() -> startActivity(startScreenIntent), 2000);
    }

    private void setAlert(String message) {
        //update alert message
        TextView alertTextView = findViewById(R.id.alertTextView);
        alertTextView.setText(message);
        System.out.println(message);
    }
}
