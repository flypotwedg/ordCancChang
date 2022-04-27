package com.example.ordcancchang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class orderList extends AppCompatActivity {

    ListView orderList;
    //custom adapter?

    String orderUID;
    String userUID;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderList=(ListView)findViewById(R.id.orderList);

        userUID=getIntent().getStringExtra("userUID");

        Query ordQuery=database.child("Orders").orderByChild(userUID);

        ordQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot order:snapshot.getChildren())
                    {
                        if(Integer.parseInt(order.child("completed").getValue().toString())==0)
                        {

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(orderList.this, "An error occurred retrieving your orders. Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void finishAct() {
        Intent ret=new Intent();
        ret.putExtra("orderUID",orderUID);

        finish();
    }
}