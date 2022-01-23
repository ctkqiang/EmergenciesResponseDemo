package my.com.johnmelody.emergenciesresponsedemo.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

import my.com.johnmelody.emergenciesresponsedemo.Application;
import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.R;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.AuthenticationService;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.DatabaseHandler;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.Util;

public class AuthenticationActivity extends AppCompatActivity
{
    private static final String TAG = ConstantsValues.TAG_NAME;
    private Util util;
    private EditText emailField, phoneField, passwordField;
    private AuthenticationService authenticationService;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void renderUserComponents(Activity activity)
    {
        /* Set Layout Content View  */
        activity.setContentView(R.layout.activity_authentication);

        /* Services Declarations */
        this.util = (Util) new Util();
        this.authenticationService = (AuthenticationService) new AuthenticationService(TAG, this);

        /* Set Title for action Bar & set title colour */
        Objects.requireNonNull(this.getSupportActionBar()).setElevation(0x0);
        Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                Html.fromHtml("<font color='#000000'> </font>")
        );

        /* Set Status bar colour to white & Set Status bar icon to black */
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        /* Render Layout Components */
        this.emailField = (EditText) this.findViewById(R.id.useremail);
        this.phoneField = (EditText) this.findViewById(R.id.userphone);
        this.passwordField = (EditText) this.findViewById(R.id.userpassword);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Render User Components */
        this.renderUserComponents(AuthenticationActivity.this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onAuthentication(View view)
    {
        String email = this.emailField.getText().toString();
        String phone = this.phoneField.getText().toString();
        String password = this.passwordField.getText().toString();

        switch (view.getId())
        {
            case R.id.login:
                if (this.util.isFieldNull(email) || this.util.isFieldNull(password))
                {
                    emailField.setError(this.getString(R.string.email_needed));
                    phoneField.setError(this.getString(R.string.phone_number_required));
                    passwordField.setError(this.getString(R.string.password_needed));
                }
                else
                {
                    if (!this.authenticationService.isLoggedIn())
                    {
                        this.authenticationService.loginUser(email, password);
                        this.util.navigate(AuthenticationActivity.this, Application.class);
                    }
                    else
                    {
                        this.util.navigate(AuthenticationActivity.this, Application.class);
                    }
                }
                Log.d(TAG, "onAuthentication: " + email);

            case R.id.register:
                if (this.util.isFieldNull(email) || this.util.isFieldNull(password))
                {
                    emailField.setError(this.getString(R.string.email_needed));
                    phoneField.setError(this.getString(R.string.phone_number_required));
                    passwordField.setError(this.getString(R.string.password_needed));
                }
                else
                {
                    if (!this.authenticationService.isLoggedIn())
                    {
                        this.authenticationService.registerUser(email, phone, password);
                        this.util.navigate(AuthenticationActivity.this, Application.class);
                    }
                    else
                    {
                        this.util.navigate(AuthenticationActivity.this, Application.class);
                    }
                }
                Log.d(TAG, "onAuthentication: " + email);
            default:
                break;
        }
    }
}