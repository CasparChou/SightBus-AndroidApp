package tenoz.lab.sightbus;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import tenoz.lab.sightbus.map.MapHelpers;
import tenoz.lab.sightbus.map.MapLocationListeners;

/**
 * Created by AppleCaspar on 2016/10/19.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LocationManager locationManager;
    private GoogleMap map;
    private MapHelpers mapUtils = new MapHelpers();
    private MapLocationListeners mapListeners = new MapLocationListeners();

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
        /*
                 *  Request Permissions
                 */
        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 8
        );

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        mapListeners.setMap( map );
        mapUtils.setMap( map );
        mapUtils.updateLocation();

        LatLng curr_position = mapUtils.loc2Lat( mapUtils.getLastKnownLocation() );

        try {
            mapUtils.moveCamera(curr_position);
        } catch ( NullPointerException e ){
            new AlertDialog.Builder(MapActivity.this)
                    .setTitle("錯誤")
                    .setMessage("App 運作發生錯誤，請嘗試重啟或回報錯誤")
                    .setPositiveButton("好的", null)
                    .show();
        }


    }

}
