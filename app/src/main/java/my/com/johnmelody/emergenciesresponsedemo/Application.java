package my.com.johnmelody.emergenciesresponsedemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Services;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Util;

public class Application extends AppCompatActivity implements LocationListener
{
    public static final String TAG = ConstantsValues.TAG_NAME;
    public static final int REQUEST_LOCATION = 0x1;
    protected LocationManager locationManager;
    private MapView mapView;
    public Services services;
    public Util util;

    public void initializeLocationService()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION
            );
        }

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public Mapbox initialiseMapBoxService(Activity activity)
    {
        Context context = activity.getApplicationContext();

        return Mapbox.getInstance(context, ConstantsValues.MAPBOX_TOKEN(context));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LogNotTimber")
    public void renderUserComponents(Activity activity)
    {
        /* Set Layout Content View  */
        activity.setContentView(R.layout.activity_main);

        /* Set Services & Utilities Initialised */
        services = (Services) new Services(TAG, activity);
        util = (Util) new Util();

        /* Set Action Bar To White Colour */
        Objects.requireNonNull(this.getSupportActionBar()).setBackgroundDrawable(
                new ColorDrawable(
                        this.getResources().getColor(R.color.white)
                )
        );

        /* Set Title for action Bar & set title colour */
        this.getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>" + ConstantsValues.APP_NAME + "</font>"));
        this.getSupportActionBar().setElevation(0x0);

        /* Set Status bar colour to white & Set Status bar icon to black */
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.white));
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Log.d(TAG, "Application:: renderedUserComponents");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Render User Components */
        Application.this.renderUserComponents(Application.this);

        /* Initialise Location Services */
        Application.this.initializeLocationService();

        /* Initialise Mapbox Services */
        Application.this.initialiseMapBoxService(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        Log.d(
                TAG,
                "onLocationChanged: [CurrentLocation] " +
                Arrays.toString(this.util.getLocationArray(location))
        );
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations)
    {
        /* Do Nothing */
    }

    @Override
    public void onFlushComplete(int requestCode)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider)
    {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider)
    {
        this.util.showToast(this,"Location Service are required.");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        /* Do Nothing */
    }
}