package com.example.admin.guestimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    //Declaring layout button, edit texts
    public EditText answer;
    public Button submit;
    public TextView question;
    public ProgressBar progressBar;
    public Button getQuestion;

    //Declaring connection variables
    public Connection con;
    String un, pass, db, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent2 = getIntent();
        String username = intent2.getStringExtra("username");
        String gamePass = intent2.getStringExtra("gamePass");

        //Get values from the button, ExitText, and TextView
        answer = (EditText) findViewById(R.id.answerBox);
        submit = (Button) findViewById(R.id.submitButton);
        question = (TextView) findViewById(R.id.questionView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getQuestion = (Button) findViewById(R.id.getQuestion);

        //Declare Server ip, username, database name, and password
        ip = "guestimation.database.windows.net:1433";
        db = "guestimation";
        un = "user";
        pass = "Cowboys2017";

        getQuestion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckQuestion checkSubmit = new CheckQuestion();
                checkSubmit.execute("");
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckSubmit checkSubmit = new CheckSubmit();
                checkSubmit.execute("");
            }
        });
    }

    public class CheckSubmit extends AsyncTask<String,String,String>
    {
        Intent intent2 = getIntent();
        String username = intent2.getStringExtra("username");
        String gamePass = intent2.getStringExtra("gamePass");
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_LONG).show();
            if (isSuccess)
            {
                Intent intent = new Intent(getApplicationContext(), Scoreboard.class);
                startActivity(intent);
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
                    int submitAns;
                    submitAns = Integer.parseInt(answer.getText().toString());
                    String query = "update Player set Response = " + submitAns + " where Nickname='" + username + "' and GameID='" + gamePass + "';";
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

    public class CheckQuestion extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {
            question.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            question.setVisibility(View.VISIBLE);
            //Toast is the black oval that shows the result
            //Toast.makeText(MainActivity.this, r, Toast.LENGTH_LONG).show();
            //if isSuccess displays the question in a text view, if it exists or the internet connection is working
            if (isSuccess) {
                question = (TextView) findViewById(R.id.questionView);
                answer.setText("");
                question.setText(name1);
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
                    String query = "select Question from Card where DeckID=1 and CardID=15"; //need to change the DeckID and CardID to be Session Variables
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        name1 = rs.getString("Question");
                        z = name1;
                        isSuccess = true;
                        con.close();
                    }
                    else
                        {
                        z = "Error retrieving question";
                        isSuccess = false;
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
