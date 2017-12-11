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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HostGame extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public EditText gamePassword, nickname;
    public Button loginButton;
    public Spinner cardSpinner;

    public Connection con;
    String un, pass, db, ip, gameKey;
    Integer deckID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        //Get values from the button, ExitText, and TextView
        nickname = findViewById(R.id.enterNickname);
        loginButton = findViewById(R.id.loginButton);
        gamePassword = findViewById(R.id.gamePassword);
        cardSpinner = findViewById(R.id.cardSpinner);

        //Declare Server ip, username, database name, and password
        ip = "guestimation.database.windows.net:1433";
        db = "guestimation";
        un = "user";
        pass = "Cowboys2017";

        generateRandom();

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreateGame createGame = new CreateGame();
                createGame.execute("");

                //calls the CheckLogin class
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");
            }
        });

        // Spinner element
        Spinner spinner = findViewById(R.id.cardSpinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("History");
        categories.add("Architecture");
        categories.add("OSU");
        categories.add("Business");
        categories.add("Media");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }



    public class CreateGame extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 ="";

        @Override
        protected void onPostExecute(String r) {}

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
                    String query = "insert into Game values ('" + gameKey + "', " + deckID + ")";
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
                intent2.putExtra("gamePass", gameKey);
                intent2.putExtra("isAdmin", "Y");
                startActivity(intent2);
            }
        }

        @Override
        protected String doInBackground(String... params)
        {
            String userNickname = nickname.getText().toString();
            try
            {
                con = connectionclass();
                if (con == null)
                {
                    z = "Check your internet access and try again!";
                }
                else
                {
                    String query = "insert into Player (Nickname, GameID, Score, cardToPlay) values ('" + userNickname + "', '" + gameKey + "', 0, 0)";
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

    public void generateRandom()
    {
        Random random = new Random();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        gameKey = generateString(random, chars, 4);
        gamePassword.setText(gameKey);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String item = adapterView.getItemAtPosition(position).toString();
        if (item.equals("History"))
        {
            deckID = 1;
        }else if (item.equals("Architecture"))
        {
            deckID = 2;
        }else if (item.equals("OSU"))
        {
            deckID = 4;
        }else if (item.equals("Business"))
        {
            deckID = 5;
        }else if (item.equals("Media"))
        {
            deckID = 6;
        }

        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

/*    @SuppressLint("ResourceType")
    private void loadSpinnerData() throws SQLException {
        List<String> decks = getAllDecks();

        @SuppressLint
                ("ResourceType") ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.id.cardSpinner, decks);
        dataAdapter.setDropDownViewResource(R.id.cardSpinner);

        cardSpinner.setAdapter(dataAdapter);
    }

    public List<String> getAllDecks() throws SQLException {
        List<String> decks = new ArrayList<String>();

        String query = "SELECT DeckName from Deck";

        Boolean isSuccess = false;
        int counter = 0;

        con = connectionclass();
        Statement stmt = con.createStatement();
        stmt.executeQuery(query);
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next())
        {
            name1 = rs.getString("DeckName");
            decks.set(counter, name1);
            cards[counter] = Integer.parseInt(z);
            counter += 1;
            isSuccess = true;
        }

        return decks;
    }*/

    /*public void onGameKeyPressed (View v) {
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
    }*/
}

