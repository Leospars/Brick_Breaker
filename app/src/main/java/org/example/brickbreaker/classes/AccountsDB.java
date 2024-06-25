package org.example.brickbreaker.classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

//Setup Firebase Database
public class AccountsDB {
    private static final String TAG = "AccountsDB";
    //Firebase Database
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference myRef = database.getReference("accounts");

    public static void addAccount(Account account) {
        myRef.child(account.getUsername()).setValue(account);
    }

    public static void updateAccount(Account account) {
        myRef.child(account.getUsername()).setValue(account);
    }

    public static void deleteAccount(Account account) {
        myRef.child(account.getUsername()).removeValue();
    }

    public static List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //DataSnapshot is of Accounts and each users username is a child storing the account
                // details. i.e. Accounts/username1 & username1: {account details}
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Account account = userSnapshot.getValue(Account.class);
                    accounts.add(account);
                    Log.d(TAG, "Account retrieved: " + account + "\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbError) {
                Log.w(TAG,"The read failed: " + dbError.getCode());
                Log.w(TAG, "Failed to read value.", dbError.toException());
            }
        });
        return accounts;
    }

    public static void main(String[] args) {
        addAccount(new Account("Leon", "Gayle", "leospars", "smnImportant123",
                "leon.gayle012@gmail.com", 120));
        getAccounts();
    }
}
