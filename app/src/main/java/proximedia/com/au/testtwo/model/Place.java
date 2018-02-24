package proximedia.com.au.testtwo.model;

import android.location.Location;

/**
 * Created by pc on 23/02/2018.
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Place
{
    private String id;
    private String name;
    private LatLng location;
    private HashMap<String, String> fromcentral;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public LatLng getLocation ()
    {
        return location;
    }

    public void setLocation (LatLng location)
    {
        this.location = location;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public HashMap<String, String> getFromcentral ()
    {
        return fromcentral;
    }

    public void setFromcentral (HashMap<String, String> fromcentral)
    {
        this.fromcentral = fromcentral;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", location = "+location+", name = "+name+", fromcentral = "+fromcentral+"]";
    }
}
