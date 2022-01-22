package my.com.johnmelody.emergenciesresponsedemo.Constants;

import android.content.Context;

import my.com.johnmelody.emergenciesresponsedemo.R;

public class ConstantsValues
{
    public static final String APP_NAME = "Emergencies Response Demo";
    public static final String TAG_NAME = "EmergenciesResponse";
    public static final String BASE_URL = "https://fcm.googleapis.com";
    public static final String CONTENT_TYPE = "application/json";
    public static final int SPLASH_TIMER = 0xbb8;

    public static String MAPBOX_TOKEN(Context context)
    {
        return context.getResources().getString(R.string.mapbox_token);
    }

    public static String FIREBASE_SERVER_KEY(Context context)
    {
        return context.getResources().getString(R.string.firebase_server_key);
    }
}
