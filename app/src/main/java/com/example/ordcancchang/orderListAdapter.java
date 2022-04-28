package com.example.ordcancchang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class orderListAdapter extends ArrayAdapter<order>{
    Context context;
    int res;

    public orderListAdapter(Context context, int resource, ArrayList<order> list)
    {
        super(context,resource,list);
        this.context=context;
        this.res=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, View convView, ViewGroup parent)
    {
        LayoutInflater inflate=LayoutInflater.from(this.context);
        convView=inflate.inflate(res,parent,false);

        order order= (com.example.ordcancchang.order) getItem(pos);

        TextView vendorName=(TextView) convView.findViewById(R.id.listVendName);
        TextView date=(TextView) convView.findViewById(R.id.listApptDate);
        TextView time=(TextView) convView.findViewById(R.id.listApptTime);

        vendorName.setText(order.getVendName());
        date.setText(order.getApptDate());
        time.setText(order.getApptTime());

        return convView;
    }

}
