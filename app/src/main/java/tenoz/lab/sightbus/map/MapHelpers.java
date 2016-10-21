package tenoz.lab.sightbus.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.openlocationcode.OpenLocationCode;

import java.util.List;

/**
 * Created by AppleCaspar on 2016/10/21.
 */

public class MapHelpers {

    private MapLocationListeners listener;
    private Context context;
    private GoogleMap map;
    private LocationManager locationManager;

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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveCamera( latLng );
    }

    public void moveCamera(LatLng latLng){

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        map.animateCamera(cameraUpdate);
        if( !checkPermission() ) return;
        locationManager.removeUpdates(this.listener);
        throw new NullPointerException();
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
            Toast.makeText(this.context, "您需要授權定位服務", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void updateLocation() {
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        if( checkPermission() ){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this.listener);
        }
    }

    public LatLng loc2Lat( Location loc ){
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    public LatLng plusCode2Lat(OpenLocationCode code){
        return new LatLng(code.decode().getCenterLatitude(), code.decode().getCenterLongitude());
    }
}
