package com.example.admin.guestimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by admin on 11/1/17.
 */

public class Homepage extends AppCompatActivity{

    public Button onHostPressed, joinButton, questionsButton;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        onHostPressed = findViewById(R.id.onHostPressed);
        joinButton = findViewById(R.id.joinButton);
        questionsButton = findViewById(R.id.questionsButton);

        onHostPressed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HostGame.class);
                startActivity(intent);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent2);
            }
        });

        questionsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), QuestionsActivity.class);
                startActivity(intent3);
            }
        });
    }
}
