package com.ugkr.lessons.LinksLists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabiusbile on 12.02.17.
 */

public class LinksList extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "LinksDatabase";

    public LinksList(Context context) {
        super(context, DATABASE_NAME, null, 6);
        context.openOrCreateDatabase(DATABASE_NAME,Context.MODE_PRIVATE,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateGroupsTable = "CREATE TABLE Groups (code TEXT PRIMARY KEY, name TEXT, favourite BOOLEAN, isGroup BOOLEAN);";
        db.execSQL(CreateGroupsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Groups;");
        onCreate(db);
    }

    public void addNameCodePair(String name, String code, boolean favourite, boolean isGroup){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Code",code);
        values.put("Name",name);
        values.put("Favourite", favourite);
        values.put("isGroup", isGroup);
        db.replace("Groups",null,values);
        db.close();
    }
    public void addNameCodePair(String name, String code, boolean isGroup){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Code",code);
        values.put("Name",name);
        values.put("isGroup", isGroup);
        db.replace("Groups",null,values);
        db.close();
    }

    public List<NameCodePair> GetLinks (Boolean isGroup, Boolean onlyFav){
        List<NameCodePair> links = new ArrayList<>();
        int isGroupInt = (isGroup) ? 1 : 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "";
        if (onlyFav){
            selectQuery = "SELECT * FROM Groups WHERE favourite = 1 ORDER BY favourite DESC;";
        } else {
            selectQuery = "SELECT * FROM Groups WHERE isGroup = "+isGroupInt+" ORDER BY favourite DESC;";
        }

        Cursor c = db.rawQuery(selectQuery,null);
        try {
            if (c.moveToFirst()) {
                do {
                    NameCodePair nameCodePair = new NameCodePair();
                    nameCodePair.code = c.getString(0);
                    nameCodePair.name = c.getString(1);
                    nameCodePair.favourite = c.getInt(2) > 0;
                    nameCodePair.isGroup = c.getInt(3) > 0;
                    links.add(nameCodePair);
                } while (c.moveToNext());
            }
        } finally{
            c.close();
        }
        db.close();
        return links;
    }
}

