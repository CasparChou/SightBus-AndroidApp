package tenoz.lab.sightbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Helpers.initialActionbar(this);
        if(checkPermission()){
            updateNearestBtn();
            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    Log.i("Refreshing","Nearest stop refreshing");
                    updateNearestBtn();
                }
            },0,10000);
        }
        ((Button)findViewById(R.id.main_nearest_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(  "您沒有授權 SightBus 定位服務".equals(((Button)findViewById(R.id.main_nearest_btn)).getText()) ){
                    requestPermission();
                }
            }
        });

    }
    private void updateNearestBtn(){
//        updateLocation();
//        Api.getNearest(MainActivity.this);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
            new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            },
            8);
    }

    public Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
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

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ((Button)findViewById(R.id.main_nearest_btn)).setText("您沒有授權 SightBus 定位服務");
            return false;//requestPermission();
        } else {
            return true;
        }
    }

    public void openMap(View view){

        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);

    }

    public void openSearchRoutes(View view){

        Intent intent = new Intent(getApplicationContext(), SearchRoutes.class);
        startActivity(intent);

    }
    public void openSearchStops(View view){

        Intent intent = new Intent(getApplicationContext(), SearchStops.class);
        startActivity(intent);

    }

    public void openRoutePlanning(View view){

        Intent intent = new Intent(getApplicationContext(), RoutePlanner.class);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 8: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateNearestBtn();
                } else {
                    Toast.makeText(getApplicationContext(), "有限的功能使用", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public void updateLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if( !checkPermission() ) return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
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
        },  Looper.getMainLooper());
    }

}
