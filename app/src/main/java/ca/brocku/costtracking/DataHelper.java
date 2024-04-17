package ca.brocku.costtracking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DB_NAME="costtracking";
    public static final String DB_TABLE="spendings";
    public static final String DB_TABLE_FAV="favourites";

    private static final String CREATE_TABLE="CREATE TABLE "
            +DB_TABLE+" (rule INTEGER PRIMARY KEY, userName TEXT, date TEXT, amount REAL, LocationName TEXT, time TEXT);";
    private static final String CREATE_TABLE_FAV="CREATE TABLE "
            +DB_TABLE_FAV+" (rule INTEGER PRIMARY KEY, userName TEXT, dateTime TEXT, amount REAL, LocationName TEXT);";

    DataHelper(Context context) {
        super(context,DB_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_FAV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //How to migrate or reconstruct data from old version to new on upgrade
    }

}
