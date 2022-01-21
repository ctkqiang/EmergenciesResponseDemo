package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

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
}
