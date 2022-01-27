package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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
                       + "TEXT, PASSWORD TEXT, PHONE_NUMBER TEXT, CURRENTLOCATION TEXT, TYPE INT)";

        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS EMERGENCIES");
        this.onCreate(database);
    }

    public SQLiteDatabase sqLiteDatabase()
    {
        return this.getWritableDatabase();
    }

    public void insertData(String email, String password, int type, String phone)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("EMAIL", email);
        values.put("PASSWORD", password);
        values.put("TYPE", type);
        values.put("PHONE_NUMBER", phone);

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
        @SuppressLint("Recycle")
        Cursor cursor = this.sqLiteDatabase()
                .rawQuery(
                        "SELECT TYPE FROM EMERGENCIES WHERE ID = 1" + " ",
                        null
                );

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
        ArrayList<String> arrayList = new ArrayList<String>();

        @SuppressLint("Recycle") Cursor cursor = this.sqLiteDatabase().rawQuery(
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

    public String getPhoneNumber(String email)
    {
        Cursor cursor = null;
        String phone = null;
        try
        {
            cursor = this.sqLiteDatabase().rawQuery(
                    "SELECT PHONE_NUMBER FROM EMERGENCIES WHERE EMAIL + ?",
                    new String[]{email}
            );

            if (cursor.getCount() > 0x0)
            {
                cursor.moveToFirst();

                phone = cursor.getString(0x0);
            }

            return (phone != null) ? phone : "no-phone-number";
        }
        finally
        {
            cursor.close();
        }
    }


}
