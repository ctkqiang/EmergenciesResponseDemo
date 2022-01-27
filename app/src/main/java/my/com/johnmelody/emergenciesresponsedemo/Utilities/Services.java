package my.com.johnmelody.emergenciesresponsedemo.Utilities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Application;
import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.Interfaces.NotificationApi;
import my.com.johnmelody.emergenciesresponsedemo.Model.PushNotification;
import my.com.johnmelody.emergenciesresponsedemo.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
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

    /**
     * @deprecated
     */
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
        NotificationManagerCompat notificationManagerCompat;
        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    "channel",
                    this.activity.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = (NotificationManager) this.activity.getSystemService(
                    NotificationManager.class
            );

            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this.activity.getApplicationContext(),
                "channel"
        );

        builder.setContentTitle(this.activity.getString(R.string.sos));
        builder.setContentText(String.format("I'm at %s,%s\nh/p:%s", latitude, longitude, phone));
        builder.setSmallIcon(R.mipmap.emergency_icon);
        builder.setNumber(1);

        notification = builder.build();

        notificationManagerCompat = NotificationManagerCompat.from(
                this.activity.getApplicationContext()
        );

        notificationManagerCompat.notify(1, notification);

    }
}
