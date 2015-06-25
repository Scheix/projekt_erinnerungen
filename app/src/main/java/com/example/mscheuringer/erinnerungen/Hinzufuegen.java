package com.example.mscheuringer.erinnerungen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mscheuringer on 19.05.2015.
 */
public class Hinzufuegen extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hinzufuegen);
        Log.d("INFO", "Hinzufuegen-Acivity");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void backToMain (final View source)
    {
        Log.d("INFO","Hinzufuegen - backToMain");
        EditText t1 = (EditText) findViewById(R.id.editText);
        EditText t2 = (EditText) findViewById(R.id.editText2);
        DatePicker date = (DatePicker) findViewById(R.id.datePicker);

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

        Erinnerung e = new Erinnerung(t1.getText().toString(), t2.getText().toString(), d,false);

        Intent intent = new Intent();
        intent.putExtra("Erinnerung", e);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
