package tenoz.lab.sightbus.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by AppleCaspar on 2016/11/16.
 */

public class StopMarker {
    public String stopid;
    public Boolean goBack;
    public String name;
    public LatLng latlng;
    public Marker marker;
    public String routes;

    public StopMarker( String id, Boolean back, String _name,  String routes, LatLng _latlng ){
        this.stopid = id;
        this.goBack = back;
        this.name = _name;
        this.latlng = _latlng;
        this.routes = routes;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public boolean isMatch(String pastQuery) {
        if( stopid.contains(pastQuery) ){
            return true;
        } else if ( name.contains(pastQuery) ){
            return true;
        } else if  ( routes.contains(pastQuery) ){
            return true;
        }
        return false;
    }
}