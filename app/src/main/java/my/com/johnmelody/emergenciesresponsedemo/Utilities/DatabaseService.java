package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import my.com.johnmelody.emergenciesresponsedemo.Model.DataItem;
import my.com.johnmelody.emergenciesresponsedemo.Model.LocationItem;
import my.com.johnmelody.emergenciesresponsedemo.Model.UserItem;

public class DatabaseService extends LocalSharedPreference
{
    public Activity activity;
    public String TAG;
    public UserItem userItem;
    public DataItem dataItem;
    public LocationItem locationItem;
    public String user = "user";
    public String emergency = "emergency";
    public String child = UUID.randomUUID().toString();

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

    public DatabaseReference getDatabaseReference(String path)
    {
        return this.database().getReference(path);
    }

    public void writeUserDetails(String email, String phone, String password, double longi,
                                 double lati, int type)
    {
        this.dataItem = (DataItem) new DataItem(email, phone, password, longi, lati, type);
        this.getDatabaseReference(this.user).child(this.child).setValue(dataItem).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Log.d(TAG, "=>>>>>>>>>>>>onComplete: " + task.isSuccessful());
            }
        });
        this.databaseHandler().insertData(email, password, type, phone);
        this.databaseHandler().insertLocationData(String.format("%s,%s", longi, lati));
    }

    public void writeCurrentLocation(double longitude, double latitude)
    {
        String phoneNumber = this.getUserPhoneNumber();
        this.locationItem = (LocationItem) new LocationItem(longitude, latitude);
        this.getDatabaseReference(this.emergency).child(phoneNumber).setValue(this.locationItem);
    }

    public void read(String path)
    {
        this.getDatabaseReference(path).addValueEventListener(new ValueEventListener()
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
