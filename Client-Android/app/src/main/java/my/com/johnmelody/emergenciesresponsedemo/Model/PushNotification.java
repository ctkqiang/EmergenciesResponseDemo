package my.com.johnmelody.emergenciesresponsedemo.Model;

import org.json.JSONObject;

public class PushNotification
{
   public JSONObject body;

    public PushNotification(JSONObject body)
    {
        this.body = body;
    }

    public JSONObject getBody()
    {
        return body;
    }

    public void setBody(JSONObject body)
    {
        this.body = body;
    }
}