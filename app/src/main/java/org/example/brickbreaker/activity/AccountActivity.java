package org.example.brickbreaker.activity;

import static org.example.brickbreaker.activity.Account.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    Context context;
    Handler handler = new Handler();
    Account currentUser = new Account();

    public AccountActivity() {
        super();
    }

    public AccountActivity(Account account) {
        super();
        this.currentUser = account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if accounts should be updated and update accounts file
        Intent intent = getIntent();
        currentUser = (getIntent().getSerializableExtra("org.example.brickbreaker.account") != null) ?
                (Account) getIntent().getSerializableExtra("org.example.brickbreaker.account") : currentUser;
        boolean update = intent.getBooleanExtra("org.example.brickbreaker.update", false);

        System.out.println("Accessing Database: " + currentUser.getUsername());
        System.out.println("Update: " + update);

        accounts = readAccountsFromAssets();

        if (update) {
            //update file and accounts static and set update to false
            updateAccountsFile(currentUser);
            intent.putExtra("org.example.brickbreaker.update", false);
            accounts = readAccountsFromAssets();
            System.out.println("Account updated");
            System.out.println("All Accounts:\n" + Account.allAccounts());
            System.out.println("File Conetent:\n" + Account.allAccounts());
        }
        this.finish();
    }

    public void updateAccountsFile(Account account) {
        try {
            Account.accounts.add(account);
            FileOutputStream outputStream = ((Context) this).openFileOutput("Accounts.csv", Context.MODE_PRIVATE);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

            account.setID(accounts.get(accounts.size() - 1).getID() + 1); //Next ID
            String data = account.getID() + "," + account.firstName + "," + account.lastName + "," +
                    account.username + "," + account.getPassword() + "," + account.email + "," +
                    account.highScore + "\n";
            bufferedWriter.write(data);
            bufferedWriter.close();

            System.out.println("Account: " + account + " added to Account.accounts: " + accounts);
            System.out.println("Accounts in File: " + readAccountsFromAssets());

        } catch (Exception e) {
            Log.e("GameOverActivity", "Error updating Account.accounts", e);
        }
    }

    public ArrayList<Account> readAccountsFromAssets() {
        AssetManager assetManager = this.getAssets();

        accounts = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open("Accounts.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null) {
                row++;
                if (row == 1) continue;

                String[] values = line.split(",");
                Account account = new Account();
                account.setID(Integer.parseInt(values[0]));
                account.setFirstName(values[1]);
                account.setLastName(values[2]);
                account.setUsername(values[3]);
                account.setPassword(values[4]);
                account.setEmail(values[5]);
                account.setHighScore(Integer.parseInt(values[6]));
                accounts.add(account);
            }
            System.out.println("Accounts read from file: " + accounts.size());
            System.out.println("All Accounts:\n" + Account.allAccounts());
        } catch (Exception e) {
            Log.e("AccountActivity", "Error reading to update Account.accounts", e);
        }
        return accounts;
    }


}
