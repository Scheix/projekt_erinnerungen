package com.example.mscheuringer.erinnerungen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael on 18.06.2015.
 */
public class Update extends Activity {

    Erinnerung e;
    boolean erledigt;
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

        TextView t1 = (TextView) findViewById(R.id.updateTitel);
        TextView t2 = (TextView) findViewById(R.id.updateNote);
        DatePicker t3 = (DatePicker) findViewById(R.id.updateDate);

        t1.setText(e.title);
        t2.setText(e.note);

    }

    public void backToMain (final View source)
    {
        TextView t1 = (TextView) findViewById(R.id.updateTitel);
        TextView t2 = (TextView) findViewById(R.id.updateNote);

        erledigt = e.erledigt;

        DatePicker date = (DatePicker) findViewById(R.id.updateDate);

        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year =  date.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String d = null;

        SimpleDateFormat format = new SimpleDateFormat("MM.dd.yyyy");//("MM/dd/yyyy")

        if (calendar != null) {
            d = format.format(calendar.getTime());
        }

        Erinnerung erinnerung = new Erinnerung(t1.getText().toString(),t2.getText().toString(), d, erledigt);

        Intent intent = new Intent();
        intent.putExtra("Erinnerung", e);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
