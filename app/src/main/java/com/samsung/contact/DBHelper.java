package com.samsung.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rajmani on 19-06-16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";
    public static final String DB_NAME = "contacts.db";
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID= "_id";
    public static final String COLUMN_FNAME = "first_name";
    public static final String COLUMN_LNAME = "last_name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_PICTURE= "picture";

    public DBHelper(Context context) {
        super(context,DB_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+
                TABLE_NAME+
                " ( "+COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_FNAME+" TEXT NOT NULL, " +
                COLUMN_LNAME+" TEXT NOT NULL, "+
                COLUMN_PHONE+" TEXT NOT NULL, "+
                COLUMN_EMAIL+" TEXT NOT NULL, "+
                COLUMN_LOCATION+" TEXT NOT NULL, "+
                COLUMN_PICTURE+" TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact (String fname, String lname, String phone, String email, String location, String picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FNAME, fname);
        contentValues.put(COLUMN_LNAME, lname);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_PICTURE, picture);
        db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "Inserting Contact");
        return true;
    }

    public Cursor getData (int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME +" where "+ COLUMN_ID +"="+id+"", null );
        return res;
    }

    public int numberOfRows (){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String fname, String lname, String phone, String email, String location, String picture)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FNAME, fname);
        contentValues.put(COLUMN_LNAME, lname);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_PICTURE, picture);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        Log.d(TAG, "Updating Contact");
        return true;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID+" = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<HashMap<String, String>> getAllContacts()
    {
        ArrayList<HashMap<String, String>> contact_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        HashMap<String, String> hashMap;

        while(res.isAfterLast() == false){
            hashMap = new HashMap<>();
            hashMap.put(COLUMN_ID, res.getString(0));
            hashMap.put(COLUMN_FNAME, res.getString(1));
            hashMap.put(COLUMN_LNAME, res.getString(2));
            hashMap.put(COLUMN_PHONE, res.getString(3));
            hashMap.put(COLUMN_PICTURE, res.getString(6));
            contact_list.add(hashMap);
            res.moveToNext();
        }
        return contact_list;
    }

}
