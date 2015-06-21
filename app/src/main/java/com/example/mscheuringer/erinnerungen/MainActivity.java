package com.example.mscheuringer.erinnerungen;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {

    ListView view;
    List<Erinnerung> list = new ArrayList<Erinnerung>();
    List<Erinnerung> done = new ArrayList<Erinnerung>();
    private static final int REQUEST_CODE = 666;
    private static final int REQUEST_CODE2 = 555;
    private static final int REQUEST_CODE3 = 444;
    private static int LIST_COUNTER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (ListView) findViewById(R.id.listView);
        registerForContextMenu(view);
        loadData();
        analyse_list();
        displayItems();
        /*for (int i = 0; i < list.size(); i++)
        {
            Erinnerung e = list.get(i);
            String s1 = e.date;

            Date d = new Date(System.currentTimeMillis());

            int year = d.getYear();
            int month = d.getMonth();
            int day = d.getDay();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            String s2 = null;

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");//("MM/dd/yyyy")

            if (calendar != null) {
                s2 = format.format(calendar.getTime());
            }

            if (s1.equals(s2))
            {
                showInStatusBar();
            }
        }*/
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

    public void displayItems ()
    {
        MyAdapter adapter = new MyAdapter(this,R.layout.list_layout, R.id.listView,list);
        view.setAdapter(adapter);
    }

    public void hinzufuegen (final View source)
    {
        Log.d("INFO","Hinzufuegen");
        Intent intent = new Intent(this, Hinzufuegen.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("INFO","onActivityResult");
        if(requestCode == REQUEST_CODE)
        {
            Log.d("INFO","resultCode");
            Bundle params = data.getExtras();

            if (params != null)
            {
                Erinnerung e = (Erinnerung) params.get("Erinnerung");
                insert(e);
                Log.d("INFO","Insert");
                loadData();
                analyse_list();
                increase_counter();
                displayItems();
            }
        }
        else if (requestCode == REQUEST_CODE3){
            Bundle params = data.getExtras();

            if (params != null)
            {
                Erinnerung e = (Erinnerung) params.get("Erinnerung");
                update(e);
                loadData();
                analyse_list();
                displayItems();
            }
        }
        else
        {
            Log.d("ERROR","A Failure accured !");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        increase_counter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(333);
    }

    private void analyse_list ()
    {
        done.clear();
        for (int i =0; i < list.size(); i++)
        {
            Erinnerung e = list.get(i);
            if (e.erledigt == true)
            {
                done.add(e);
                list.remove(i);
            }
        }
    }

    private void increase_counter ()
    {
        LIST_COUNTER = list.size();
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(LIST_COUNTER+" Einträge");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.d("INFO","onCreateContextMenu");
        getMenuInflater().inflate(R.menu.context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("INFO","onContextItemSelected");

        int id = item.getItemId();
        ContextMenu.ContextMenuInfo info = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)info;
        long entryID = adapterContextMenuInfo.id;
        int pos = adapterContextMenuInfo.position;

        Erinnerung erinnerung = list.get(pos);

        switch(id){
            case R.id.delete:
                delete(erinnerung);
                loadData();
                analyse_list();
                increase_counter();
                displayItems();
                break;
            case R.id.update:
                Intent i = new Intent(this, Update.class);
                i.putExtra("Erinnerung", erinnerung);
                startActivityForResult(i, REQUEST_CODE3);
                break;
            case R.id.detail:
                Intent intent = new Intent(this, Detalis.class);
                intent.putExtra("Erinnerung", erinnerung);
                startActivityForResult(intent, REQUEST_CODE2);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void loadData ()
    {
        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor rows = db.query("Erinnerungen",new String[]{"Titel","Notiz","Date","Erledigt"},null,null,null,null,null);

        list.clear();
        boolean erledigt;

        while(rows.moveToNext())
        {
            String s = rows.getString(0)+":"+rows.getString(1)+":"+rows.getString(2)+":"+rows.getString(3);
            String[]a = s.split(":");
            if (a[3].equals("false"))
            {
                erledigt = false;
            }
            else
            {
                erledigt = true;
            }
            Erinnerung e = new Erinnerung (a[0],a[1],a[2],erledigt);
            list.add(e);
        }
        rows.close();
        db.close();
    }

    public void delete(Erinnerung e)
    {
        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        SQLiteDatabase db2 = helper.getReadableDatabase();
        Cursor rows = db2.query("Erinnerungen",new String[]{"ID"},"Titel=?",new String[]{""+e.title},null,null,null);
        int id = 0;
        if (rows.moveToNext()) {
            id = rows.getInt(0);
        }
        long nrDeleted = db.delete("Erinnerungen", "ID=?", new String[]{""+id});
        rows.close();
        db.close();
    }

    public void insert (Erinnerung e)
    {
        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Titel", e.title);
        values.put("Notiz", e.note);
        values.put("Date", e.date);
        values.put("Erledigt", "false");

        long insertedID = db.insert("Erinnerungen", null, values);
        db.close();
    }

    public void update(Erinnerung e)
    {
        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String title = e.title;

        ContentValues values = new ContentValues();
        values.put("Titel", e.title);
        values.put("Notiz", e.note);
        values.put("Date", e.date);
        values.put("Erledigt", e.erledigt);

        SQLiteDatabase db2 = helper.getReadableDatabase();
        Cursor rows = db2.query("Erinnerungen",new String[]{"ID"},"Titel=?",new String[]{""+title},null,null,null);
        int id = 0;
        if(rows.moveToNext()) {
            id = rows.getInt(0);
        }

        long nrUpdated = db.update("Erinnerungen", values, "ID=?", new String[]{""+id});
        db.close();
        rows.close();
    }

    private void showInStatusBar()
    {
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification note = new Notification(
                R.drawable.icon,
                "Eine Erinnerung steht an !",
                System.currentTimeMillis());
        Intent i = new Intent(this, MainActivity.class);
        final PendingIntent intent = PendingIntent.getActivity(
                this,
                0,
                i,
                0);
        note.setLatestEventInfo(this, "Erinnerung", "Ich sollte Sie heute an etwas erinnern !", intent);
        note.vibrate = new long[] {100,200};
        manager.notify(333,note);
    }

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        RadioButton b = (RadioButton)findViewById(R.id.radioButton1);
        String action;
        int pos  = position;
        if (b.isChecked())
        {
            action = "Anruf";
        }
        else
        {
            action = "Karte";
        }
        Intent intent = new Intent(this, com.example.kontakte.Menu.class);
        intent.putExtra("ArrayList", kontakte);
        intent.putExtra("Action", action);
        intent.putExtra("ID", pos);
        startActivity(intent);
    }*/

}
