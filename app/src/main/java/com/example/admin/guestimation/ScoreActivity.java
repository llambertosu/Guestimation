package com.example.admin.guestimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    private final int DISPLAY_LENGTH = 5000;

    public Connection con;

    public TextView userResponse1, userResponse2, userResponse3, userResponse4, userResponse5, userResponse6, userResponse7, userResponse8, userResponse9,userResponse10;
    public TextView response1, response2, response3, response4, response5, response6, response7, response8, response9, response10;
    public TextView score1, score2, score3, score4, score5, score6, score7, score8, score9, score10;
    public TextView correctAnswer;

    String username, gamePass, isAdmin;
    //sets arrays for displaying responses, usernames, and scores
    ArrayList<Integer> onCard = new ArrayList<>();
    ArrayList<String> responses = new ArrayList<>();
    ArrayList<String> players = new ArrayList<>();
    ArrayList<String> scores = new ArrayList<>();
    Integer userCard, answer, card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //gets variables passed through from MainActivity intent
        Intent getMainIntent = getIntent();
        username = getMainIntent.getStringExtra("username");
        gamePass = getMainIntent.getStringExtra("gamePass");
        isAdmin = getMainIntent.getStringExtra("isAdmin");
        card = Integer.parseInt(getMainIntent.getStringExtra("lastCardPlayed")) - 1;

        correctAnswer = findViewById(R.id.correctAnswer);
        correctAnswer.setVisibility(View.INVISIBLE);
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        score5 = findViewById(R.id.score5);
        score6 = findViewById(R.id.score6);
        score7 = findViewById(R.id.score7);
        score8 = findViewById(R.id.score8);
        score9 = findViewById(R.id.score9);
        score10 = findViewById(R.id.score10);
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
        userResponse1 = findViewById(R.id.userResponse1);
        userResponse2 = findViewById(R.id.userResponse2);
        userResponse3 = findViewById(R.id.userResponse3);
        userResponse4 = findViewById(R.id.userResponse4);
        userResponse5 = findViewById(R.id.userResponse5);
        userResponse6 = findViewById(R.id.userResponse6);
        userResponse7 = findViewById(R.id.userResponse7);
        userResponse8 = findViewById(R.id.userResponse8);
        userResponse9 = findViewById(R.id.userResponse9);
        userResponse10 = findViewById(R.id.userResponse10);
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
        response1 = findViewById(R.id.response1);
        response2 = findViewById(R.id.response2);
        response3 = findViewById(R.id.response3);
        response4 = findViewById(R.id.response4);
        response5 = findViewById(R.id.response5);
        response6 = findViewById(R.id.response6);
        response7 = findViewById(R.id.response7);
        response8 = findViewById(R.id.response8);
        response9 = findViewById(R.id.response9);
        response10 = findViewById(R.id.response10);
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

        //calls the checkquestion method in order to pull all responses from the database and the card each user is on
        CheckQuestion checkQuestion = new CheckQuestion();
        checkQuestion.execute("");

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run ()
            {
                Intent backtoMain = new Intent(getApplicationContext(), MainActivity.class);
                backtoMain.putExtra("username", username);
                backtoMain.putExtra("gamePass", gamePass);
                backtoMain.putExtra("isAdmin", isAdmin);
                finish();
                startActivity(backtoMain);
            }
        }, DISPLAY_LENGTH);*/

    }

    public class CheckQuestion extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {
            onCard.clear();
        }

        @Override
        protected void onPostExecute(String r)
        {
            //calls the CheckAnswer method
            CheckAnswers checkAnswers = new CheckAnswers();
            checkAnswers.execute("");
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
                    //pulls the cardToPlay for each player, to assure everyone has answered the correct question before everyone can see it
                    String query = "select cardToPlay from Player where GameID='" + gamePass + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next())
                    {
                        Integer cardPlay = rs.getInt("cardToPlay");
                        onCard.add(cardPlay);
                        isSuccess = true;
                    }
                    //pulls the users cardToPlay for comparison
                    String query2 = "select cardToPlay from Player where GameID='" + gamePass + "' and Nickname='" + username + "'";
                    ResultSet rs1 = stmt.executeQuery(query2);
                    if (rs1.next())
                    {
                        userCard = rs1.getInt("cardToPlay");
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

    public class ComputeScore extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";
        Integer winner = 9999;

        protected void onPreExecute(){
            for(String i: responses) {
                if(answer - Integer.parseInt(i) < winner && answer - Integer.parseInt(i) >= 0)
                {
                    winner = Integer.parseInt(i);
                }
            }
        }

        @Override
        protected void onPostExecute(String r)
        {
            //Toast.makeText(ScoreActivity.this, winner.toString(), Toast.LENGTH_LONG).show();
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
                    //updates the score for every player that has the closest or correct answer
                    String query = "update Player set Score = Score + 100 where response = " + winner + " and GameID = '" + gamePass + "';";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    isSuccess = true;
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

    public class GetCorrect extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(String r)
        {
            if (isSuccess = true)
            {
                //sets the correctAnswer textview
                correctAnswer.setText(Integer.toString(answer));
                correctAnswer.setVisibility(View.VISIBLE);
            }
            //calls the method to determine the closest answer, and updates the score by 100 points
            ComputeScore computeScore = new ComputeScore();
            computeScore.execute("");
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
                    isSuccess = false;
                }
                else
                {
                    //pulls the answer from the database
                    String query = "select Answer from Card where CardID= " + card;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        name1 = rs.getString("Answer");
                        answer = Integer.parseInt(name1);
                        isSuccess = true;
                        con.close();
                    }
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

            int counter = 0;
            while (onCard.get(counter) != userCard && counter < onCard.size()) {
                CheckQuestion checkQuestion = new CheckQuestion();
                checkQuestion.execute("");
                counter += 1;
            }
        }

        @Override
        protected void onPostExecute(String r)
        {
            //determines how many entries are in the arrayList and sets the views visible with usernames for that many
            if (players.size() == 10 && responses.size() == 10)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                userResponse4.setText(players.get(3));
                userResponse5.setText(players.get(4));
                userResponse6.setText(players.get(5));
                userResponse7.setText(players.get(6));
                userResponse8.setText(players.get(7));
                userResponse9.setText(players.get(8));
                userResponse10.setText(players.get(9));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
                response4.setText(responses.get(3));
                response5.setText(responses.get(4));
                response6.setText(responses.get(5));
                response7.setText(responses.get(6));
                response8.setText(responses.get(7));
                response9.setText(responses.get(8));
                response10.setText(responses.get(9));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
                score4.setText(scores.get(3));
                score5.setText(scores.get(4));
                score6.setText(scores.get(5));
                score7.setText(scores.get(6));
                score8.setText(scores.get(7));
                score9.setText(scores.get(8));
                score10.setText(scores.get(9));
            }
            else if (players.size() == 9 && responses.size() == 9)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                userResponse4.setText(players.get(3));
                userResponse5.setText(players.get(4));
                userResponse6.setText(players.get(5));
                userResponse7.setText(players.get(6));
                userResponse8.setText(players.get(7));
                userResponse9.setText(players.get(8));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
                response4.setText(responses.get(3));
                response5.setText(responses.get(4));
                response6.setText(responses.get(5));
                response7.setText(responses.get(6));
                response8.setText(responses.get(7));
                response9.setText(responses.get(8));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
                score4.setText(scores.get(3));
                score5.setText(scores.get(4));
                score6.setText(scores.get(5));
                score7.setText(scores.get(6));
                score8.setText(scores.get(7));
                score9.setText(scores.get(8));
            }
            else if (players.size() == 8 && responses.size() == 8)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                userResponse4.setText(players.get(3));
                userResponse5.setText(players.get(4));
                userResponse6.setText(players.get(5));
                userResponse7.setText(players.get(6));
                userResponse8.setText(players.get(7));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
                response4.setText(responses.get(3));
                response5.setText(responses.get(4));
                response6.setText(responses.get(5));
                response7.setText(responses.get(6));
                response8.setText(responses.get(7));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
                score4.setText(scores.get(3));
                score5.setText(scores.get(4));
                score6.setText(scores.get(5));
                score7.setText(scores.get(6));
                score8.setText(scores.get(7));
            }
            else if (players.size() == 7 && responses.size() == 7)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                userResponse4.setText(players.get(3));
                userResponse5.setText(players.get(4));
                userResponse6.setText(players.get(5));
                userResponse7.setText(players.get(6));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
                response4.setText(responses.get(3));
                response5.setText(responses.get(4));
                response6.setText(responses.get(5));
                response7.setText(responses.get(6));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
                score4.setText(scores.get(3));
                score5.setText(scores.get(4));
                score6.setText(scores.get(5));
                score7.setText(scores.get(6));
            }
            else if (players.size() == 6 && responses.size() == 6)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                userResponse4.setText(players.get(3));
                userResponse5.setText(players.get(4));
                userResponse6.setText(players.get(5));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
                response4.setText(responses.get(3));
                response5.setText(responses.get(4));
                response6.setText(responses.get(5));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
                score4.setText(scores.get(3));
                score5.setText(scores.get(4));
                score6.setText(scores.get(5));
            }
            else if (players.size() == 5 && responses.size() == 5)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                userResponse4.setText(players.get(3));
                userResponse5.setText(players.get(4));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
                response4.setText(responses.get(3));
                response5.setText(responses.get(4));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
                score4.setText(scores.get(3));
                score5.setText(scores.get(4));
            }
            else if (players.size() == 4 && responses.size() == 4)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                userResponse4.setText(players.get(3));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
                response4.setText(responses.get(3));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
                score4.setText(scores.get(3));
            }
            else if (players.size() == 3 && responses.size() == 3)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                userResponse3.setText(players.get(2));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
                response3.setText(responses.get(2));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
                score3.setText(scores.get(2));
            }
            else if (players.size() == 2 && responses.size() == 2)
            {
                userResponse1.setText(players.get(0));
                userResponse2.setText(players.get(1));
                response1.setText(responses.get(0));
                response2.setText(responses.get(1));
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
                score1.setText(scores.get(0));
                score2.setText(scores.get(1));
            }
            else
            {
                userResponse1.setText(players.get(0));
                response1.setText(responses.get(0));
                userResponse1.setVisibility(View.VISIBLE);
                response1.setVisibility(View.VISIBLE);
                score1.setText(scores.get(0));
                score1.setVisibility(View.VISIBLE);
            }
            //calls the method to pull the correct answer and display it in a textView on the screen
            GetCorrect getCorrect = new GetCorrect();
            getCorrect.execute("");
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
                    //pulls the response, nickname, and score for each player in the database associated with the gameID, in order to display them to the screen
                    String query = "select Response, Nickname, Score from Player where GameID='" + gamePass + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next())
                    {
                        name1 = rs.getString("Response");
                        String name2 = rs.getString("Nickname");
                        String score = rs.getString("Score");
                        z= name1;
                        responses.add(z);
                        players.add(name2);
                        scores.add(score);
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