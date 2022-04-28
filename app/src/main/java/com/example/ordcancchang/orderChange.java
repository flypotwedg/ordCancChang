package com.example.ordcancchang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class orderChange extends AppCompatActivity {

    String orderUID;
    String vendName;
    String apptAddr;

    EditText streetInput;
    EditText cityInput;
    EditText stateInput;
    EditText zipcodeInput;

    Button dateBtn;
    TextView date;
    DatePickerDialog datePickerDiag;

    Button timeBtn;
    TextView time;
    TimePickerDialog timePickerDiag;
    int vendOpen = 8; //set to check from firebase or smth
    int vendClose = 20; //same as above

    TextView vendNameLabel;

    private Button confirmBtn;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR); //sets year to current
    int month = calendar.get(Calendar.MONTH); //sets month to current
    int day = calendar.get(Calendar.DAY_OF_MONTH); //sets day to current

    String apptTime;
    String apptDate;

    String street;
    String city;
    String state;
    String zipcode;

    String address;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_change);

        getSupportActionBar().setTitle("Edit Appointment"); //renames action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enables back arrow in top bar

        streetInput = (EditText) findViewById(R.id.street);
        cityInput = (EditText) findViewById(R.id.city);
        stateInput = (EditText) findViewById(R.id.state);
        zipcodeInput = (EditText) findViewById(R.id.zipcode);

        orderUID = getIntent().getStringExtra("orderUID");
        apptAddr=getIntent().getStringExtra("apptAddr");
        apptDate=getIntent().getStringExtra("apptDate");
        apptTime=getIntent().getStringExtra("apptTime");
        vendName =getIntent().getStringExtra("vendName");

        String addrSplit[]=apptAddr.split(", ");

        //tokenize address
        streetInput.setText(addrSplit[0]);
        cityInput.setText(addrSplit[1]);
        stateInput.setText(addrSplit[2]);
        zipcodeInput.setText(addrSplit[3]);

        streetInput.addTextChangedListener(addressTextWatch);
        cityInput.addTextChangedListener(addressTextWatch);
        stateInput.addTextChangedListener(addressTextWatch);
        zipcodeInput.addTextChangedListener(addressTextWatch);

        vendNameLabel = (TextView) findViewById(R.id.vendNameText);

        vendNameLabel.setText(vendName);

        dateBtn = (Button) findViewById(R.id.dateButton);
        date = (TextView) findViewById(R.id.dateDisp);

        date.setText(apptDate); //changes text to apptDate

        dateBtn.setOnClickListener(new View.OnClickListener() { //select date button
            @Override
            public void onClick(View v) {
                datePick();
                dateBtn.setText("Change Date");
            }
        });

        timeBtn = (Button) findViewById(R.id.timeButton);
        time = (TextView) findViewById(R.id.timeDisp);

        timeBtn.setOnClickListener(new View.OnClickListener() { //select time button
            @Override
            public void onClick(View v) {
                timePick();
                timeBtn.setText("Change Time");
            }
        });

        confirmBtn = (Button) findViewById(R.id.confirmButton);
        confirmBtn.setOnClickListener(new View.OnClickListener() { //confirm button
            @Override
            public void onClick(View v) {
                if(!(street.isEmpty()&&city.isEmpty()&&state.isEmpty()&&zipcode.isEmpty()))
                {
                    uplUpd();
                }
                else
                {
                    Toast.makeText(orderChange.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void uplUpd()
    {
        database.child("Orders").child(orderUID).child("apptAddr").setValue(address);
        database.child("Orders").child(orderUID).child("apptDate").setValue(date.getText().toString());
        database.child("Orders").child(orderUID).child("apptTime").setValue(time.getText().toString());

        Toast.makeText(this, "Order updated", Toast.LENGTH_LONG).show();
        finish();
    }
    private TextWatcher addressTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            street = streetInput.getText().toString().trim();
            city = cityInput.getText().toString().trim();
            state = stateInput.getText().toString().trim();
            zipcode = zipcodeInput.getText().toString().trim();

            address = street + ", " + city + ", " + state + ", " + zipcode;
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    public void datePick() //date picker dialog
    {
        datePickerDiag = new DatePickerDialog(orderChange.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        apptDate = (month + 1) + "/" + day + "/" + year;

                        date.setText(apptDate); //changes date textView to what user selected
                    }
                }, year, month, day);
        datePickerDiag.show();
        datePickerDiag.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000); //sets minimum date in dialog to tomorrow, no time travelling
    }
    public void timePick() {
        timePickerDiag = new TimePickerDialog(orderChange.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        time.setText(apptTime);
                        if (hour < vendOpen || hour > vendClose) //checks if time selected is within business hours
                        {
                            Toast.makeText(orderChange.this, "Vendor will be closed, select a different time", Toast.LENGTH_LONG).show();
                        } else {
                            final String dayOrNight = hour < 12 ? "AM" : "PM"; //checks whether appointment is am or pm
                            final String longHour = hour < 13 ? String.valueOf(hour) : String.valueOf(hour - 12); //formats hour to 12 hour format
                            final String longMin = min < 10 ? "0" + min : String.valueOf(min); //adds an extra 0 to the front if only single digit
                            apptTime = longHour + ":" + longMin + " " + dayOrNight;

                            time.setText(apptTime); //does not change textview until valid hours are selected
                        }
                    }
                }, 0, 0, false);

        timePickerDiag.show();
    }
}