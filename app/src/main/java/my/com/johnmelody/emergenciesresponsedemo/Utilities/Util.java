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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.an.deviceinfo.device.model.Battery;
import com.an.deviceinfo.device.model.Device;
import com.an.deviceinfo.device.model.Network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import my.com.johnmelody.emergenciesresponsedemo.Activities.SplashActivity;
import my.com.johnmelody.emergenciesresponsedemo.Application;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
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
                                READ_PHONE_STATE
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
}
