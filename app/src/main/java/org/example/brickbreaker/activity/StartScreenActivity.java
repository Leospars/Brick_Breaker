package org.example.brickbreaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.example.brickbreaker.GameView;
import org.example.brickbreaker.R;
import org.example.brickbreaker.classes.Account;


public class StartScreenActivity extends AppCompatActivity {

    static StringBuilder leaderBoardData;
    Account[] accounts = new Account[10];
    Account currentUser = new Account("Guest", "User", "guest", "", "", 0);

    public StartScreenActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        //Start User Signed in
        currentUser = (getIntent().getSerializableExtra("org.example.brickbreaker.account") != null) ?
                (Account) getIntent().getSerializableExtra("org.example.brickbreaker.account") : currentUser;

        assert currentUser != null : "Current User is null"; // This is really not possible because currentUser is initialized in the constructor
        //but the IDE doesn't know that and reccomended it.

        //Remove Login Buttons
        LinearLayout loginButtons = findViewById(R.id.loginButtonsLayout);
        if (currentUser.getID() >= 1) {
            System.out.println(currentUser.getUsername() + " is logged in");
            loginButtons.setVisibility(View.GONE);
        } else {
            loginButtons.setVisibility(View.VISIBLE);
        }

        //Show User Info
        TextView msgTxtView = findViewById(R.id.messageTextView);
        String welcomeMessage = getResources().getString(R.string.welcome_message, currentUser.getUsername());
        msgTxtView.setText(welcomeMessage);

        //Show High Score
        TextView highScoreTxtView = findViewById(R.id.highScoreTextView);
        String highScoreMessage = getResources().getString(R.string.highScore, currentUser.getHighScore());
        highScoreTxtView.setText(highScoreMessage);

        //Start Button
        ImageView startButtonImage = findViewById(R.id.startButton2);
        startButtonImage.setOnClickListener(this::start);

        //Login Button
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this::login);

        //Sign Up Button
        Button signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(this::signUp);
    }

    public void start(View view) {
        GameView gameView = new GameView(this, currentUser);
        setContentView(gameView);
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
