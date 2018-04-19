package com.example.moody.mybock;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> web;
    private final ArrayList<String> imageId;
    private final ArrayList<String> val;
    public CustomList(Activity context,
                      ArrayList<String> web, ArrayList<String> imageId,ArrayList<String> val) {
        super(context, R.layout.list_row_layout, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.val = val;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_row_layout, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.namme);

        TextView txtDat = (TextView) rowView.findViewById(R.id.datta);
        TextView txtval = (TextView) rowView.findViewById(R.id.VALL);
        txtTitle.setText(web.get(position));

        txtDat.setText(imageId.get(position));
        String s=": ";
        s+=val.get(position);
        txtval.setText(s);
        return rowView;
    }
}