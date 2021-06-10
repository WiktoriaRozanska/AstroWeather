package com.example.astroweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "location_table";
    private static final String COL1 = "Localization";
    private static final String COL2 = "Latitude";
    private static final String COL3 = "Longitude";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+COL1 +" TEXT, "+COL2+" TEXT, "+COL3+" TEXT, UNIQUE("+ COL2+","+COL3+") ON CONFLICT REPLACE)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String city, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, city);
        contentValues.put(COL2, latitude);
        contentValues.put(COL3, longitude);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result<0)
            return false;
        return true;
    }

    public boolean deleteData(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        long result = 0 ;

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+ COL1 +" =\""+ id+"\";");

        if (result<0)
            return false;
        return true;
//        return db.delete(TABLE_NAME,"ID = ?",new String[] {id})>0;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
