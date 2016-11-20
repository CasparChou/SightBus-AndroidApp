package tenoz.lab.sightbus.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AppleCaspar on 2016/11/16.
 */

public class StopMarker {
    public String stopid;
    public Boolean goBack;
    public String name;
    public LatLng latlng;
    public StopMarker( String id, Boolean back, String _name, LatLng _latlng ){
        this.stopid = id;
        this.goBack = back;
        this.name = _name;
        this.latlng = _latlng;
    }
}