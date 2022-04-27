package com.example.ordcancchang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class orderList extends AppCompatActivity {

    ListView orderList;
    //custom adapter?

    String orderUID;
    String userUID;

    TextView tempTV;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    ArrayList<orderDetail> listOfOrders=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        tempTV=(TextView)findViewById(R.id.disclaimer);

        orderList=(ListView)findViewById(R.id.orderList);

        userUID=getIntent().getStringExtra("userUID");

        Query ordQuery=database.child("Orders").orderByChild(userUID);

        ordQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Calendar calendar=Calendar.getInstance();
                    int year=calendar.get(Calendar.YEAR);
                    int month=calendar.get(Calendar.MONTH);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int hour=calendar.get(Calendar.HOUR);
                    int min=calendar.get(Calendar.MINUTE);

                    String date=(month+1) + "/" + day + "/" + year;
                    String time=(hour+12)+":"+min;

                    for(DataSnapshot order:snapshot.getChildren())
                    {
                        if((Integer.parseInt(order.child("completed").getValue().toString())==0))//checks if order is pending
                        {
                            String[] longApptTimeTemp=order.child("apptTime").getValue().toString().split(":.*\\s[a-zA-Z]]M");

                            tempTV.setText(longApptTimeTemp[0]+" | "+longApptTimeTemp[1]+" | "+longApptTimeTemp[2]);

                            if(((date.compareTo(order.child("apptDate").getValue().toString()))>=-1)//check date to see if date before
                                    &&((time.compareTo(longApptTimeTemp[0])>=0)))//check time to see if within 24 hours
                            {
                                continue; //skip putting item on list since its within 24 hours
                            }
                            else
                            {
                                String orderUIDTemp=order.getKey();
                                String vendNameTemp=order.child("vendName").getValue().toString();
                                String apptDateTemp=order.child("apptDate").getValue().toString();
                                String apptTimeTemp=order.child("apptTime").getValue().toString();

                                orderDetail temp=new orderDetail(orderUIDTemp,vendNameTemp,apptDateTemp,apptTimeTemp);
                                listOfOrders.add(temp);
                            }
                        }
                        else
                        {
                            continue; //skip item since it is finished or cancelled
                        }
                    }

                    orderListAdapter adapter=new orderListAdapter(orderList.this,R.layout.adapter_view_layout,listOfOrders);
                    orderList.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(orderList.this, "An error occurred retrieving your orders. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    public void finishAct() {
        Intent ret=new Intent();
        ret.putExtra("orderUID",orderUID);
        setResult(RESULT_OK, ret);
        finish();
    }
}