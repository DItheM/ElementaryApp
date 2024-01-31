package com.example.elementaryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserData.db";
    public static final String TABLE_NAME = "User_Data_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USER_NAME";
    public static final String COL_3 = "PFP_URL";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " + TABLE_NAME + " (" + COL_1 + " TEXT PRIMARY KEY , " + COL_2 + " TEXT , " + COL_3 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //data insertion method
    public boolean insertData (String id, String userName, String pfpURL) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, userName);
        contentValues.put(COL_3, pfpURL);
        long insertionResult = database.insert(TABLE_NAME, null, contentValues);
        return (insertionResult != -1);
    }

    //data retrieval method
    public Cursor getAllData () {
        SQLiteDatabase database = this.getWritableDatabase();
        String getAllDataQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor result = database.rawQuery(getAllDataQuery, null);
        return result;
    }

    //data updation method
    public boolean updateUserName (String id, String userName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, userName);
        int numberOfAffectedRows =database.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
        return (numberOfAffectedRows > 0);
    }

    //update pfp url
    public boolean updatePFPUrl (String id, String pfpURL) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, pfpURL);
        int numberOfAffectedRows =database.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
        return (numberOfAffectedRows > 0);
    }

    //data deletion method
    public boolean deleteData (String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        int deletedDataRows = database.delete(TABLE_NAME, "ID = ?", new String[] {id});
        return (deletedDataRows > 0);
    }

    //delete all data
    public void deleteAllData () {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }
}
