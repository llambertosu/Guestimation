package com.example.admin.guestimation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class ScoreActivity extends AppCompatActivity {

    private final int DISPLAY_LENGTH = 5000;

    public Connection con;

    public TextView userResponse1, userResponse2, userResponse3, userResponse4, userResponse5, userResponse6, userResponse7, userResponse8, userResponse9,userResponse10;
    public TextView response1, response2, response3, response4, response5, response6, response7, response8, response9, response10;
    public TextView score1, score2, score3, score4, score5, score6, score7, score8, score9, score10;
    public Button back;

    String username, gamePass, nextCard;
    //sets arrays for displaying responses, usernames, and scores
    String[] responses = new String[10];
    String[] players = new String[10];
    String[] scores = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        back = (Button) findViewById(R.id.button);
        score1 = (TextView) findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        score3 = (TextView) findViewById(R.id.score3);
        score4 = (TextView) findViewById(R.id.score4);
        score5 = (TextView) findViewById(R.id.score5);
        score6 = (TextView) findViewById(R.id.score6);
        score7 = (TextView) findViewById(R.id.score7);
        score8 = (TextView) findViewById(R.id.score8);
        score9 = (TextView) findViewById(R.id.score9);
        score10 = (TextView) findViewById(R.id.score10);
        score1.setVisibility(View.INVISIBLE);
        score2.setVisibility(View.INVISIBLE);
        score3.setVisibility(View.INVISIBLE);
        score4.setVisibility(View.INVISIBLE);
        score5.setVisibility(View.INVISIBLE);
        score6.setVisibility(View.INVISIBLE);
        score7.setVisibility(View.INVISIBLE);
        score8.setVisibility(View.INVISIBLE);
        score9.setVisibility(View.INVISIBLE);
        score10.setVisibility(View.INVISIBLE);
        userResponse1 = (TextView) findViewById(R.id.userResponse1);
        userResponse2 = (TextView) findViewById(R.id.userResponse2);
        userResponse3 = (TextView) findViewById(R.id.userResponse3);
        userResponse4 = (TextView) findViewById(R.id.userResponse4);
        userResponse5 = (TextView) findViewById(R.id.userResponse5);
        userResponse6 = (TextView) findViewById(R.id.userResponse6);
        userResponse7 = (TextView) findViewById(R.id.userResponse7);
        userResponse8 = (TextView) findViewById(R.id.userResponse8);
        userResponse9 = (TextView) findViewById(R.id.userResponse9);
        userResponse10 = (TextView) findViewById(R.id.userResponse10);
        userResponse1.setVisibility(View.INVISIBLE);
        userResponse2.setVisibility(View.INVISIBLE);
        userResponse3.setVisibility(View.INVISIBLE);
        userResponse4.setVisibility(View.INVISIBLE);
        userResponse5.setVisibility(View.INVISIBLE);
        userResponse6.setVisibility(View.INVISIBLE);
        userResponse7.setVisibility(View.INVISIBLE);
        userResponse8.setVisibility(View.INVISIBLE);
        userResponse9.setVisibility(View.INVISIBLE);
        userResponse10.setVisibility(View.INVISIBLE);
        response1 = (TextView) findViewById(R.id.response1);
        response2 = (TextView) findViewById(R.id.response2);
        response3 = (TextView) findViewById(R.id.response3);
        response4 = (TextView) findViewById(R.id.response4);
        response5 = (TextView) findViewById(R.id.response5);
        response6 = (TextView) findViewById(R.id.response6);
        response7 = (TextView) findViewById(R.id.response7);
        response8 = (TextView) findViewById(R.id.response8);
        response9 = (TextView) findViewById(R.id.response9);
        response10 = (TextView) findViewById(R.id.response10);
        response1.setVisibility(View.INVISIBLE);
        response2.setVisibility(View.INVISIBLE);
        response3.setVisibility(View.INVISIBLE);
        response4.setVisibility(View.INVISIBLE);
        response5.setVisibility(View.INVISIBLE);
        response6.setVisibility(View.INVISIBLE);
        response7.setVisibility(View.INVISIBLE);
        response8.setVisibility(View.INVISIBLE);
        response9.setVisibility(View.INVISIBLE);
        response10.setVisibility(View.INVISIBLE);

        //calls the CheckAnswer method
        CheckAnswers checkAnswers = new CheckAnswers();
        checkAnswers.execute("");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //sets the variables and starts the MainActivity class over
                Intent backToMain = new Intent(getApplicationContext(), MainActivity.class);
                backToMain.putExtra("username", username);
                backToMain.putExtra("gamePass", gamePass);
                //closes the ScoreActivity class so that it can be reused
                finish();
                startActivity(backToMain);
            }
        });
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run ()
            {
                Intent backtoMain = new Intent(getApplicationContext(), MainActivity.class);
                backtoMain.putExtra("username", username);
                backtoMain.putExtra("gamePass", gamePass);
                startActivity(backtoMain);
            }
        }, DISPLAY_LENGTH);*/

    }

    //pulls all responses from the database then sets the correct textviews visible for users without null responses
    public class CheckAnswers extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {
            //gets variables passed through from MainActivity intent
            Intent getMainIntent = getIntent();
            username = getMainIntent.getStringExtra("username");
            gamePass = getMainIntent.getStringExtra("gamePass");
        }

        @Override
        protected void onPostExecute(String r)
        {
            if (players[9] != null && responses[9] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                userResponse4.setText(players[3]);
                userResponse5.setText(players[4]);
                userResponse6.setText(players[5]);
                userResponse7.setText(players[6]);
                userResponse8.setText(players[7]);
                userResponse9.setText(players[8]);
                userResponse10.setText(players[9]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                response4.setText(responses[3]);
                response5.setText(responses[4]);
                response6.setText(responses[5]);
                response7.setText(responses[6]);
                response8.setText(responses[7]);
                response9.setText(responses[8]);
                response10.setText(responses[9]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                userResponse4.setVisibility(View.VISIBLE);
                userResponse5.setVisibility(View.VISIBLE);
                userResponse6.setVisibility(View.VISIBLE);
                userResponse7.setVisibility(View.VISIBLE);
                userResponse8.setVisibility(View.VISIBLE);
                userResponse9.setVisibility(View.VISIBLE);
                userResponse10.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                response4.setVisibility(View.VISIBLE);
                response5.setVisibility(View.VISIBLE);
                response6.setVisibility(View.VISIBLE);
                response7.setVisibility(View.VISIBLE);
                response8.setVisibility(View.VISIBLE);
                response9.setVisibility(View.VISIBLE);
                response10.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
                score5.setVisibility(View.VISIBLE);
                score6.setVisibility(View.VISIBLE);
                score7.setVisibility(View.VISIBLE);
                score8.setVisibility(View.VISIBLE);
                score9.setVisibility(View.VISIBLE);
                score10.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score4.setText(scores[3].toString());
                score5.setText(scores[4].toString());
                score6.setText(scores[5].toString());
                score7.setText(scores[6].toString());
                score8.setText(scores[7].toString());
                score9.setText(scores[8].toString());
                score10.setText(scores[9].toString());
            }
            else if (players[8] != null && responses[8] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                userResponse4.setText(players[3]);
                userResponse5.setText(players[4]);
                userResponse6.setText(players[5]);
                userResponse7.setText(players[6]);
                userResponse8.setText(players[7]);
                userResponse9.setText(players[8]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                response4.setText(responses[3]);
                response5.setText(responses[4]);
                response6.setText(responses[5]);
                response7.setText(responses[6]);
                response8.setText(responses[7]);
                response9.setText(responses[8]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                userResponse4.setVisibility(View.VISIBLE);
                userResponse5.setVisibility(View.VISIBLE);
                userResponse6.setVisibility(View.VISIBLE);
                userResponse7.setVisibility(View.VISIBLE);
                userResponse8.setVisibility(View.VISIBLE);
                userResponse9.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                response4.setVisibility(View.VISIBLE);
                response5.setVisibility(View.VISIBLE);
                response6.setVisibility(View.VISIBLE);
                response7.setVisibility(View.VISIBLE);
                response8.setVisibility(View.VISIBLE);
                response9.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
                score5.setVisibility(View.VISIBLE);
                score6.setVisibility(View.VISIBLE);
                score7.setVisibility(View.VISIBLE);
                score8.setVisibility(View.VISIBLE);
                score9.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score4.setText(scores[3].toString());
                score5.setText(scores[4].toString());
                score6.setText(scores[5].toString());
                score7.setText(scores[6].toString());
                score8.setText(scores[7].toString());
                score9.setText(scores[8].toString());
            }
            else if (players[7] != null && responses[7] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                userResponse4.setText(players[3]);
                userResponse5.setText(players[4]);
                userResponse6.setText(players[5]);
                userResponse7.setText(players[6]);
                userResponse8.setText(players[7]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                response4.setText(responses[3]);
                response5.setText(responses[4]);
                response6.setText(responses[5]);
                response7.setText(responses[6]);
                response8.setText(responses[7]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                userResponse4.setVisibility(View.VISIBLE);
                userResponse5.setVisibility(View.VISIBLE);
                userResponse6.setVisibility(View.VISIBLE);
                userResponse7.setVisibility(View.VISIBLE);
                userResponse8.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                response4.setVisibility(View.VISIBLE);
                response5.setVisibility(View.VISIBLE);
                response6.setVisibility(View.VISIBLE);
                response7.setVisibility(View.VISIBLE);
                response8.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
                score5.setVisibility(View.VISIBLE);
                score6.setVisibility(View.VISIBLE);
                score7.setVisibility(View.VISIBLE);
                score8.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score4.setText(scores[3].toString());
                score5.setText(scores[4].toString());
                score6.setText(scores[5].toString());
                score7.setText(scores[6].toString());
                score8.setText(scores[7].toString());
            }
            else if (players[6] != null && responses[6] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                userResponse4.setText(players[3]);
                userResponse5.setText(players[4]);
                userResponse6.setText(players[5]);
                userResponse7.setText(players[6]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                response4.setText(responses[3]);
                response5.setText(responses[4]);
                response6.setText(responses[5]);
                response7.setText(responses[6]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                userResponse4.setVisibility(View.VISIBLE);
                userResponse5.setVisibility(View.VISIBLE);
                userResponse6.setVisibility(View.VISIBLE);
                userResponse7.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                response4.setVisibility(View.VISIBLE);
                response5.setVisibility(View.VISIBLE);
                response6.setVisibility(View.VISIBLE);
                response7.setVisibility(View.VISIBLE);
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
                score5.setVisibility(View.VISIBLE);
                score6.setVisibility(View.VISIBLE);
                score7.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score4.setText(scores[3].toString());
                score5.setText(scores[4].toString());
                score6.setText(scores[5].toString());
                score7.setText(scores[6].toString());
            }
            else if (players[5] != null && responses[5] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                userResponse4.setText(players[3]);
                userResponse5.setText(players[4]);
                userResponse6.setText(players[5]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                response4.setText(responses[3]);
                response5.setText(responses[4]);
                response6.setText(responses[5]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                userResponse4.setVisibility(View.VISIBLE);
                userResponse5.setVisibility(View.VISIBLE);
                userResponse6.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                response4.setVisibility(View.VISIBLE);
                response5.setVisibility(View.VISIBLE);
                response6.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score4.setText(scores[3].toString());
                score5.setText(scores[4].toString());
                score6.setText(scores[5].toString());
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
                score5.setVisibility(View.VISIBLE);
                score6.setVisibility(View.VISIBLE);
            }
            else if (players[4] != null && responses[4] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                userResponse4.setText(players[3]);
                userResponse5.setText(players[4]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                response4.setText(responses[3]);
                response5.setText(responses[4]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                userResponse4.setVisibility(View.VISIBLE);
                userResponse5.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                response4.setVisibility(View.VISIBLE);
                response5.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score4.setText(scores[3].toString());
                score5.setText(scores[4].toString());
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
                score5.setVisibility(View.VISIBLE);
            }
            else if (players[3] != null && responses[3] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                userResponse4.setText(players[3]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                response4.setText(responses[3]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                userResponse4.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                response4.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score4.setText(scores[3].toString());
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
                score4.setVisibility(View.VISIBLE);
            }
            else if (players[2] != null && responses[2] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                userResponse3.setText(players[2]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                response3.setText(responses[2]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                userResponse3.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                response3.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score3.setText(scores[2].toString());
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
                score3.setVisibility(View.VISIBLE);
            }
            else if (players[1] != null && responses[1] != null)
            {
                userResponse1.setText(players[0]);
                userResponse2.setText(players[1]);
                response1.setText(responses[0]);
                response2.setText(responses[1]);
                userResponse1.setVisibility(View.VISIBLE);
                userResponse2.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                response2.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score2.setText(scores[1].toString());
                score1.setVisibility(View.VISIBLE);
                score2.setVisibility(View.VISIBLE);
            }
            else
            {
                userResponse1.setText(players[0]);
                response1.setText(responses[0]);
                userResponse1.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                score1.setText(scores[0].toString());
                score1.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                con = connectionclass();
                if (con == null)
                {
                    z = "Check your internet access and try again!";
                }
                else
                {
                    int counter = 0;
                    String query = "select Response, Nickname, Score from Player where GameID='" + gamePass + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next())
                    {
                        name1 = rs.getString("Response");
                        String name2 = rs.getString("Nickname");
                        String score = rs.getString("Score");
                        z= name1;
                        responses[counter] = z;
                        players[counter] = name2;
                        scores[counter] = score;
                        counter += 1;
                        isSuccess = true;
                    }
                    con.close();
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();

                Log.d("sql error", z);
            }
            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //your database connection string goes below
            ConnectionURL = "jdbc:jtds:sqlserver://guestimation.database.windows.net:1433;DatabaseName=guestimation;user=user@guestimation;password=Cowboys2017;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}

