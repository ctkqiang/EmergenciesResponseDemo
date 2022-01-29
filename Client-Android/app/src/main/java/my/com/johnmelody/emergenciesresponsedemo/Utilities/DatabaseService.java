package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import my.com.johnmelody.emergenciesresponsedemo.Interfaces.GetUser;
import my.com.johnmelody.emergenciesresponsedemo.Model.DataItem;
import my.com.johnmelody.emergenciesresponsedemo.Model.EmergencyItem;
import my.com.johnmelody.emergenciesresponsedemo.Model.LocationItem;
import my.com.johnmelody.emergenciesresponsedemo.Model.UserItem;
import my.com.johnmelody.emergenciesresponsedemo.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DatabaseService extends LocalSharedPreference
{
    public Activity activity;
    public String TAG;
    public UserItem userItem;
    public DataItem dataItem;
    public EmergencyItem emergencyItem;
    public LocationItem locationItem;
    public String user = "user";
    public String emergency = "emergency";
    public ArrayList<String> result = new ArrayList<String>();

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

    public FirebaseAuth firebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }

    public FirebaseDatabase database()
    {
        return FirebaseDatabase.getInstance();
    }

    public DatabaseReference getDatabaseReference(String path)
    {
        return this.database().getReference(path);
    }

    public String getUserPhoneFromBranch(String email)
    {
        DatabaseService databaseService = new DatabaseService(this.activity, this.TAG);
        this.getDatabaseReference(this.user).child(util().replaceSpecialChar(email)).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snap : snapshot.getChildren())
                {
                    databaseService.result.add(snap.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        Log.d(TAG, "getUserPhoneFromBranch: " +result);

        return databaseService.result.toString();
    }

    public void writeUserDetails(String email, String phone, String password, double longi,
                                 double lati, int type)
    {
        this.dataItem = (DataItem) new DataItem(email, phone, util().setToMD5(password), longi,
                lati, type
        );
        this.getDatabaseReference(this.user).child(util().replaceSpecialChar(email)).setValue(dataItem).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Log.d(TAG, "=>>>>>>>>>>>>onComplete: " + task.isSuccessful());
            }
        });
        this.databaseHandler().insertData(util().replaceSpecialChar(email), password, type, phone);
        this.databaseHandler().insertLocationData(String.format("%s,%s", longi, lati));
    }

    public void writeCurrentLocation(String phone, double longitude, double latitude)
    {
        String currentUser = this.firebaseAuth().getCurrentUser().getEmail();
        this.emergencyItem = (EmergencyItem) new EmergencyItem(
                util().getCurrentDateTime(),
                phone,
                String.valueOf(latitude),
                String.valueOf(longitude)
        );

        Log.d(TAG, "writeCurrentLocation: " + this.emergencyItem);

        this.getDatabaseReference(this.emergency).child(util().replaceSpecialChar(currentUser)).setValue(emergencyItem).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Log.d(TAG, "=>>>>>>>>>>>>onComplete: " + task.isSuccessful());
            }
        });
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

    public String getCurrentUserPhone(Activity activity, String email)
    {
        List<String> result = new ArrayList<String>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetUser.endpoint)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        GetUser api = retrofit.create(GetUser.class);
        Call<String> call = api.getUser();

        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                if (response.isSuccessful())
                {
                    result.add(response.body());

                    util().showToast(activity, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {

            }
        });

        return result.toString();
    }

    public class GetUserData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            String result = null;

            URL url = null;
            try
            {
                url = new URL(GetUser.endpoint);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader inputStreamReader =
                            new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null)
                    {
                        stringBuilder.append(temp);
                    }

                    result = stringBuilder.toString();
                }
                else
                {
                    result = "Error";
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }
}
