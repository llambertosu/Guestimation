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
import android.widget.ProgressBar;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Declaring layout button, edit texts
    public EditText answer;
    public Button submit;
    public TextView question;
    public ProgressBar progressBar;

    //Declaring connection variables
    public Connection con;
    String un, pass, db, ip, username, gamePass, isAdmin;
    Integer deckID, nextCard;
    ArrayList<Integer> cards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting values to log in for the database
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        gamePass = intent.getStringExtra("gamePass");
        isAdmin = intent.getStringExtra("isAdmin");

        //Get values from the button, ExitText, and TextView
        answer = findViewById(R.id.answerBox);
        submit = findViewById(R.id.submitButton);
        question = findViewById(R.id.questionView);
        progressBar = findViewById(R.id.progressBar);

        //Declare Server ip, username, database name, and password
        ip = "guestimation.database.windows.net:1433";
        db = "guestimation";
        un = "user";
        pass = "Cowboys2017";

        //calls the CheckCard method in order to determine which card should be pulled
        CheckCard checkCard = new CheckCard();
        checkCard.execute("");

        //calls the CheckDeck method
        CheckDeck checkDeck = new CheckDeck();
        checkDeck.execute("");

        //calls the GetCard method
        GetCards getCards = new GetCards();
        getCards.execute("");

        //calls the CheckQuestion method
        CheckQuestion checkQuestion = new CheckQuestion();
        checkQuestion.execute("");

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Calls the submit method after an answer has been entered
                CheckSubmit checkSubmit = new CheckSubmit();
                checkSubmit.execute("");
            }
        });
    }

    //Submits the answer to the question for the specified user
    public class CheckSubmit extends AsyncTask<String,String,String>
    {
        //Intent intent2 = getIntent();
        //String username = intent2.getStringExtra("username");
        //String gamePass = intent2.getStringExtra("gamePass");
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(MainActivity.this, r, Toast.LENGTH_LONG).show();
            if (isSuccess)
            {
                UpdateCard updateCard = new UpdateCard();
                updateCard.execute("");
                //Creates the intent to pass variables to the ScoreActivity class
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("gamePass", gamePass);
                intent.putExtra("isAdmin", isAdmin);
                intent.putExtra("lastCardPlayed",Integer.toString(cards.get(nextCard)));
                //closes the MainActivity so it can be reused later
                finish();
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

    //Gets the question associated with each deck
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
            //Toast.makeText(MainActivity.this, nextCard.toString(), Toast.LENGTH_LONG).show();
            //if isSuccess displays the question in a text view, if it exists or the internet connection is working
            if (isSuccess) {
                question = findViewById(R.id.questionView);
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
                    String query = "select Question from Card where DeckID=" + deckID + " and CardID=" + cards.get(nextCard);
                    nextCard += 1;
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

    //Gets the deck ID associated with the GameID
    public class CheckDeck extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        Intent intent2 = getIntent();
        String gamePass = intent2.getStringExtra("gamePass");

        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(String r)
        {
            //Toast.makeText(MainActivity.this, r, Toast.LENGTH_LONG).show();
            if (isSuccess = true)
            {
                deckID = Integer.parseInt(z);
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
                    //pulls the DeckID for use with questions
                    String query = "select DeckID from Game where GameID = '" + gamePass + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        name1 = rs.getString("DeckID");
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

    //Get the ID of all the cards associated with the particular deck
    public class GetCards extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute (String r)
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
                    String query = "select CardID from Card where DeckID =" + deckID;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next())
                    {
                        name1 = rs.getString("CardID");
                        z = name1;
                        cards.add(Integer.parseInt(z));
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

    //get the array index
    public class CheckCard extends AsyncTask<String,String,String>
    {
        //Intent intent2 = getIntent();
        //String username = intent2.getStringExtra("username");
        //String gamePass = intent2.getStringExtra("gamePass");
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r) {
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
                    String query = "select cardToPlay from Player where nickname='" + username + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        nextCard = rs.getInt("cardToPlay");
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

    //updates the cardToPlay table in the database for each user
    public class UpdateCard extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(String r) {
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
                    String query = "update Player set cardToPlay = " + nextCard + " where Nickname='" + username + "' and GameID='" + gamePass + "';";
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
