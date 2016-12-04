package tenoz.lab.sightbus.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import tenoz.lab.sightbus.MapActivity;
import tenoz.lab.sightbus.StopsActivity;

/**
 * Created by AppleCaspar on 2016/10/21.
 */

public class MapMarker implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private MapLocationListeners listener;
    private GoogleMap map;
    private MapActivity activity;
    private Context context;
    public Map<String, String> markers = new HashMap<String, String>();
    public Map<String, Integer> ids = new HashMap<String, Integer>();

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("Marker.title", marker.getTitle());
        Log.i("Marker.snippet", marker.getSnippet());
        return false;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setListener(MapLocationListeners listener) {
        this.listener = listener;
    }
    public void setActivity(Activity activity) {
        this.activity = (MapActivity)activity;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            StopMarker stop = this.activity.getNearbyStops().get(marker.getId());
            Intent stopActivity = new Intent(this.activity, StopsActivity.class);
            stopActivity.putExtra("Title@Actionbar", stop.name);
            stopActivity.putExtra("StopID", stop.stopid);

            Log.i("Marker", marker.getId() + ", Clicked: " + markers.get(marker.getId()));
            Log.i("ID:" ,   marker.getId());
            Log.i("Name:", stop.name);
            Log.i("SID:",   stop.stopid+"");
            this.activity.startActivity(stopActivity);
        } catch ( NullPointerException e ){

        }
    }
}










