package tenoz.lab.sightbus.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.openlocationcode.OpenLocationCode;

import java.util.List;

import tenoz.lab.sightbus.MapActivity;

/**
 * Created by AppleCaspar on 2016/10/21.
 */

public class MapHelpers {

    private MapLocationListeners listener;
    private GoogleMap map;
    private LocationManager locationManager;
    private MapActivity activity;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMap( GoogleMap map ){
        this.map = map;
    }

    public void setListener(MapLocationListeners listener) {
        this.listener = listener;
    }

    public void moveCamera(Location location){
        moveCamera( loc2Lat(location) );
    }

    public void moveCamera(LatLng latLng){

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        map.animateCamera(cameraUpdate);
        if( !checkPermission() ) return;
        locationManager.removeUpdates(this.listener);
        this.activity.reloadStops();

    }
    public Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        if( !checkPermission() ) return null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    public boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            return true;
        }
        return false;
    }

    public void updateLocation() {
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        if( !checkPermission() ) return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this.listener);
    }

    public LatLng loc2Lat( Location loc ){
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    public LatLng plusCode2Lat(String str){
        OpenLocationCode code = new OpenLocationCode( str );
        return new LatLng(code.decode().getCenterLatitude(), code.decode().getCenterLongitude());
    }
    public LatLng plusCode2Lat(OpenLocationCode code){
        return new LatLng(code.decode().getCenterLatitude(), code.decode().getCenterLongitude());
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this.activity,
            new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.INTERNET
            },
            8);
    }

    public void setActivity(MapActivity activity) {
        this.activity = activity;
    }

    public MapActivity getActivity() {
        return activity;
    }
}
