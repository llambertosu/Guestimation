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
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserLogin extends AppCompatActivity {

    public EditText nickname;
    public EditText gamePass;
    public Button loginButton;

    public Connection con;
    String un, pass, db, ip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //Get values from the button, ExitText, and TextView
        nickname = (EditText) findViewById(R.id.enterNickname);
        gamePass = (EditText) findViewById(R.id.enterPassword);
        loginButton = (Button) findViewById(R.id.loginButton);

        //Declare Server ip, username, database name, and password
        ip = "guestimation.database.windows.net:1433";
        db = "guestimation";
        un = "user";
        pass = "Cowboys2017";

        /*Are we supposed to be INSERT'ing or pulling from
        the db and comparing values? line 94.
         */
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //calls the CheckLogin class
                UserLogin.CheckLogin checkLogin = new UserLogin.CheckLogin();
                checkLogin.execute("");
            }
        });
    }

    //creates the player in the database with entered gamePass
    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 ="";

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess)
            {
                //starts the MainActivity intent and passes in speciifed variables
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
}
