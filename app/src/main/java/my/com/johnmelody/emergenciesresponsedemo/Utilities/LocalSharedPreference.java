package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocalSharedPreference
{
    private String TAG;
    private Activity activity;


    public LocalSharedPreference(String TAG, Activity activity)
    {
        this.TAG = TAG;
        this.activity = activity;
    }

    public LocalSharedPreference()
    {
    }

    @SuppressLint("WrongConstant")
    public SharedPreferences sharedPreferences()
    {
        return PreferenceManager.getDefaultSharedPreferences(this.activity);
    }

    public SharedPreferences.Editor editor()
    {
        return this.sharedPreferences().edit();
    }

    /**
     * @deprecated
     */
    public void insertUserPhoneNumber(String phoneNumber)
    {
        if (getUserPhoneNumber().isEmpty())
        {
            this.editor().putString("phone_number", phoneNumber);
            this.editor().commit();
        }
        else
        {
            Log.d(TAG, String.format(
                    "insertUserPhoneNumber: Phone number %s already exist",
                    phoneNumber
            ));
        }
    }

    public String getUserPhoneNumber()
    {
        return this.sharedPreferences().getString("phone_number", null);
    }
}
