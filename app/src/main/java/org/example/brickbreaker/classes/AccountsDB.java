package org.example.brickbreaker.classes;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

//Setup Firebase Database
public class AccountsDB {
    private static final String TAG = "AccountsDB";
    //Firebase Database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference myRef = database.getReference("accounts");
    private static DataSnapshot accountsSnapshot = null;

    AccountsDB(){
        getAccounts();
    }

    public static void addAccount(Account account) {
        myRef.child(account.getUsername()).setValue(account);
    }

    public static void updateAccount(Account account) {
        myRef.child(account.getUsername()).setValue(account);
    }

    public static void deleteAccount(Account account) {
        myRef.child(account.getUsername()).removeValue();
    }


    /**
     * Whenever the online realtime database is update the {@link Account#allAccounts } static variable
     * is automatically updated with the new data.
     * @return {@link Account#allAccounts} a list of all accounts in the database
     */
    public static List<Account> getAccounts() {
        ExecutorService threadpool = Executors.newCachedThreadPool();
        Future<List<Account>> futureTask = threadpool.submit(() -> {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "Account Snapshot is taken : " + dataSnapshot + "\n");
                    accountsSnapshot = dataSnapshot;
                    Account.allAccounts.clear();
                    for (DataSnapshot snapshot : accountsSnapshot.getChildren()) {
                        Account account = snapshot.getValue(Account.class);
                        Account.allAccounts.add(account);
                    }
                    Log.d(TAG, "Accounts retrieved: " + Account.allAccounts + "\n");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError dbError) {
                    Log.w(TAG,"The read failed: " + dbError.getCode());
                    Log.w(TAG, "Failed to read value.", dbError.toException());
                }
            });
            return Account.allAccounts;
        });

        //Wait a maximum of 40 seconds to get data from database then return error
        try {
            long startTime = System.currentTimeMillis();
            List<Account> result = futureTask.get(40, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "Time taken to get data from database: " + (endTime - startTime) + " milliseconds");
            return result;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e(TAG, "Error getting data from database: " + e.getMessage());
            return new ArrayList<Account>();
        }
    }
}
