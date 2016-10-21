package tenoz.lab.sightbus.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by AppleCaspar on 2016/10/21.
 */

public class MapLocationListeners implements LocationListener {

    private GoogleMap map;
    private MapHelpers mapUtil;

    @Override
    public void onLocationChanged(Location location) {
        if(this.map != null)
            this.mapUtil.moveCamera(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setMapUtil( MapHelpers util ){
        this.mapUtil = util;
    }

    public void setMap( GoogleMap map ){
        this.map = map;
    }
}
