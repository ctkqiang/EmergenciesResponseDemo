package my.com.johnmelody.emergenciesresponsedemo.Interfaces;

import my.com.johnmelody.emergenciesresponsedemo.Constants.ConstantsValues;
import my.com.johnmelody.emergenciesresponsedemo.Utilities.PushNotification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi
{
    @Headers({
            "Authorization: key=" + ConstantsValues.FSKEY,
            "Content-Type:" + ConstantsValues.CONTENT_TYPE
    })
    @POST("fcm/send")
    Call postNotification(@Body PushNotification data);
}
