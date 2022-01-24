package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.Interfaces.NotificationApi;
import my.com.johnmelody.emergenciesresponsedemo.Model.Notification;
import my.com.johnmelody.emergenciesresponsedemo.Model.PushNotification;
import my.com.johnmelody.emergenciesresponsedemo.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Services extends Util
{
    public int REQUEST_LOCATION = 0x1;
    public String TAG;
    public Activity activity;

    public Services(String TAG, Activity activity)
    {
        this.TAG = TAG;
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LogNotTimber")
    public Double[] getCurrentUserLocation()
    {
        LocationManager locationManager;
        locationManager =
                (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);

        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        /* Request Permissions from user */
        ActivityCompat.requestPermissions(this.activity, permission, REQUEST_LOCATION);

        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)))
            this.turnOnGPS();

        return this.getLocation();
    }

    public void turnOnGPS()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity.getBaseContext());

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }
        ).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    public Double[] getLocation()
    {
        Double[] result = new Double[]{};

        LocationManager locationManager;
        locationManager =
                (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION
            );
        }
        else
        {
            Location locationGPS =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (locationGPS != null)
            {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();

                result = new Double[]{longi, lat};
            }
            else
            {
                Toast.makeText(this.activity, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }

        return result;
    }

    public void setPushNotificationService()
    {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this.activity);
        OneSignal.setAppId(ConstantsValues.ONE_SIGNAL_TOKEN(this.activity.getApplicationContext()));
    }

    public void broadcastToALl(String phone, double longitude, double latitude)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://onesignal.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        JSONObject body = new JSONObject();
        JSONObject headings = new JSONObject();
        JSONObject content = new JSONObject();
        JSONArray array = new JSONArray();
        List<String> list = new ArrayList<String>();

        list.add("Subscribed Users");

        array.put(list.get(0));
        try
        {
            headings.put("en", "[EMERGENCY]I Need help!");
            content.put("en", String.format("I am at %s%s|Myinfo: %s|", longitude, latitude,
                    phone
            ));
            body.put("app_id", this.activity.getResources().getString(R.string.one_signal_appId));
            body.put("headings", headings);
            body.put("contents", content);

            body.put("included_segments", array);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        PushNotification pushNotification = new PushNotification(body);

        Call<PushNotification> call = notificationApi.broadcastPushNotification(pushNotification);

        call.enqueue(new Callback<PushNotification>()
        {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response)
            {
                Log.d(
                        TAG,
                        "+++++++++++++onResponse: " + body.toString()
                );
                Log.d(TAG, ">>>>>>onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t)
            {
                Log.d(TAG, ">>>>>>>onFailure: " + t);
            }
        });
    }

    public void broadCastToActive(String phone, double longitude, double latitude)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"app_id\": "
                                                         + "\"42e96cdb-46b2-4d1a-9f73"
                                                         + "-0fc3e542710e\",\r\n  "
                                                         + "\"included_segments\": [\"Subscribed "
                                                         + "Users\"],\r\n  \"headings\": {\"en\":"
                                                         + " \"[EMERGENCY]I Need help!\"\"},\r\n "
                                                         + " \"contents\": "
                                                         + "{\"en\": \"" + String.format("I am at"
                                                                                         + " %s%s"
                                                                                         +
                                                                                         "|Myinfo"
                                                                                         + ": %s|", longitude, latitude, phone) + "\"}\r\n}");
        Request request = new Request.Builder()
                .url("https://onesignal.com/api/v1/notifications")
                .method("POST", body)
                .addHeader("Content-Type", "application/json; charset=utf-8\"")
                .addHeader("Authorization", "Basic "
                                            + "MjQwYmUwZDAtNGU0NC00MDY0LTgyOTEtODE5NDA3MzFiYTk3")
                .build();
        try
        {
            okhttp3.Response response = client.newCall(request).execute();
            Log.d(TAG, "broadCastToActive: " + response);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
