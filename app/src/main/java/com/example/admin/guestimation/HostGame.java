package com.example.admin.guestimation;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by admin on 11/27/17.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class HostGame extends AppCompatActivity {

    public EditText nickname;
    public EditText gamePass;
    public Button loginButton;
    private TextView _screen;
    private String display ="";

    public Connection con;
    String un, pass, db, ip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        //Get values from the button, ExitText, and TextView
        nickname = findViewById(R.id.enterNickname);
        gamePass = findViewById(R.id.enterPassword);
        loginButton = findViewById(R.id.loginButton);

        //Declare Server ip, username, database name, and password
        ip = "guestimation.database.windows.net:1433";
        db = "guestimation";
        un = "user";
        pass = "Cowboys2017";

        _screen = findViewById(R.id.viewGameKey);
        _screen.setText(display);



    }

    private void updateScreen () {
        _screen.setText(display);
    }

    private void clearScreen() {
        display = "";
    }


    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 ="";

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess)
            {
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                intent2.putExtra("username", nickname.getText().toString());
                intent2.putExtra("gamePass", gamePass.getText().toString());
                startActivity(intent2);
            }
        }

        @Override
        protected String doInBackground(String... params)
        {
            String userNickname = nickname.getText().toString();
            String gamePassword = gamePass.getText().toString();
            try
            {
                con = connectionclass();
                if (con == null)
                {
                    z = "Check your internet access and try again!";
                }
                else
                {
                    String query = "insert into Player (Nickname, GameID, Score, cardToPlay) values ('" + userNickname + "', '" + gamePassword + "', 0, 0)";
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

    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public void onGameKeyPressed (View v) {
        clearScreen();
        Random random = new Random();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String gameKey = generateString(random, chars, 4);
        //RandomString gameKey = new RandomString(1);
        display += gameKey;
        updateScreen();
    }

    public void onClickBack (View v) {
        Intent intent = new Intent(getApplicationContext(), Homepage.class);
        startActivity(intent);
    }
}

