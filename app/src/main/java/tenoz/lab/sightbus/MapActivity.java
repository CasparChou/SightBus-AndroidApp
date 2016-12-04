package tenoz.lab.sightbus;

import android.content.DialogInterface;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tenoz.lab.sightbus.http.Api;
import tenoz.lab.sightbus.map.MapHelpers;
import tenoz.lab.sightbus.map.MapLocationListeners;
import tenoz.lab.sightbus.map.MapMarker;
import tenoz.lab.sightbus.map.StopMarker;

/**
 * Created by AppleCaspar on 2016/10/19.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LocationManager locationManager;
    private GoogleMap map;
    private MapHelpers mapUtils = new MapHelpers();
    private MapLocationListeners mapListeners = new MapLocationListeners();
    private MapMarker mapMarkerListeners = new MapMarker();
    public LatLng[] bounds = new LatLng[2];
    private Map<String, StopMarker> nearbyStops = new HashMap<String, StopMarker>();


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

        overridePendingTransition(R.anim.translate_in, R.anim.translate_out);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        mapListeners.setMap( map );
        mapUtils.setMap( map );
        mapMarkerListeners.setMap( map );
        map.setOnMarkerClickListener(mapMarkerListeners);
        map.setOnInfoWindowClickListener(mapMarkerListeners);
        map.setOnCameraMoveStartedListener(mapListeners);
        map.setOnCameraIdleListener(mapListeners);
        map.setMyLocationEnabled(true);


        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker)   {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.layout_marker_infowindow, null);
                TextView titleHidden = (TextView)  v.findViewById(R.id.marker_info_title_hidden);
                if( marker.getTitle().length()*2 > marker.getSnippet().length()){
                    titleHidden.setText(marker.getTitle());
                } else {
                    titleHidden.setText(marker.getSnippet());
                }
                titleHidden.setTextColor(Color.TRANSPARENT);
                TextView title = (TextView)  v.findViewById(R.id.marker_info_title);
                title.setText(marker.getTitle());
                title.setTextColor(Color.BLACK);

                TextView snippet = (TextView)  v.findViewById(R.id.marker_info_snippet);
                snippet.setText(marker.getSnippet());
                snippet.setTextColor(Color.GRAY);

                return v;
            }

        });

        if( mapUtils.checkPermission() ){
            mapUtils.updateLocation();
        } else {
            finishActivity(0);
        }


        try {
            LatLng curr_position = mapUtils.loc2Lat( mapUtils.getLastKnownLocation() );
            mapUtils.moveCamera(curr_position);
        } catch ( NullPointerException e ){
            e.printStackTrace();
            Log.e("Error", e.getMessage());
            Log.e("Error", e.getLocalizedMessage());
            new AlertDialog.Builder(MapActivity.this)
                .setTitle("錯誤")
                .setMessage("App 運作發生錯誤，請檢查定位是否開啟，嘗試重啟或回報錯誤")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MapActivity.this.finish();
                    }
                })
                .show();
        }

        pinMarkers();
    }

    private void pinMarkers() {
//        addMarker( mapUtils.plusCode2Lat("7QQ32CMM+3WM"), "捷運輔大站" );
    }

    public void reloadStops(){
        VisibleRegion curScreen = map.getProjection().getVisibleRegion();

        bounds[0] = new LatLng( curScreen.farLeft.latitude,     curScreen.farLeft.longitude );
        bounds[1] = new LatLng( curScreen.nearLeft.latitude,    curScreen.nearRight.longitude   );
        Api.getNearbyStops(this);
    }


    public Marker addMarker(LatLng latLng, String title, String stopid, String routes){
        BitmapDescriptor descriptor = (
                BitmapDescriptorFactory.fromResource(
                        getResources().getIdentifier("marker", "drawable", getPackageName())
                )
        );
        return (
            this.map.addMarker(
                new MarkerOptions()
                    .position( latLng )
                    .title( title )
                    .icon( descriptor )
                    .snippet(routes)
            )
        );
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
    }

    public void parserResults(String fetch) {
        if( null == fetch ) {
            return;
        }
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray allStops = jsonRootObject.optJSONArray("stops");
            for( int i = 0; i < allStops.length(); i++){
                JSONObject stop = allStops.getJSONObject(i);
                StopMarker stopMark = new StopMarker(
                        stop.getString("id"),
                        false, //stop.getInt("goBack")
                        stop.getString("name"),
                        stop.getString("routes"),
                        new LatLng(
                                stop.getDouble("lat"),
                                stop.getDouble("lng")
                        )
                );
                Marker mark = addMarker(stopMark.latlng, stopMark.name, stopMark.stopid, stopMark.routes);
                nearbyStops.put(mark.getId(), stopMark );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Map<String, StopMarker> getNearbyStops(){
        return  nearbyStops;
    }
}
