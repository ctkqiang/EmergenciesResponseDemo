package my.com.johnmelody.emergenciesresponsedemo.Model;

public class LocationItem
{
    private double longitude;
    private double latitude;

    public LocationItem(double longitude, double latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
}
