package com.ugkr.lessons.LinksLists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabiusbile on 12.02.17.
 */

public class LinksList extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "LinksDatabase";

    public LinksList(Context context) {
        super(context, DATABASE_NAME, null, 3);
        context.openOrCreateDatabase(DATABASE_NAME,Context.MODE_PRIVATE,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateGroupsTable = "CREATE TABLE Groups (code TEXT PRIMARY KEY, name TEXT);";
        String CreateTeachersTable = "CREATE TABLE  Teachers (code TEXT PRIMARY KEY, name TEXT);";
        db.execSQL(CreateGroupsTable);
        db.execSQL(CreateTeachersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Groups;");
        db.execSQL("DROP TABLE IF EXISTS Teachers;");
        onCreate(db);
        db.close();
    }

    public void addNameCodePair(String table, String name, String code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Code",code);
        values.put("Name",name);
        db.replace(table,null,values);
        db.close();
    }

    public List<NameCodePair> GetLinks (String table){
        List<NameCodePair> links = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+table+";";

        Cursor c = db.rawQuery(selectQuery,null);

        if (c.moveToFirst()){
            do {
                NameCodePair nameCodePair = new NameCodePair();
                nameCodePair.name = c.getString(1);
                nameCodePair.code = c.getString(0);
                links.add(nameCodePair);
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return links;
    }
}
