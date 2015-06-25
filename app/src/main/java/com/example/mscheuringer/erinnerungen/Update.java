package com.example.mscheuringer.erinnerungen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael on 18.06.2015.
 */
public class Update extends Activity {

    Erinnerung e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();

        if (params != null)
        {
            e = (Erinnerung) params.get("Erinnerung");
            Log.d("INFO","Update-Activity, Object(Erinnerung)-Recieved");
            Log.d("INFO","Update-Activity, "+e.title);
        }

        EditText t1 = (EditText) findViewById(R.id.updateTitel);
        EditText t2 = (EditText) findViewById(R.id.updateNote);
        DatePicker t3 = (DatePicker) findViewById(R.id.updateDate);

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");

        try {
            format.parse(e.date);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        Calendar calc = format.getCalendar();
        int year = calc.get(Calendar.YEAR);
        int month = calc.get(Calendar.MONTH);
        int day = calc.get(Calendar.DAY_OF_MONTH);

        t1.setText(e.title);
        t2.setText(e.note);
        t3.updateDate(year, month, day);
    }

    public void backToMain (final View source)
    {
        EditText t1 = (EditText) findViewById(R.id.updateTitel);
        EditText t2 = (EditText) findViewById(R.id.updateNote);

        DatePicker date = (DatePicker) findViewById(R.id.updateDate);

        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year =  date.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String d = null;

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");//("MM/dd/yyyy")

        if (calendar != null) {
            d = format.format(calendar.getTime());
        }

        Erinnerung erinnerung = new Erinnerung(t1.getText().toString(),t2.getText().toString(), d, e.erledigt);

        Intent intent = new Intent();
        intent.putExtra("Erinnerung-Neu", erinnerung);
        intent.putExtra("Erinnerung-Alt", e);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
