package com.example.redmo.currencyconverter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //Declare some variables
    private EditText editText01;
    private Button bnt01;
    private TextView textView01;
    private String usd;

    //URL used to extract real-time currency exchange rates, using JSON, for the Petrol-AirCraftCarrier-Federal-Reserve-Note
    private static final String url = "https://api.fixer.io/latest?base=USD";

    //Variable to store the buffered JSON string from website
    String json = "";

    //This string will be assigned each line of the json string in a loop using the BufferedReader Class and readLine() method
    String line = "";

    //Variable that will obtain the value we need for currency conversion
    String rate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //cast the variables to their ids
        editText01 = findViewById(R.id.EditText01);
        bnt01 = findViewById(R.id.bnt);
        textView01 = findViewById(R.id.Yen);

        //Click event
        bnt01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View convertToYen) {
                //All 'System.out' messages are a great way for Android Studio novices to out put messages BEFORE and AFTER specific Methods and/or Operations
                //You can also output the value of key variables or methods in System.out messages, helping you to isolate the cause for errors
                //This greatly assists in debugging and troubleshooting errors in your code, until you become more familiar with Android Studio's Debugging Features
                System.out.println("\nTESTING 1 ... Before AsynchExecution\n");

                /**THIS IS WHERE WE INITIALIZE AN OBJECT OF THE BackgroundTask Class We have created**/
                BackgroundTask object = new BackgroundTask();
                /**We simply invoke the execute() method from the AsynchTask extension in our BackgroundTask class, this execute() method calls the Asynch Task when onClick() is activated**/
                object.execute();

                System.out.println("\nTESTING 2 ... After AsynchExecution\n");

                /**NOTE: The Entirety of the onClick Method's Definition from Lab2 Will Be CUT and PASTED into the 'BackgroundTask' Class**/
                //The reason for this, is because the Android Environment will only let us work with and modify JSON String data ONLY within the Asynchronous Task
                //Thus, we must take all the logic code for converting the currencies into the BackgroundTask Class

            }
        });
    }

    private class BackgroundTask extends AsyncTask<Void, Void, String> {
        //the method we use specifying what happens before the Asynchronous Task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //the method we use specifying what happens during the progress of the Asynchronous Task executing
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        //this is the method for the immediate aftermath of the Asynchronous Task being executed
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }

        //this method, within the definition of the Class BackgroundTask will contain the vast Bulk of our code for the Asynchronous Task
        @Override
        protected String doInBackground(Void... params) {

            try {
                //create an object from the URL Class and initialize it to the 'url' string in this Java Class, MainActivity
                URL web_url = new URL(MainActivity.this.url);

                //create an object from the HttpURLConnection class named httpURLConnection and initialize it with (HttpURLConnection)web_url.openConnection()
                //where the method openConnection() is a method defined in the URL class
                HttpURLConnection httpURLConnection = (HttpURLConnection)web_url.openConnection();

                //Request method set as 'GET'
                httpURLConnection.setRequestMethod("GET");

                System.out.println("\nTESTING ... BEFORE connection method to URL\n");

                //invoke the connect() method from the object httpURLConnection
                httpURLConnection.connect();

                //create an object from the class InputStream and initialize object with
                InputStream inputStream = httpURLConnection.getInputStream();

                //create object named bufferedReader from the BufferedReader class and initialize the object with new BufferedReader(new InputStreamReader(inputStream))
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                System.out.println("CONNECTION SUCCESSFUL\n");

                //extract the string from the JSON, line by line, store it in the 'json' string variable, using a while loop, checking until the end of the entire JSON String
                while (line != null){
                    //We will assign the bufferedReader.readLine() to the string line every iteration
                    line = bufferedReader.readLine();
                    //then we will append line to json
                    json += line;
                }
                System.out.println("\nTHE JSON: " + json);

                /**create JSON Object from JSONObject Class, using the json string**/
                JSONObject obj = new JSONObject(json);

                //create second JSON Object that will contain a nested JSON Object within the FIRST JSON Object created
                JSONObject objRate = obj.getJSONObject("rates");

                //use the second JSON Object created and use the get(String ...) method
                //We will put "JPY" as the argument in the parameter of the get(String ...) method in order to get the exchange rate for Yen
                //Lastly, from the get(String ...) method, we will invoke the toString() method, because we must pull the JSON Object as a string firstly

                //the string rate will store the Yen to USD conversion ratio
                rate = objRate.get("JPY").toString();
                System.out.println("\nWhat is rate: " + rate + "\n");

                /**CRUCIAL: We MUST convert the String 'rate' to the type double 'value' HERE, within the Asynchronous Task**/
                Double value = Double.parseDouble(rate);

                System.out.println("\nTesting JSON String Exchange Rate INSIDE AsynchTask: " + value);

                //convert user's input to string
                usd = editText01.getText().toString();
                //if-else statement to make sure user cannot leave the EditText blank
                if (usd.equals("")){
                    textView01.setText("This field cannot be blank!");
                } else {
                    //Convert string to double
                    Double dInputs = Double.parseDouble(usd);
                    /**We finally get to use our up to date currency conversion value for Yen in our operation**/
                    Double result = dInputs * value;
                    //Display the result
                    textView01.setText("$" + usd + " = " + "¥"+String.format("%.2f", result));
                    //clear the edit text after clicking
                    editText01.setText("");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("MYAPP", "unexpected JSON exception", e);
                System.exit(1);
            }
            return null;
        }

    }
}
