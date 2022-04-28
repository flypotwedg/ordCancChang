package com.example.ordcancchang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    String orderUID;
    String userUID;

    TextView tempTV;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        getSupportActionBar().hide();
        //little break from coding.
        //hides that top bar, theres a better way to do this via manifest but thats for later

        tempTV=(TextView)findViewById(R.id.disclaimer);

        orderList=(ListView)findViewById(R.id.orderList);

        userUID=getIntent().getStringExtra("userUID");

        Query ordQuery=database.child("Orders").orderByChild(userUID);

        ordQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<order> listOfOrders=new ArrayList<>();

                    Calendar calendar=Calendar.getInstance();
                    int year=calendar.get(Calendar.YEAR);
                    int month=calendar.get(Calendar.MONTH)+1;
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int AMPM=calendar.get(Calendar.AM_PM);
                    int hour=AMPM==1?calendar.get(Calendar.HOUR)+12:calendar.get(Calendar.HOUR);
                    int min=calendar.get(Calendar.MINUTE);

                    for(DataSnapshot order:snapshot.getChildren())
                    {
                        if(Integer.parseInt(order.child("completed").getValue().toString())==0)
                        {
                            if(userUID.equals(order.child("userUID").getValue().toString()))
                            {
                                String sansDay[]=order.child("apptTime").getValue().toString().split("\\s"); //take away AM or PM
                                String sansMin[]=sansDay[0].split(":"); //split hour and min
                                int hourTemp=Integer.parseInt(sansMin[0]);
                                int newHour=(sansDay[1].equals("PM")&&(hourTemp<13))?(hourTemp+12):hourTemp; //add 12 to hour is PM

                                String dateTemp=order.child("apptDate").getValue().toString();
                                String dateSplit[]=dateTemp.split("/");//0 month, 1 day, 2 year
                                tempTV.setText(sansMin[1]+" | "+min);
                                if(Integer.parseInt(dateSplit[2])==year||year>=Integer.parseInt(dateSplit[2]))//same year or previous year
                                {
                                    if(Integer.parseInt(dateSplit[0])==month||month>=Integer.parseInt(dateSplit[0]))//same month or previous month
                                    {
                                        if (Integer.parseInt(dateSplit[1])==day)//same day
                                        {
                                            continue; //within 24 hours
                                        }
                                        else if(day+1==Integer.parseInt(dateSplit[1]))//tomorrow
                                        {
                                            if(hour>=newHour)//current time is past appointment time
                                            {
                                                if(min>=Integer.parseInt(sansMin[1]))//same minute
                                                {
                                                    continue;//within 24 hours
                                                }
                                            }
                                        }
                                        else if(day>Integer.parseInt(dateSplit[1]))
                                        {
                                            continue; //unfulfilled in the past
                                        }
                                    }
                                }
                                String orderUIDTemp=order.getKey();
                                String vendNameTemp=order.child("vendName").getValue().toString();
                                String apptDateTemp=order.child("apptDate").getValue().toString();
                                String apptTimeTemp=order.child("apptTime").getValue().toString();

                                com.example.ordcancchang.order temp=new order(orderUIDTemp,vendNameTemp,apptDateTemp,apptTimeTemp);
                                listOfOrders.add(temp);
                            }
                            else
                            {
                                continue;//order is not pending
                            }
                        }
                        else
                        {
                            continue;
                        }
                    }

                    orderListAdapter adapter=new orderListAdapter(orderList.this,R.layout.adapter_view_layout,listOfOrders);
                    orderList.setAdapter(adapter);

                    orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //get orderUID from clicked item - done
                            //pass to details - done
                            //details will query and display data and give 2 buttons - done
                            //cancel will give a prompt to confirm and set order to -1 - done
                            //change will open up schedule or its own custom activity to change - in progress
                            //both will finish, then orderlist will finish
                            orderUID=listOfOrders.get(i).getOrderUID();

                            Intent detail=new Intent(orderList.this, orderDetails.class);
                            detail.putExtra("orderUID",orderUID);
                            startActivityForResult(detail,1);
                        }
                    });

                }
                else
                {
                    Toast.makeText(orderList.this, "Error getting your orders. Please try again", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(orderList.this, "An error occurred retrieving your orders. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode, resCode, data);
        switch(reqCode)
        {
            case 1: //orderDetails
                switch(resCode)
                {
                    case RESULT_OK: //changed or cancelled
                        Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_LONG).show();
                        break;
                    case 2: //user presses back
                        Toast.makeText(this, "Changes cancelled", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(this, "Your order has been cancelled", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                        break;
                }
            default:
                Toast.makeText(this, "An error occurred. Please try again", Toast.LENGTH_LONG).show();
                break;
        }
    }
}