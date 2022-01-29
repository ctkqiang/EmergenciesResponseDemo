package my.com.johnmelody.emergenciesresponsedemo.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetUser
{
    String endpoint = "https://opensourceprojects-44592-default-rtdb.firebaseio.com/user.json";

    @GET("user.json")
    Call<String> getUser();
}
