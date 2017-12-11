package com.example.admin.guestimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DisplayWinner extends AppCompatActivity {

    public Connection con;
    public TextView displayWinner, displayWinner1, displayWinner2;
    public Button back;
    public String username, gamePass, isAdmin, winner;
    public ArrayList<String> winners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_winner);

        Intent getMainIntent = getIntent();
        username = getMainIntent.getStringExtra("username");
        gamePass = getMainIntent.getStringExtra("gamePass");
        isAdmin = getMainIntent.getStringExtra("isAdmin");

        back = findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        displayWinner = findViewById(R.id.displayWinner);
        displayWinner.setVisibility(View.INVISIBLE);
        displayWinner1 = findViewById(R.id.displayWinner1);
        displayWinner1.setVisibility(View.INVISIBLE);
        displayWinner2 = findViewById(R.id.displayWinner2);
        displayWinner2.setVisibility(View.INVISIBLE);

        CheckWinner checkWinner = new CheckWinner();
        checkWinner.execute("");

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isAdmin.equals("Y"))
                {
                    DeleteGame deletegame = new DeleteGame();
                    deletegame.execute("");
                }
                Intent backHome = new Intent(getApplicationContext(), Homepage.class);
                finish();
                startActivity(backHome);
            }
        });
    }

    public class CheckWinner extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";
        int score;

        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r)
        {
            if (winners.size() >= 3)
            {
                displayWinner.setText(winners.get(0));
                displayWinner.setVisibility(View.VISIBLE);
                displayWinner1.setText(winners.get(1));
                displayWinner1.setVisibility(View.VISIBLE);
                displayWinner2.setText(winners.get(3));
                displayWinner2.setVisibility(View.VISIBLE);
            }
            if (winners.size() == 2)
            {
                displayWinner.setText(winners.get(0));
                displayWinner.setVisibility(View.VISIBLE);
                displayWinner1.setText(winners.get(1));
                displayWinner1.setVisibility(View.VISIBLE);
            }
            else {
                displayWinner.setText(winners.get(0));
                displayWinner.setVisibility(View.VISIBLE);
            }
            back.setVisibility(View.VISIBLE);
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
                    String query = "select MAX(Score) as Score from Player where GameID='" + gamePass + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        score = rs.getInt("Score");
                        isSuccess = true;
                    }
                    //pulls the users cardToPlay for comparison
                    String query2 = "select Nickname from Player where GameID='" + gamePass + "' and Score=" + score + "";
                    ResultSet rs1 = stmt.executeQuery(query2);
                    while (rs1.next())
                    {
                        winner = rs1.getString("Nickname");
                        winners.add(winner);
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

    public class DeleteGame extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute(){
        }

        @Override
        protected void onPostExecute(String r)
        {
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
                    String query = "delete from Player where GameID = '" + gamePass + "';";
                    String query2 = "delete from Game where GameID = '" + gamePass + "'";
                    Statement stmt = con.createStatement();
                    stmt.executeQuery(query);
                    stmt.executeQuery(query2);
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
