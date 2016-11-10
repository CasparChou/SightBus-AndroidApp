package tenoz.lab.sightbus.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;

/**
 * Created by AppleCaspar on 2016/10/21.
 */

public class MapLocationListeners implements LocationListener, OnCameraMoveStartedListener, OnCameraMoveCanceledListener, OnCameraIdleListener  {

    private GoogleMap map;
    private MapHelpers mapUtil;
    private boolean idle = true;
    private long startIdleTimer = 0L;

    @Override
    public void onLocationChanged(Location location) {
        if(this.map != null){
            this.mapUtil.moveCamera(location);
        }
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

    @Override
    public void onCameraMoveCanceled() {
        this.idle = true;
    }

    @Override
    public void onCameraIdle() {
        if( this.idle == false ){
            startIdleTimer = System.currentTimeMillis();
//            new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        if( System.currentTimeMillis() - startIdleTimer >= 1500 && startIdleTimer > 0)
                            updateStops();
//                        startIdleTimer = 0;
//                    }
//                },
//            1500);

        }
        this.idle = true;
    }

    private void updateStops() {
        this.mapUtil.getActivity().reloadStops();
    }

    @Override
    public void onCameraMoveStarted(int i) {
        this.idle = false;
    }
}
