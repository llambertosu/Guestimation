package com.example.admin.guestimation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    public Button button;

    private final int DISPLAY_LENGTH = 5000;

    String username, gamePass, nextCard;
    int[] cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        /*Intent intent2 = getIntent();
        username = intent2.getStringExtra("username");
        gamePass = intent2.getStringExtra("gamePass");
        cards = intent2.getIntArrayExtra("cards");
        nextCard = intent2.getStringExtra("nextCard");

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.putExtra("username", username);
                mainIntent.putExtra("gamePass", gamePass);
                mainIntent.putExtra("cards", cards);
                mainIntent.putExtra("nextCard", nextCard);
                startActivity(mainIntent);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run ()
            {

            }
        }, DISPLAY_LENGTH);*/
    }
}

