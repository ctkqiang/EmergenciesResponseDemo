package my.com.johnmelody.emergenciesresponsedemo.Interfaces;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.Model.PushNotification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi
{
    @Headers({
            "Authorization: key=" + "Basic MjQwYmUwZDAtNGU0NC00MDY0LTgyOTEtODE5NDA3MzFiYTk3",
            "Content-Type:" + "application/json; charset=utf-8"
    })
    @POST("/notifications")
    Call<PushNotification> broadcastPushNotification(@Body PushNotification data);
}
