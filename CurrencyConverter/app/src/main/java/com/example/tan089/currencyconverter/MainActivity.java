package com.example.tan089.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Declare some variables
    private EditText editText01;
    private Button bnt01;
    private TextView textView01;
    private String usd;
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
                //covert user's input to string
                usd = editText01.getText().toString();
                //Convert string to double
                Double dInputs = Double.parseDouble(usd);
                //Convert function
                Double result = dInputs * 112.57;
                //Display the result
                textView01.setText("$" + usd + " = " + String.format("%.2f", result) + " " + "Yen");

            }
        });
    }
}
