package com.example.ordcancchang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.StringTokenizer;

public class orderList extends AppCompatActivity {

    ListView orderList;

    String orderUID;
    String userUID;

    TextView tempTV;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        tempTV=(TextView)findViewById(R.id.disclaimer);

        orderList=(ListView)findViewById(R.id.orderList);

        userUID=getIntent().getStringExtra("userUID");

        Query ordQuery=database.child("Orders").orderByChild(userUID);

        ordQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<orderDetail> listOfOrders=new ArrayList<>();

                    Calendar calendar=Calendar.getInstance();
                    int year=calendar.get(Calendar.YEAR);
                    int month=calendar.get(Calendar.MONTH);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int hour=calendar.get(Calendar.HOUR);
                    int min=calendar.get(Calendar.MINUTE);

                    String date=(month+1) + "/" + day + "/" + year;
                    String time=(hour+12)+":"+min; //current time

                    for(DataSnapshot order:snapshot.getChildren())
                    {
                        if((Integer.parseInt(order.child("completed").getValue().toString())==0))//checks if order is pending
                        {
                            String sansDay[]=order.child("apptTime").getValue().toString().split("\\s[AP]M"); //take away AM or PM
                            String sansMin[]=sansDay[0].split(":"); //split hour and min
                            int newHour=Integer.parseInt(sansMin[0])+12; //add 12 to hour
                            String newTime=String.valueOf(newHour)+":"+sansMin[1]; //recombine

                            if(((date.compareTo(order.child("apptDate").getValue().toString()))>=-1)//check date to see if date before
                                    &&((time.compareTo(newTime)<=0)))//check time to see if within 24 hours
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

                    orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(orderList.this, "An error occurred retrieving your orders. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void changeAct()
    {
        Intent change=new Intent(this, orderChange.class);
        change.putExtra("orderUID",orderUID);
        startActivityForResult(change,1);
    }
    public void cancelAct()
    {
        Intent cancel=new Intent(this, orderCancel.class);
        cancel.putExtra("orderUID",orderUID);
        startActivityForResult(cancel,2);
    }
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
                    Toast.makeText(this, "Your order has been cancelled", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "An error occurred while cancelling your order. Please try again", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(this, "An error occurred while saving your changes. Please try again", Toast.LENGTH_LONG).show();
                break;
        }
        finish();
    }
}