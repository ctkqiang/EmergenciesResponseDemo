package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public DatabaseReference getPath()
    {
        final String PATH = "";
        return this.database().getReference(PATH);
    }

    public void write(String value)
    {
        this.getPath().setValue(value);
        Log.d(TAG, "Database -> write: " + value);
    }

    public void read()
    {
        this.getPath().addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String value = snapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
