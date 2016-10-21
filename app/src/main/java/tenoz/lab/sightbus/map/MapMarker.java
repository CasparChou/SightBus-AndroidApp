package tenoz.lab.sightbus.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import tenoz.lab.sightbus.StopsActivity;

/**
 * Created by AppleCaspar on 2016/10/21.
 */

public class MapMarker implements GoogleMap.OnMarkerClickListener {

    private MapLocationListeners listener;
    private GoogleMap map;
    private Activity activity;
    private Context context;
    public Map<String, String> markers = new HashMap<String, String>();

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            Log.i("Marker", marker.getId() + ", Clicked: " + markers.get(marker.getId()));
            Intent stop = new Intent(this.activity, StopsActivity.class);
            stop.putExtra("Title@Actionbar", markers.get(marker.getId()));
            this.activity.startActivity(stop);
        } catch ( NullPointerException e ){

        }
        return false;
    }


    public void setContext(Context context) {
        this.context = context;
    }
    public void setListener(MapLocationListeners listener) {
        this.listener = listener;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}










