package my.com.johnmelody.emergenciesresponsedemo.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;

import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Application;
import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.R;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.AuthenticationService;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.DatabaseHandler;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Services;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Util;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity
{
    private static final String TAG = ConstantsValues.TAG_NAME;
    public DatabaseHandler databaseHandler;
    public Handler handler;
    private Intent intent;
    private Util util;
    private Services services;
    private AuthenticationService authenticationService;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);

        this.util = (Util) new Util();
        this.services = (Services) new Services(TAG, this);
        this.authenticationService = (AuthenticationService) new AuthenticationService(TAG, this);
        this.services.requestSecurityPermission(this);
        this.util.requestPermissionsAtOnce(SplashActivity.this);

        /* Set Title for action Bar & set title colour */
        Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                Html.fromHtml("<font color='#000000'> </font>")
        );
        this.getSupportActionBar().setElevation(0x0);

        /* Set Status bar colour to white & Set Status bar icon to black */
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        this.databaseHandler = (DatabaseHandler) new DatabaseHandler(this);
        this.handler = (Handler) new Handler();
        this.handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (SplashActivity.this.authenticationService.isLoggedIn())
                {
                    SplashActivity.this.util.navigate(
                            SplashActivity.this,
                            Application.class
                    );

                    Log.d(TAG, "Navigate to application");
                }
                else
                {
                    SplashActivity.this.util.navigate(
                            SplashActivity.this,
                            AuthenticationActivity.class
                    );

                    Log.d(TAG, "Navigate to Authentication");
                }
            }
        }, ConstantsValues.SPLASH_TIMER);
    }
}