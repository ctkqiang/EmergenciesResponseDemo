package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.an.deviceinfo.device.model.Battery;
import com.an.deviceinfo.device.model.Device;
import com.an.deviceinfo.device.model.Network;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;


public class Util
{
    public void showToast(Context context, String message)
    {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);

        if (message == "kill")
        {
            toast.cancel();
        }
        else
        {
            toast.show();
        }
    }

    public String getCurrentDateTime()
    {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormats = new SimpleDateFormat("HH:mm:ss");

        return simpleDateFormats.format(new Date());
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED)
            {
                this.showToast(activity.getApplicationContext(), "Accept the permission in order "
                                                                 + "for the app to work!");

                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{
                                READ_PHONE_STATE,
                                },
                        0x1
                );
            }
        }
    }

    @SuppressLint({"InlinedApi", "HardwareIds", "DefaultLocale"})
    public ArrayList<String> getPhoneInfo(Activity activity)
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<String> networkList = new ArrayList<String>();

        if (ActivityCompat.checkSelfPermission(activity, READ_SMS)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, READ_PHONE_NUMBERS)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                ActivityCompat.requestPermissions(
                        activity, new String[]{
                                READ_SMS,
                                READ_PHONE_NUMBERS,
                                READ_PHONE_STATE
                        },
                        100
                );
            }
        }


        Battery battery = (Battery) new Battery(activity.getApplicationContext());
        Device device = (Device) new Device(activity.getApplicationContext());
        Network network = (Network) new Network(activity.getApplicationContext());

        networkList.add(String.format("IMSI: %s", network.getIMSI()));
        networkList.add(String.format("Phone Number: %s", network.getPhoneNumber()));
        networkList.add(String.format("SIM Serial: %s", network.getsIMSerial()));
        networkList.add(String.format("SIM Operator: %s", network.getOperator()));
        networkList.add(String.format("Network Type: %s", network.getNetworkType()));
        networkList.add(String.format("Is Wifi On? %s", network.isWifiEnabled()));
        networkList.add(String.format("Network Availability:  %s", network.isNetworkAvailable()));

        arrayList.add(String.format("Battery Percent: %d", battery.getBatteryPercent()));
        arrayList.add(String.format("Battery Temperature: %s", battery.getBatteryTemperature()));
        arrayList.add(String.format("Hardware detail: %s", device.getHardware()));
        arrayList.add(String.format("Devices detail: %s", device.getDevice()));
        arrayList.add(String.format("Manufacturer detail: %s", device.getManufacturer()));
        arrayList.add(String.format("Model detail: %s", device.getModel()));
        arrayList.add(String.format("Networks: {%s}", networkList));

        return arrayList;
    }

    public Boolean isFieldNull(String data)
    {
        if (data.isEmpty() || data.length() < 5)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void navigate(Activity activity, Class<?> className)
    {
        Intent intent = (Intent) new Intent(activity, className);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * @param activity
     *
     * @deprecated
     */
    @SuppressLint("InlinedApi")
    public void requestPermission(Activity activity)
    {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)

        {
            this.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_PHONE_NUMBERS,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.FOREGROUND_SERVICE,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }
            );
        }

        this.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_PHONE_NUMBERS,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );

    }

    /**
     * @param activity
     * @param permission
     *
     * @deprecated
     */
    public void requestPermissions(Activity activity, String[] permission)
    {
        ActivityCompat.requestPermissions(activity, permission, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void requestPermissionsAtOnce(Activity activity)
    {
        Dexter.withActivity(activity).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                READ_SMS,
                READ_PHONE_STATE,
                READ_PHONE_NUMBERS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener()
        {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report)
            {
                Log.d(ConstantsValues.TAG_NAME, "onPermissionsChecked: " + report);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                           PermissionToken token)
            {
                token.continuePermissionRequest();
            }
        }).onSameThread().check();
    }
}