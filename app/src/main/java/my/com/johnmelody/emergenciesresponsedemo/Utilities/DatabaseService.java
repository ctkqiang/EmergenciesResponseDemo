package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.app.Activity;

import com.google.firebase.database.FirebaseDatabase;

public class DatabaseService
{
    public Activity activity;
    public String TAG;

    public DatabaseService(Activity activity, String TAG)
    {
        this.activity = activity;
        this.TAG = TAG;
    }

    public FirebaseDatabase database()
    {
        return FirebaseDatabase.getInstance();
    }
}
