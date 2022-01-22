package my.com.johnmelody.emergenciesresponsedemo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.R;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.DatabaseHandler;

public class AuthenticationActivity extends AppCompatActivity
{
    private static final String TAG = ConstantsValues.TAG_NAME;
    private EditText email, password;

    public void renderUserComponents(Activity activity)
    {
        activity.setContentView(R.layout.activity_authentication);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Render User Components */
        this.renderUserComponents(AuthenticationActivity.this);
    }
}