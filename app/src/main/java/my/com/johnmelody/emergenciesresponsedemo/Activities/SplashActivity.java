package my.com.johnmelody.emergenciesresponsedemo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity
{
    private static final String TAG = ConstantsValues.TAG_NAME;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = (Handler) new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

            }
        }, ConstantsValues.SPLASH_TIMER);
    }
}