package com.example.mscheuringer.erinnerungen;

/**
 * Created by Michael on 21.05.2015.
 */
public class ErinnerungTbl
{
    public final static String TABLE_NAME = "Erinnerungen";

    public final static String ID = "ID";
    public final static String Titel = "Titel";
    public final static String Notiz = "Notiz";
    public final static String Date = "Date";
    public final static String Erledigt = "Erledigt";
    public final static String[] ALL_COLUMNS = new String[] {ID+" AS _id",Titel,Notiz,Date,Erledigt};

    public static final String SQL_DROP = "DROP TABLE IF EXISTS "+ TABLE_NAME;
    public static final String SQL_CREATE =
            "CREATE TABLE "+ TABLE_NAME +
                    "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Titel + " TEXT NOT NULL,"+
                    Notiz + " TEXT NOT NULL,"+
                    Date + " TEXT NOT NULL,"+
                    Erledigt + " TEXT NOT NULL" +
                    ")";

    public static final String STMT_DELETE = "DELETE FROM "+ TABLE_NAME;
    public static final String STMT_INSERT =
            "INSERT INTO "+ TABLE_NAME +
                    "("+Titel+","+Notiz+","+Date+","+Erledigt+")" +
                    "VALUES (?,?,?,?,?)";
}
