package com.example.ordcancchang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class orderAlter extends AppCompatActivity {

    Button button;

    String userUID;
    String orderUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_alter);

        button =(Button)findViewById(R.id.buttonn);

        //userUID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        userUID="MBMmAi2B3eRjhIsXo0Lsa6ChfFM2"; //temp, replace with line above

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAct();
            }
        });

    }
    public void listAct()
    {
        Intent list=new Intent(this, orderList.class);
        list.putExtra("userUID",userUID);
        startActivityForResult(list,3);
    }
    /*
    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode, resCode, data);
        switch(reqCode) {
            case 1: //change order
                if(resCode==RESULT_OK)
                {
                    Toast.makeText(this, "Changes save successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "An error occurred while applying your changes. Please try again", Toast.LENGTH_LONG).show();
                }
                break;
            case 2: //cancel order
                if(resCode==RESULT_OK)
                {
                    Toast.makeText(this, "Your order has ben cancelled", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "An error occurred while cancelling your order. Please try again", Toast.LENGTH_LONG).show();
                }
                break;
            case 3: //listview

                if(resCode==RESULT_OK)
                {
                    orderUID=data.getStringExtra("orderUID");
                    switch (action) {
                        case 1:
                            changeAct();
                            break;
                        case 2:
                            cancelAct();
                            break;
                        default:
                            Toast.makeText(this, "An error occurred initiating process. Please try again", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                else
                {
                    Toast.makeText(this, "An error occurred pulling up your orders. Please try again", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(this, "An error occurred while saving your changes. Please try again", Toast.LENGTH_LONG).show();
                break;
        }
    }*/
}