package my.com.johnmelody.emergenciesresponsedemo.Model;

public class EmergencyItem
{
    private String datetime;
    private String phone;
    private String latitude;
    private String longitude;

    public EmergencyItem(String datetime, String phone, String latitude, String longitude)
    {
        this.datetime = datetime;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDatetime()
    {
        return datetime;
    }

    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }
}
