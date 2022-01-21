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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LogNotTimber")
    public void renderUserComponents(Activity activity)
    {
        /* Set Services & Utilities Initialised */
        services = (Services) new Services(TAG, activity);
        util = (Util) new Util();

        /* Request READ_PHONE_STATE permission */
        this.util.requestSecurityPermission(activity); /* Refer https://stackoverflow
        .com/q/70803660/10758321 */

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

        Log.d(TAG, "Application :: renderUserComponents :: getServices ");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Initialise Mapbox Services */
        Mapbox.getInstance(this, this.getResources().getString(R.string.mapbox_token));

        /* Set Layout Content View  */
        Application.this.setContentView(R.layout.activity_main);

        /* Render User Components */
        Application.this.renderUserComponents(Application.this);

        /* Set MapView to Instances of the current activity */
        Application.this.mapView = (MapView) this.findViewById(R.id.mapview);
        //Application.this.mapView.onCreate(savedInstanceState);

        /* Initialise Location Services */
        Application.this.initializeLocationService();
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
        Log.d(TAG, String.format("onFlushComplete: %d", requestCode));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.d(TAG, String.format("onStatusChanged: %s%d", provider, status));
    }

    @Override
    public void onProviderEnabled(@NonNull String provider)
    {
        Log.d(TAG, String.format("onProviderEnabled: %s", provider));
    }

    @Override
    public void onProviderDisabled(@NonNull String provider)
    {
        this.util.showToast(this, "Location Service Are Required.");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        /* Do Nothing */
    }
}