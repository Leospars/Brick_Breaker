package org.example.brickbreaker.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import org.example.brickbreaker.GameView;
import org.example.brickbreaker.R;
import org.example.brickbreaker.classes.Account;
import org.example.brickbreaker.classes.AccountsDB;

import java.util.List;

public class GameOverActivity extends AppCompatActivity {
    Context context;
    Handler handler = new Handler();
    Account currentUser;
    int points = 0;

    public GameOverActivity() {
        super();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        points = intent.getIntExtra("org.example.brickbreaker.points", 0);
        boolean gameWon = intent.getBooleanExtra("org.example.brickbreaker.gameWon", false);
        currentUser = (Account) intent.getSerializableExtra("org.example.brickbreaker.account");

        if (currentUser == null)
            currentUser = new Account();

        //Update highScore
        if (points > currentUser.getHighScore()) {
            currentUser.setHighScore(points);
            updateAccount(currentUser);
        }

        //Display if game was won or lost
        TextView gameOverTextView = findViewById(R.id.alertTitle);
        TextView msgTextView = findViewById(R.id.messageTextView);
        if (gameWon) {
            gameOverTextView.setText("You Won!");
            msgTextView.setBackground(AppCompatResources.getDrawable(this, R.drawable.clapping));

            //delay removing the background and displaying text
            handler.postDelayed(() -> msgTextView.setBackground(null), 3000);
            msgTextView.setText("Congratulations!\n" + currentUser.getUsername());
        } else {
            gameOverTextView.setText("Game Over");
            msgTextView.setText("Try Again Next Time!\n" + currentUser.getUsername());
        }

        //Display the score
        TextView score = findViewById(R.id.pointsTextView);
        score.setText("Score: " + points + "\t\t\t\t" + "High Score: " + currentUser.getHighScore());

        // Start over game
        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(v -> playAgain());

        // Show Leaderboard
        Button leaderBoardButton = findViewById(R.id.leaderBoardButton);
        leaderBoardButton.setOnClickListener(this::showLeaderBoard);
    }

    private void showLeaderBoard(View v) {
        TextView leaderBoard = findViewById(R.id.leaderboardData);
        leaderBoard.setVisibility(TextView.VISIBLE);

        StringBuilder leaderBoardData = getLeaderBoard();
        leaderBoard.setText(leaderBoardData.toString());
    }

    @NonNull
    private StringBuilder getLeaderBoard() {
        StringBuilder leaderBoardData = new StringBuilder();

        //Update the static variable Account.accounts using accountActivity
        try {
            System.out.println("Requesting user account data");
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("org.example.brickbreaker.account", currentUser);
            intent.putExtra("org.example.brickbreaker.update", false);
            //Wait on account intent to complete first
            handler.postDelayed(() -> startActivity(intent), 1000);
        } catch (Exception e) {
            Log.e("GameOverActivity", "Error starting AccountActivity", e);
        }

        //Retrieve accounts from database
        List<Account> accounts = AccountsDB.getAccounts();
        if (accounts.isEmpty()) {
            leaderBoardData.append("No data found");
        } else {
            for (Account account : accounts)
                leaderBoardData.append(account.getUsername()).append("\t\t\t\t")
                        .append(account.getHighScore()).append("\n");
        }
        System.out.println(leaderBoardData);
        return leaderBoardData;
    }

    public void playAgain() {
        System.out.println("Play again");
        setContentView(new GameView(this, currentUser));
    }

    public void updateAccount(Account account) {
        try {
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("org.example.brickbreaker.account", account);
            intent.putExtra("org.example.brickbreaker.update", true);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("GameOverActivity", "Error updating Account", e);
        }
    }
}
