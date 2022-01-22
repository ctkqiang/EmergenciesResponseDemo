package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "bfdb";
    private static final int DATABASE_VERSION = 1;
    public static int counter;

    public DatabaseHandler(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        String query = "CREATE TABLE EMERGENCIES( ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL "
                       + "TEXT, PASSWORD TEXT, CURRENTLOCATION TEXT, TYPE INT)";

        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS EMERGENCIES");
        this.onCreate(database);
    }

    public void insertData(String email, String password, int type)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("EMAIL", email);
        values.put("PASSWORD", password);
        values.put("TYPE", type);

        database.insert("EMERGENCIES", null, values);
        database.close();
    }

    public void insertLocationData(String currentLocation)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("CURRENTLOCATION", currentLocation);

        database.insert("EMERGENCIES", null, values);
        database.close();
    }

    @SuppressLint("Range")
    public int getType()
    {
        SQLiteDatabase database = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT TYPE FROM EMERGENCIES WHERE ID = 1 ", null);

        if (cursor.moveToFirst())
        {
            counter = cursor.getInt(cursor.getColumnIndex("REPORTED"));
        }
        else
        {
            counter = 0;
        }

        return counter;
    }

    @SuppressLint("Range")
    public ArrayList getEmail()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM EMERGENCIES",
                null
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            arrayList.add(cursor.getString(cursor.getColumnIndex("email")));
            cursor.moveToNext();
        }

        return arrayList;
    }
}
