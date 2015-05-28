package com.example.mscheuringer.erinnerungen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michael on 21.05.2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "erinnerung.db";
    private final static int Version = 1;

    public MySQLiteHelper (Context context1)
    {
        super(context1,DB_NAME,null,Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ErinnerungTbl.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ErinnerungTbl.SQL_DROP);
        onCreate(db);
    }
}
