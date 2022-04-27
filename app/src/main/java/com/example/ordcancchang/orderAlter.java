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

}