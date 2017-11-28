package com.example.admin.guestimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by admin on 11/1/17.
 */

public class Homepage extends AppCompatActivity{
    //Not sure if this is where this class needs to be,
    //but it is the random number generator for the gamekey.




    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
    }

    public void onHostButton(View v) {
        Intent intent = new Intent(getApplicationContext(), HostGame.class);
        startActivity(intent);
    }
}
