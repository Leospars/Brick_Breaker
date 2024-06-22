package org.example.brickbreaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.example.brickbreaker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Keep Screen On
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Start Button
        ImageView startButtonImage = findViewById(R.id.startButton2);
        startButtonImage.setOnClickListener(this::start);
    }


    public void start(View view) {
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
        this.finish();
    }
}