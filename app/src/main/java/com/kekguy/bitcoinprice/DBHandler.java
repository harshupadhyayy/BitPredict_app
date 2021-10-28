package com.kekguy.bitcoinprice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "porfolio";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "portfoliodata";
    private static final String ID_COL = "_id";
    private static final String BITCOIN_COL = "bitcoin";
    private static final String MONEY_COL = "money";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public long addNewBitcoinData(String bitcoin, String money) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BITCOIN_COL, bitcoin);
        values.put(MONEY_COL, money);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<String> readBitcoinData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id = (SELECT MAX(_id) FROM portfoliodata);", null);
        StringBuilder buffer = new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String bitcoins = cursor.getString(1);
            String money = cursor.getString(2);
            res.add(bitcoins);
            res.add(money);

            buffer.append("ID is: ").append(id).append(" ")
                    .append("Username is: ").append(bitcoins).append(" ")
                    .append("Password is: ").append(money).append("\n\n");
        }
        cursor.close();
        Log.d("data", buffer.toString());
        Log.d("res[0]", res.get(0));
        return res;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BITCOIN_COL + " TEXT, "
                + MONEY_COL + " TEXT)";
        String bitcoins = "0";
        String money = "0";
        String first_entry = "INSERT INTO " + TABLE_NAME + " VALUES(1," + bitcoins + "," + money + ")";
        db.execSQL(query);
        db.execSQL(first_entry);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
