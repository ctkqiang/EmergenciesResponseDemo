package my.com.johnmelody.emergenciesresponsedemo;

import static android.Manifest.permission.READ_PHONE_STATE;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Services;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Application extends AppCompatActivity implements LocationListener, OnMapReadyCallback
{
    public static final String TAG = ConstantsValues.TAG_NAME;
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    public static final int REQUEST_LOCATION = 0x1;
    protected LocationManager locationManager;
    private MapboxDirections mapboxDirectionsClient;
    private DirectionsRoute currentRoute;
    private FloatingActionButton floatingActionButton;
    private MapboxMap mapboxMap;
    private Point user;
    private Point help;
    public MapView mapView;
    public TextView locationView;
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

        /* Render Layout Components */
        this.locationView = (TextView) this.findViewById(R.id.currentLocation);
        this.locationView.setSelected(true);
        this.locationView.setPadding(0xa, 0x0, 0xa, 0x0);
        this.locationView.setMarqueeRepeatLimit(0xffffffff);
        this.locationView.setVerticalScrollBarEnabled(true);
        this.locationView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.locationView.setMovementMethod(new ScrollingMovementMethod());
        this.floatingActionButton = (FloatingActionButton) this.findViewById(R.id.fabtn);
        this.floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view)
            {
                /* TODO */

                Log.d(TAG, "Sending Help...");
            }
        });

        /*
         * Request READ_PHONE_STATE permission
         * Refer https://stackoverflow.com/q/70803660/10758321
         */
        this.util.requestSecurityPermission(activity);

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
        Mapbox.getInstance(this, ConstantsValues.MAPBOX_TOKEN(this));

        /* Set Layout Content View  */
        Application.this.setContentView(R.layout.activity_main);

        /* Render User Components */
        Application.this.renderUserComponents(Application.this);

        /* Set MapView to Instances of the current activity */
        Application.this.mapView = (MapView) this.findViewById(R.id.mapview);
        Application.this.mapView.onCreate(savedInstanceState);
        Application.this.mapView.getMapAsync(this);

        /* Initialise Location Services */
        Application.this.initializeLocationService();
    }

    public void initiateSource(@NonNull Style loadMapStyle)
    {
        loadMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconOfGeoJsonSource = new GeoJsonSource(
                ICON_SOURCE_ID,
                FeatureCollection.fromFeatures(
                        new Feature[]{
                                Feature.fromGeometry(
                                        Point.fromLngLat(
                                                Application.this.user.longitude(),
                                                Application.this.user.latitude()
                                        )
                                ),
                                Feature.fromGeometry(
                                        Point.fromLngLat(
                                                Application.this.help.longitude(),
                                                Application.this.help.latitude()
                                        )
                                )
                        }
                )
        );

        loadMapStyle.addSource(iconOfGeoJsonSource);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initiateLayers(@NonNull Style loadedMapStyle)
    {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        /* Add the LineLayer to the map. This layer will display the directions route. */
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

        /* Add the red marker icon image to the map */
        loadedMapStyle.addImage(
                RED_PIN_ICON_ID,
                Objects.requireNonNull(
                        BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.red_marker))
                )
        );

        /* Add the red marker icon SymbolLayer to the map */
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[]{0f, -9f})
        ));
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        /* Set Current Location */
        new Handler(this.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                Application.this.locationView.append(
                        String.format(
                                "-[%s]-> Longitude: %s\t\tLatitude: %s\n",
                                Application.this.util.getCurrentDateTime(),
                                Application.this.util.getLocationArray(location)[0],
                                Application.this.util.getLocationArray(location)[1]
                        )
                );
            }
        });

        Log.d(
                TAG,
                "onLocationChanged: [CurrentLocation] " +
                Arrays.toString(this.util.getLocationArray(location))
        );
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations)
    {
        /* Set Current Location */
        Application.this.locationView.setText(locations.toString());
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

    public void getRoute(MapboxMap mapboxMap, Point origin, Point destination)
    {
        this.mapboxDirectionsClient = MapboxDirections
                .builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(ConstantsValues.MAPBOX_TOKEN(this.getApplicationContext()))
                .build();

        this.mapboxDirectionsClient.enqueueCall(new Callback<DirectionsResponse>()
        {
            @Override
            public void onResponse(Call<DirectionsResponse> call,
                                   Response<DirectionsResponse> response)
            {
                Log.d(TAG, String.format("onResponse: %d", response.code()));

                if (response.body() == null)
                {
                    Application.this.util.showToast(
                            Application.this.getApplicationContext(),
                            "The Access Token Invalid"
                    );
                }
                else if (response.body().routes().size() < 1)
                {
                    Application.this.util.showToast(
                            Application.this.getApplicationContext(),
                            "No route found"
                    );
                }
                else
                {
                    /* Get Directions route */
                    Application.this.currentRoute = response.body().routes().get(0);

                    /* Display Toast output of the distance */
                    Application.this.util.showToast(
                            Application.this.getApplicationContext(),
                            String.format(
                                    "The Distance%s",
                                    Application.this.currentRoute.distance()
                            )
                    );

                    if (mapboxMap == null)
                    {
                        Log.d(TAG, "onResponse::map == null");
                    }
                    else
                    {
                        mapboxMap.getStyle(new Style.OnStyleLoaded()
                        {
                            @Override
                            public void onStyleLoaded(@NonNull Style style)
                            {
                                /*
                                 * Retrieve and update the source designated for showing the
                                 * directions route
                                 */
                                GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                                /*
                                 * Create a LineString with the directions route's geometry and
                                 * reset
                                 * the GeoJSON source for the route LineLayer source
                                 */
                                if (source != null)
                                {
                                    source.setGeoJson(
                                            LineString.fromPolyline(
                                                    currentRoute.geometry(),
                                                    PRECISION_6
                                            )
                                    );
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t)
            {
                Application.this.util.showToast(
                        Application.this.getApplicationContext(),
                        t.getMessage()
                );

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        /* Cancel the Directions API request*/
        if (this.mapboxDirectionsClient != null)
        {
            this.mapboxDirectionsClient.cancelCall();
        }

        this.mapView.onDestroy();
        this.util.showToast(this, "kill");
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.mapView.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.mapView.onStop();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.mapView.onPause();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap)
    {
        Application.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onStyleLoaded(@NonNull Style style)
            {
                Application.this.user = Point.fromLngLat(
                        Application.this.services.getCurrentUserLocation()[0],
                        Application.this.services.getCurrentUserLocation()[1]
                );

                /* TODO Change this to HTTP_REQUEST coordinates from the server side*/
                Application.this.help = Point.fromLngLat(
                        Application.this.services.getCurrentUserLocation()[0] - 0x1.0p0,
                        Application.this.services.getCurrentUserLocation()[1] - 0x1.0p0
                );

                /* Render sources and Fixtures */
                Application.this.initiateSource(style);

                /* Render Map Layers */
                Application.this.initiateLayers(style);

                /* Get the directions route from the Mapbox Directions API */
                Application.this.getRoute(
                        mapboxMap,
                        Application.this.user,
                        Application.this.help
                );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 100:
                Application.this.util.getPhoneInfo(Application.this);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }

    }
}