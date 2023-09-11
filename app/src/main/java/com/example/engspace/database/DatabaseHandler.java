package com.example.engspace.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.engspace.model.SetTable;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "offlineDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "sets";

    private static final String KEY_ID = "id";
    private static final String KEY_SET = "set_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_sets_table = String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INT, %s TEXT, %s INT)", TABLE_NAME, KEY_ID, KEY_SET, KEY_NAME, KEY_AMOUNT);
        db.execSQL(create_sets_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_sets_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_sets_table);
        onCreate(db);
    }

    public ArrayList<SetTable> getAllSets() {
        ArrayList<SetTable>  setList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " DESC LIMIT 20";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            SetTable setTable = new SetTable(cursor.getInt(1), cursor.getString(2), cursor.getInt(3));
            setList.add(setTable);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return setList;
    }

    public SetTable getSet(int setId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, KEY_SET + " = ?", new String[] { String.valueOf(setId) },null, null, null);
        SetTable setTable = null;
        if (cursor.moveToFirst()) {
            setTable = new SetTable(cursor.getInt(1), cursor.getString(2), cursor.getInt(3));
        }
        cursor.close();
        db.close();
        return setTable;
    }

    public void addSet(SetTable setTable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SET, setTable.getSet());
        values.put(KEY_NAME, setTable.getName());
        values.put(KEY_AMOUNT, setTable.getAmount());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateSet(SetTable setTable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, setTable.getName());
        values.put(KEY_AMOUNT, setTable.getAmount());
        values.put(KEY_SET, setTable.getSet());

        db.update(TABLE_NAME, values, KEY_SET + " = ?", new String[] { String.valueOf(setTable.getSet()) });
        db.close();
    }

    public void deleteSet(int setId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_SET + " = ?", new String[] { String.valueOf(setId) });
        db.close();
    }

    public void deleteAllSets() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
