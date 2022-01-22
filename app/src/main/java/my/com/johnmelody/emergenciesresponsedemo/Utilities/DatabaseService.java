package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.com.johnmelody.emergenciesresponsedemo.Model.LocationItem;
import my.com.johnmelody.emergenciesresponsedemo.Model.UserItem;

public class DatabaseService
{
    public Activity activity;
    public String TAG;
    public UserItem userItem;
    public LocationItem locationItem;
    private final String path = "users";

    private Util util()
    {
        return new Util();
    }

    public DatabaseService(Activity activity, String TAG)
    {
        this.activity = activity;
        this.TAG = TAG;
    }

    public DatabaseHandler databaseHandler()
    {
        return new DatabaseHandler(this.activity.getApplicationContext());
    }

    public FirebaseDatabase database()
    {
        return FirebaseDatabase.getInstance();
    }

    public DatabaseReference getPath(String path)
    {
        return this.database().getReference(path);
    }

    public void writeUserDetails(String email, String password, int type)
    {
        this.userItem = (UserItem) new UserItem(email, password, type);
        this.getPath(this.path).setValue(this.userItem);
        this.databaseHandler().insertData(email, password, type);
    }

    public void writeCurrentLocation(double longitude, double latitude)
    {
        this.locationItem = (LocationItem) new LocationItem(longitude, latitude);
        this.getPath(this.path).setValue(this.locationItem);
        this.databaseHandler().insertLocationData(
                String.valueOf(String.format("%s,%s", longitude, latitude))
        );
    }

    public void read(String path)
    {
        this.getPath(path).addValueEventListener(new ValueEventListener()
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
