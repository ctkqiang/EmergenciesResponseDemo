package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class Util
{
    public void showToast(Context context, String message)
    {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public String[] getLocationArray(Location location)
    {
        return new String[]{
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude())
        };
    }

    public void requestSecurityPermission(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED)
            {
                this.showToast(activity.getApplicationContext(), "Accept the permission in order for the app to work!");

                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{
                                Manifest.permission.READ_PHONE_STATE
                        },
                        0x1
                );
            }
        }
    }
}
