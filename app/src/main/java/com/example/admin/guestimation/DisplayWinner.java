package com.example.admin.guestimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DisplayWinner extends AppCompatActivity {

    public Connection con;
    public TextView displayWinner;
    String username, gamePass, isAdmin, winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_winner);

        Intent getMainIntent = getIntent();
        username = getMainIntent.getStringExtra("username");
        gamePass = getMainIntent.getStringExtra("gamePass");
        isAdmin = getMainIntent.getStringExtra("isAdmin");

        displayWinner = findViewById(R.id.displayWinner);
        displayWinner.setVisibility(View.INVISIBLE);

        CheckWinner checkWinner = new CheckWinner();
        checkWinner.execute("");

        if (isAdmin.equals("Y"))
        {
            DeleteGame deletegame = new DeleteGame();
            deletegame.execute("");
        }
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
            displayWinner.setText(winner);
            displayWinner.setVisibility(View.VISIBLE);
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
                    if (rs1.next())
                    {
                        winner = rs1.getString("Nickname");
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
