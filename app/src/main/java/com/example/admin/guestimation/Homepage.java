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

    public Button onHostPressed, joinButton;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        onHostPressed = findViewById(R.id.onHostPressed);
        joinButton = findViewById(R.id.joinButton);

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
                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent);
            }
        });
    }
}
