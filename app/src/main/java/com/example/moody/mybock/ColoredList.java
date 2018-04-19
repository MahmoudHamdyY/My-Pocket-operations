package com.example.moody.mybock;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class ColoredList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> web;

    public ColoredList( Activity context, ArrayList<String> web) {
        super(context, R.layout.list_colored,web);

        this.context = context;
        this.web = web;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_colored, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.lestCo);
        txtTitle.setText(web.get(position));

        return rowView;
    }
}
