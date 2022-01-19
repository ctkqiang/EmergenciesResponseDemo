package my.com.johnmelody.emergenciesresponsedemo;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.geometryType;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Arrays;
import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Services;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Util;

public class Application extends AppCompatActivity implements MapboxMap.OnMapClickListener
{
    public static final String TAG = ConstantsValues.TAG_NAME;
    public static final String TOKEN = ConstantsValues.MAPBOX_TOKEN;
    private static final String ISOCHRONE_RESPONSE_GEOJSON_SOURCE_ID = "ISOCHRONE_RESPONSE_GEOJSON_SOURCE_ID";
    private static final String ISOCHRONE_FILL_LAYER = "ISOCHRONE_FILL_LAYER";
    private static final String ISOCHRONE_LINE_LAYER = "ISOCHRONE_LINE_LAYER";
    private static final String TIME_LABEL_LAYER_ID = "TIME_LABEL_LAYER_ID";
    private static final String MAP_CLICK_SOURCE_ID = "MAP_CLICK_SOURCE_ID";
    private static final String MAP_CLICK_MARKER_ICON_ID = "MAP_CLICK_MARKER_ICON_ID";
    private static final String MAP_CLICK_MARKER_LAYER_ID = "MAP_CLICK_MARKER_LAYER_ID";
    private static final String[] contourColors = new String[]{"80f442", "41f4f1", "bc404c"};
    public Services services;
    public Util util;
    public MapboxMap mbMap;
    public MapView mapView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LogNotTimber")
    public void renderUserComponents(Activity activity)
    {

        /* Set Services Initialised */
        services = (Services) new Services(TAG, activity);

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
        Log.d(TAG, "renderUserComponents: " + Arrays.toString(services.getCurrentUserLocation()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Set Layout Content View  */
        this.setContentView(R.layout.activity_main);

        /* Render User Components */
        Application.this.renderUserComponents(Application.this);
    }

    public void renderMapView(Activity activity, Bundle bundle)
    {
        Mapbox.getInstance(activity.getBaseContext(), TOKEN);

        /* Render Layout Components */
        this.mapView = (MapView) activity.findViewById(R.id.mapView);
        this.mapView.onCreate(bundle);
        this.mapView.getMapAsync(new OnMapReadyCallback()
        {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onMapReady(MapboxMap mapboxMap)
            {
                mapboxMap.setStyle(
                        new Style.Builder()
                                .withImage(MAP_CLICK_MARKER_ICON_ID, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                                        getResources().getDrawable(R.drawable.icon))))
                                .withSource(new GeoJsonSource(MAP_CLICK_SOURCE_ID))
                                .withSource(new GeoJsonSource(ISOCHRONE_RESPONSE_GEOJSON_SOURCE_ID))
                                .withLayer(new SymbolLayer(MAP_CLICK_MARKER_LAYER_ID, MAP_CLICK_SOURCE_ID).withProperties(
                                        iconImage(MAP_CLICK_MARKER_ICON_ID),
                                        iconIgnorePlacement(true),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f, - 4f})
                                )), new Style.OnStyleLoaded()
                        {
                            @Override
                            public void onStyleLoaded(@NonNull Style style)
                            {
                                Application.this.mbMap = mapboxMap;
                                initFillLayer(style);
                                initLineLayer(style);
                            }
                        }
                );

            }
        });
    }

    @Override
    public boolean onMapClick(@NonNull LatLng latLng)
    {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }

    public void initFillLayer(@NonNull Style style)
    {
        FillLayer isochroneFillLayer = new FillLayer(ISOCHRONE_FILL_LAYER, ISOCHRONE_RESPONSE_GEOJSON_SOURCE_ID);

        isochroneFillLayer.setProperties(
                fillColor(get("color")),
                fillOpacity(get("fillOpacity")));
        isochroneFillLayer.setFilter(eq(geometryType(), literal("Polygon")));

        style.addLayerBelow(isochroneFillLayer, MAP_CLICK_MARKER_LAYER_ID);
    }

    public void initLineLayer(@NonNull Style style)
    {
        LineLayer isochroneLineLayer = new LineLayer(ISOCHRONE_LINE_LAYER, ISOCHRONE_RESPONSE_GEOJSON_SOURCE_ID);

        isochroneLineLayer.setProperties(
                lineColor(get("color")),
                lineWidth(5f),
                lineOpacity(.8f));
        isochroneLineLayer.setFilter(eq(geometryType(), literal("LineString")));

        style.addLayerBelow(isochroneLineLayer, MAP_CLICK_MARKER_LAYER_ID);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.mapView.onStart();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        this.mapView.onResume();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.mapView.onStop();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        this.mapView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mbMap != null) this.mbMap.removeOnMapClickListener(this);
        this.mapView.onDestroy();
    }
}