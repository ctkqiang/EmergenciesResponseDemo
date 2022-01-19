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

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Services extends Util
{
    public int REQUEST_LOCATION = 0x1;
    public String TAG;
    public Activity activity;

    public Services(@NotNull String TAG, @NotNull Activity activity)
    {
        this.TAG = TAG;
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LogNotTimber")
    public String[] getCurrentUserLocation()
    {
        LocationManager locationManager;
        locationManager = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);

        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        /* Request Permissions from user */
        ActivityCompat.requestPermissions(this.activity, permission, REQUEST_LOCATION);

        if (! (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) this.turnOnGPS();

        Log.d(this.TAG, "getCurrentUserLocation: " + Arrays.toString(this.getLocation()));

        return this.getLocation();
    }

    public void turnOnGPS()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity.getBaseContext());

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener()
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

    public String[] getLocation()
    {
        String[] result = new String[]{};

        LocationManager locationManager;
        locationManager = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (locationGPS != null)
            {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();

                result = new String[]{String.valueOf(lat), String.valueOf(longi)};
            }
            else
            {
                Toast.makeText(this.activity, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }

        return result;
    }
}
