package com.example.mscheuringer.erinnerungen;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Michael on 28.05.2015.
 */
public class MyAdapter extends ArrayAdapter <Erinnerung> {



    public MyAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context ctx = parent.getContext();
        LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.list_layout, parent, false);

        Erinnerung erinnerung = this.getItem(position);
        CheckBox check = (CheckBox) v.findViewById(R.id.lblTitel);
        TextView text = (TextView) v.findViewById(R.id.lblTime);

        text.setText(erinnerung.date);
        check.setText(erinnerung.title);

        return v;
    }
}
