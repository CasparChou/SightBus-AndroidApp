package tenoz.lab.sightbus;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import tenoz.lab.sightbus.map.MapHelpers;
import tenoz.lab.sightbus.map.MapLocationListeners;
import tenoz.lab.sightbus.map.MapMarker;

/**
 * Created by AppleCaspar on 2016/10/19.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LocationManager locationManager;
    private GoogleMap map;
    private MapHelpers mapUtils = new MapHelpers();
    private MapLocationListeners mapListeners = new MapLocationListeners();
    private MapMarker mapMarkerListeners = new MapMarker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        /*
                 *  Initial helpers & utils
                 */
        mapUtils.setContext( getApplicationContext() );
        mapListeners.setMapUtil( mapUtils );
        mapUtils.setListener( mapListeners );
        mapUtils.setActivity( this );
        mapMarkerListeners.setActivity( this );
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        mapListeners.setMap( map );
        mapUtils.setMap( map );
        mapMarkerListeners.setMap( map );
        map.setOnMarkerClickListener(mapMarkerListeners);

        if( mapUtils.checkPermission() ){
            mapUtils.updateLocation();
        } else {
            finishActivity(0);
        }


        try {
            LatLng curr_position = mapUtils.loc2Lat( mapUtils.getLastKnownLocation() );
            mapUtils.moveCamera(curr_position);
        } catch ( NullPointerException e ){
            new AlertDialog.Builder(MapActivity.this)
                    .setTitle("錯誤")
                    .setMessage("App 運作發生錯誤，請嘗試重啟或回報錯誤")
                    .setPositiveButton("好的", null)
                    .show();
        }

        pinMarkers();
    }

    private void pinMarkers() {
        addMarker( mapUtils.plusCode2Lat("7QQ32CMM+3WM"), "捷運輔大站" );
    }



    public void addMarker( LatLng latLng, String title ){
        BitmapDescriptor descriptor = (
                BitmapDescriptorFactory.fromResource(
                        getResources().getIdentifier("marker", "drawable", getPackageName())
                )
        );
        String id = this.map.addMarker(
                new MarkerOptions()
                        .position( latLng )
                        .title( title )
                        .icon( descriptor )
        ).getId();

        mapMarkerListeners.markers.put(id,title);
    }
}
