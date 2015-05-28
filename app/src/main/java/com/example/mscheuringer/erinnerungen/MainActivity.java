package com.example.mscheuringer.erinnerungen;

import android.app.Activity;
import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {

    ListView view;
    List<Erinnerung> list = new ArrayList<Erinnerung>();
    private static final int REQUEST_CODE = 666;
    private static int LIST_COUNTER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (ListView) findViewById(R.id.listView);
        registerForContextMenu(findViewById(R.id.listView));
        loadData();
        displayItems();
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
        //String [] from = new String[] {ErinnerungTbl.Titel, ErinnerungTbl.Date};
        //int[] to = new int[] {R.id.lblTitel, R.id.lblTime};
        MyAdapter adapter = new MyAdapter(this,R.layout.list_layout, R.id.listView,list);
        view.setAdapter(adapter);
    }

    public void hinzufuegen (final View source)
    {
        Intent intent = new Intent(this, Hinzufuegen.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK)
        {
            Bundle params = data.getExtras();

            if (params != null)
            {
                Erinnerung e = (Erinnerung) params.get("Erinnerung");
                list.add(e);
                insert(e);
                increase_counter();
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

    private void increase_counter ()
    {
        LIST_COUNTER = list.size();
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(LIST_COUNTER+" Einträge");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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
                delete(pos);
                loadData();
                displayItems();
                break;
            case R.id.sdf:
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

    public void delete(int pos)
    {
        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        long nrDeleted = db.delete("Erinnerungen", "ID=?", new String[]{""+pos});
    }

    public void insert (Erinnerung e)
    {
        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String erledigt;
        if (e.erledigt == true)
        {
            erledigt = "true";
        }
        else
        {
            erledigt = "flase";
        }

        ContentValues values = new ContentValues();
        values.put("Titel", e.title);
        values.put("Notiz", e.note);
        values.put("Date", e.date);
        values.put("Erledigt", erledigt);

        long insertedID = db.insert("Erinnerungen", null, values);
        db.close();
    }

    public void update(Erinnerung e, int pos)
    {
        MySQLiteHelper helper = new MySQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Titel", e.title);
        values.put("Notiz", e.note);
        values.put("Date", e.date);
        values.put("Erledigt", e.erledigt);

        long nrUpdated = db.update("Erinnerungen", values, "ID=?", new String[]{""+pos});
        db.close();
    }
}
