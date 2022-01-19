package my.com.johnmelody.emergenciesresponsedemo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;

public class Application extends AppCompatActivity
{
    public static final String TAG = ConstantsValues.TAG_NAME;

    public void renderUserComponents(Activity activity)
    {
        activity.setContentView(R.layout.activity_main);

        /* Set Action Bar To White Colour */
        Objects.requireNonNull(this.getSupportActionBar()).setBackgroundDrawable(
                new ColorDrawable(
                        this.getResources().getColor(R.color.white)
                )
        );

        /* Set Title for action Bar & set title colour */
        this.getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>" + ConstantsValues.APP_NAME + "</font>"));
        this.getSupportActionBar().setElevation(0x0);

        Log.d(TAG, "Application:: Rendered User Components ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Render User Components */
        Application.this.renderUserComponents(Application.this);
    }
}