package com.example.ordcancchang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class orderAlter extends AppCompatActivity {

    Button changeButt;
    Button cancelButt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_alter);

        changeButt=(Button)findViewById(R.id.change);
        cancelButt=(Button)findViewById(R.id.cancel);


        //check date and time to see if before 24 hours of appointment
        //if true, allow the change or cancellation
        //otherwise error

        //could also show appointments but have them greyed out in listview

    }
    public void changeAct()
    {
        Intent change=new Intent(this, orderChange.class);
        startActivityForResult(change,1);
    }
    public void cancelAct()
    {
        Intent cancel=new Intent(this, orderCancel.class);
        startActivityForResult(cancel,2);
    }
    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode, resCode, data);
        switch(reqCode) {
            case 1:
                break;
            case 2:
                break;
            default:
                Toast.makeText(this, "An error occurred while saving your changes. Please try again", Toast.LENGTH_LONG).show();
                break;
        }
    }
}