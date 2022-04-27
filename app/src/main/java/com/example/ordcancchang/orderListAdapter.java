package com.example.ordcancchang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class orderListAdapter extends ArrayAdapter{
    Context context;
    int res;

    public orderListAdapter(Context context, int resource, ArrayList<orderDetail> list)
    {
        super(context,resource,list);
        this.context=context;
        this.res=resource;
    }

    @NonNull
    public View view(int pos, View convView, ViewGroup parent)
    {
        /*
        String orderUID=getItem(pos).getOrderUID();
        String vendName=getItem(pos).getVendName();
        String apptDate=getItem(pos).getApptDate();
        String apptTime=getItem(pos).getApptTime();

        orderDetail order=new orderDetail(orderUID,vendName,apptDate,apptTime);

        LayoutInflater inflate=LayoutInflater.from(this.context);
        convView=inflate.inflate(res,parent,false);

        TextView vendorName=(TextView) convView.findViewById(R.id.listVendName);
        TextView date=(TextView) convView.findViewById(R.id.listApptDate);
        TextView time=(TextView) convView.findViewById(R.id.listApptTime);

        vendorName.setText(vendName);
        date.setText(apptDate);
        time.setText(apptTime);

         */
        orderDetail order= (orderDetail) getItem(pos);

        TextView vendorName=(TextView) convView.findViewById(R.id.listVendName);
        TextView date=(TextView) convView.findViewById(R.id.listApptDate);
        TextView time=(TextView) convView.findViewById(R.id.listApptTime);

        vendorName.setText(order.getVendName());
        date.setText(order.getApptDate());
        time.setText(order.getApptTime());

        return convView;
    }

}
