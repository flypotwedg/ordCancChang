package com.example.ordcancchang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class orderDetails extends AppCompatActivity {

    String orderUID;
    String apptAddr;
    String apptTime;
    String apptDate;
    String vendName;

    TextView orderUIDLabel;
    TextView vendNameLabel;
    TextView addrLabel;
    TextView apptDateLabel;
    TextView apptTimeLabel;
    TextView priceLabel;

    Button chanButt;
    Button cancButt;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    private static final DecimalFormat dfZero=new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderUIDLabel=(TextView) findViewById(R.id.orderUIDLabel);
        vendNameLabel=(TextView) findViewById(R.id.vendNameLabel);
        addrLabel=(TextView)findViewById(R.id.addressLabel);
        apptDateLabel=(TextView) findViewById(R.id.apptDateLabel);
        apptTimeLabel=(TextView) findViewById(R.id.apptTimeLabel);
        priceLabel=(TextView) findViewById(R.id.priceLabel);

        chanButt=(Button) findViewById(R.id.edit);
        cancButt=(Button)findViewById(R.id.cancel);

        orderUID=getIntent().getStringExtra("orderUID");

        Query ordQuery=database.child("Orders").orderByChild(orderUID);

        ordQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    apptAddr=snapshot.child(orderUID).child("apptAddr").getValue().toString();
                    apptDate=snapshot.child(orderUID).child("apptDate").getValue().toString();
                    apptTime=snapshot.child(orderUID).child("apptTime").getValue().toString();
                    vendName=snapshot.child(orderUID).child("vendName").getValue().toString();

                    orderUIDLabel.setText("Order ID:\n"+orderUID);
                    vendNameLabel.setText("Vendor:\n"+vendName);
                    addrLabel.setText("Address:\n"+apptAddr);
                    apptDateLabel.setText("Appointment Date:\n"+apptDate);
                    apptTimeLabel.setText("Appointment Time:\n"+apptTime);

                    float tempPrice=Float.parseFloat(snapshot.child(orderUID).child("price").getValue().toString());
                    String priceStr=dfZero.format(tempPrice);
                    priceLabel.setText("Price:\n$"+priceStr);
                }
                else
                {
                    Toast.makeText(orderDetails.this, "Order details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(orderDetails.this, "Error retrieving order information", Toast.LENGTH_LONG).show();
            }
        });

        cancButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        chanButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAct();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(2);
        super.onBackPressed();
    }
    public void openDialog()
    {
        AlertDialog.Builder diagBuild=new AlertDialog.Builder(this);
        diagBuild.setTitle("Warning");
        diagBuild.setMessage("This cannot be undone. Are you sure you want to cancel your order?");
        diagBuild.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.child("Orders").child(orderUID).child("completed").setValue(-1);
                setResult(3);
                finish();
            }
        });
        diagBuild.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Toast.makeText(orderDetails.this, "Changes discarded", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog diag=diagBuild.create();
        diag.show();
    }
    public void changeAct()
    {
        Intent change=new Intent(this, orderChange.class);
        change.putExtra("orderUID",orderUID);
        change.putExtra("apptAddr",apptAddr);
        change.putExtra("apptDate",apptDate);
        change.putExtra("apptTime",apptTime);
        change.putExtra("vendName",vendName);
        startActivityForResult(change,1);
    }
    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode, resCode, data);
        switch(reqCode) {
            case 1: //change order
                if(resCode==RESULT_OK)
                {
                    setResult(RESULT_OK);
                }
                else
                {
                    setResult(RESULT_CANCELED);
                }
                break;
            default:
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }
}