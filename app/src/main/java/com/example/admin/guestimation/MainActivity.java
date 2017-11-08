package com.example.admin.guestimation;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    //Declaring layout button, edit texts
    public EditText answer;
    public Button submit;
    public TextView question;

    //Declaring connection variables
    public Connection con;
    String un,pass,db,ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get values from the button, ExitText, and TextView
        answer = (EditText) findViewById(R.id.answerBox);
        submit = (Button) findViewById(R.id.submitButton);
        question = (TextView) findViewById(R.id.questionView);

        //Declare Server ip, userrname, database name, and password
        ip = "guestimation.database.windows.net:1433";
        db = "guestimation";
        un = "user";
        pass = "Cowboys2017";

    }

}
