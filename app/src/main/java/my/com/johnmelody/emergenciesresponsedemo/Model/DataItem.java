package my.com.johnmelody.emergenciesresponsedemo.Model;

public class DataItem
{
    public String email;
    public String phoneNumber;
    public String password;
    public double longitude;
    public double latitude;
    public int type;

    public DataItem()
    {
    }

    public DataItem(String email, String phoneNumber, String password, double longitude,
                    double latitude, int type)
    {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
