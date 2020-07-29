package com.example.passwordgenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<String> password;
    ArrayList<String> reason;

    public ItemAdapter(Context c, ArrayList<String> p,  ArrayList<String> r){
        password=p;
        reason=r;
        mInflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return password.size();
    }

    @Override
    public Object getItem(int i) {
        return password.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=mInflater.inflate(R.layout.mylist_view,null);
        TextView passwordTextView=(TextView)v.findViewById(R.id.passwordTextView);
        TextView reasonTextView=(TextView)v.findViewById(R.id.reasonTextView);

        String pwd=password.get(i);
        String rsn=reason.get(i);


        passwordTextView.setText(pwd);
        reasonTextView.setText(rsn);
        return v;
    }
}
