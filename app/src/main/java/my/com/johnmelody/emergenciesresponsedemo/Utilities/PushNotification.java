package my.com.johnmelody.emergenciesresponsedemo.Utilities;

import my.com.johnmelody.emergenciesresponsedemo.Model.Notification;

public class PushNotification
{
    private Notification notification;
    private String to;

    public PushNotification(Notification notification, String to)
    {
        this.notification = notification;
        this.to = to;
    }

    public Notification getNotification()
    {
        return notification;
    }

    public void setNotification(Notification notification)
    {
        this.notification = notification;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }
}
