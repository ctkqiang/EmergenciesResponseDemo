package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;

public class AuthenticationService extends Util
{
    public String TAG = ConstantsValues.TAG_NAME;
    public Activity activity;
    public DatabaseHandler databaseHandler;

    public DatabaseService databaseService()
    {
        return new DatabaseService(this.activity, this.TAG);
    }

    public AuthenticationService(String TAG, Activity activity)
    {
        this.TAG = TAG;
        this.activity = activity;
    }

    public FirebaseAuth firebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser firebaseUser()
    {
        return this.firebaseAuth().getCurrentUser();
    }

    public String getCurrentUser()
    {
        return this.firebaseAuth().getCurrentUser().getEmail();
    }

    public boolean isLoggedIn()
    {
        return this.firebaseUser() != null;
    }

    public void registerUser(String email, String phone, String password)
    {
        this.firebaseAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.activity, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        databaseService().writeUserDetails(email, phone, password, 0, 0, 0);

                        showToast(
                                activity.getApplicationContext(),
                                String.format("%s registered", email)
                        );

                        Log.d(TAG, "onComplete: ");

                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                databaseHandler = new DatabaseHandler(activity);

                if (databaseHandler.getType() == 0)
                {
                    databaseService().writeUserDetails(email, phone, password, 0, 0, 0);
                }

                showToast(
                        activity.getApplicationContext(),
                        String.format("Failed To register %s ", email)
                );
            }
        });
    }

    public void loginUser(String email, String password)
    {
        this.firebaseAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        showToast(
                                activity.getApplicationContext(),
                                String.format("%s, Welcome Back", email)
                        );

                        Log.d(TAG, "onComplete: ");

                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                /* Do Nothing */
            }
        });
    }
}
