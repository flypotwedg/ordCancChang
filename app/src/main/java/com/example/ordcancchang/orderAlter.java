package com.example.ordcancchang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class orderAlter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_alter);

        //check date and time to see if before 24 hours of appointment
        //if true, allow the change or cancellation
        //otherwise error

        //could also show appointments but have them greyed out in listview

    }
}