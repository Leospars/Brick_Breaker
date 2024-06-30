package org.example.brickbreaker.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import org.example.brickbreaker.GameView;
import org.example.brickbreaker.R;
import org.example.brickbreaker.classes.Account;
import org.example.brickbreaker.classes.AccountsDB;

import java.util.List;

public class GameOverActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Account currentUser;
    int points = 0;
    final static String TAG = "GameOverActivity";

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
        if (currentUser == null) currentUser = new Account();

        //Update highScore
        if (points > currentUser.getHighScore() ) {
            currentUser.setHighScore(points);
            if(!currentUser.getUsername().equals("guest"))
                AccountsDB.updateAccount(currentUser);
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
        leaderBoardButton.setOnClickListener((v) -> setLeaderBoard());
    }

    private void writeToLeaderBoard(String leaderBoardData) {
        TextView leaderBoard = findViewById(R.id.leaderboardData);
        leaderBoard.setVisibility(TextView.VISIBLE);
        leaderBoard.setText(leaderBoardData);
    }

    private void setLeaderBoard() {
        //Display LeaderBoard
        TextView leaderBoard = findViewById(R.id.leaderboardData);
        leaderBoard.setVisibility(TextView.VISIBLE);

        List<Account> accounts = Account.allAccounts;
        Log.d(TAG, "setLeaderBoard: Accounts Loaded:" + accounts);

        if (accounts.isEmpty()) {
            writeToLeaderBoard("No data found");
            return;
        }

        StringBuilder highScoreData = new StringBuilder();
        for (Account account : accounts)
            highScoreData.append(account.getUsername()).append("\t\t\t\t")
                    .append(account.getHighScore()).append("\n");
        writeToLeaderBoard(highScoreData.toString());
    }

    public void playAgain() {
        System.out.println("Play again");
        this.finish();
        Intent intent = new Intent(this, StartScreenActivity.class);
        intent.putExtra("org.example.brickbreaker.account", currentUser);
        intent.putExtra("StartScreenActivity.restart", true);
        startActivity(intent);
    }
}
