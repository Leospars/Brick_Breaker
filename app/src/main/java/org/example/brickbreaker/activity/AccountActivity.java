package org.example.brickbreaker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.example.brickbreaker.classes.Account;
import org.example.brickbreaker.classes.AccountsDB;

import java.util.List;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "AccountActivity";
    Context context;
    Handler handler = new Handler();

    public AccountActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if accounts should be updated and update accounts file
        Intent intent = getIntent();
        Account currentUser = (Account) getIntent().getSerializableExtra("org.example.brickbreaker.account");
        currentUser = (currentUser != null) ? currentUser : new Account();
        boolean update = intent.getBooleanExtra("org.example.brickbreaker.update", false);

        System.out.println("Accessing Database: " + currentUser.getUsername());
        System.out.println("Update: " + update);

        if (update) {
            //update file and accounts static and set update to false
            AccountsDB.updateAccount(currentUser);

            intent.putExtra("org.example.brickbreaker.update", false);
            List<Account> accounts = AccountsDB.getAccounts();
            Log.d(TAG, "Account updated");
            Log.d(TAG, "All Accounts:\n" + accounts);
        }
        this.finish();
    }


/*  public List<Account> readAccountsFromAssets() {
        AssetManager assetManager = this.getAssets();

        accounts = new ArrayList<Account>();
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
*/

}
