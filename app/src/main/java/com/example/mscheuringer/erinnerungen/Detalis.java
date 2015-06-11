package com.example.mscheuringer.erinnerungen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Michael on 11.06.2015.
 */
public class Detalis extends Activity {

    Erinnerung e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();

        if (params != null)
        {
            e = (Erinnerung) params.get("Erinnerung");
        }

        TextView t1 = (TextView) findViewById(R.id.detailTitel);
        TextView t2 = (TextView) findViewById(R.id.detailNote);
        TextView t3 = (TextView) findViewById(R.id.detailDate);
        TextView t4 = (TextView) findViewById(R.id.detailErldedigt);

        t1.setText(e.title);
        t2.setText(e.note);
        t3.setText(e.date);
        t4.setText(""+e.erledigt);
    }
}
