package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.content.Context;
import android.widget.Toast;

public class Util
{
    public static void showToast(Context context , String message)
    {
       Toast toast =  Toast.makeText(context, message, Toast.LENGTH_SHORT);
       toast.show();
    }
}
