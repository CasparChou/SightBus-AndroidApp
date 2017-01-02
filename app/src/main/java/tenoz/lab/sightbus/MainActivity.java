package tenoz.lab.sightbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import tenoz.lab.sightbus.data.PlanList;
import tenoz.lab.sightbus.http.Api;
import tenoz.lab.sightbus.routeplanner.RoutePlannerPlanningPage;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private int backPress = 0;
    private String location = "";

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
//        ((TextView)findViewById(R.id.main_location)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            if(  "您沒有授權 SightBus 定位服務".equals(((TextView)findViewById(R.id.main_location)).getText()) ){
//                requestPermission();
//            }
//            }
//        });
        getCityName(getLastKnownLocation());
    }
    private void updateNearestBtn(){
        getCityName(getLastKnownLocation());
        updateLocation();
        Api.getNearest(MainActivity.this);
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
        getCityName(bestLocation);
        return bestLocation;
    }

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ((TextView)findViewById(R.id.main_location)).setText("您沒有授權 SightBus 定位服務");
            return false;//requestPermission();
        } else {
            return true;
        }
    }

    public void openMap(View view){
        getCityName(getLastKnownLocation());
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
    public void openNearestStop(View view){

        Intent stopsEstimate = new Intent(getApplicationContext(), EstimateStopsActivity.class);
        stopsEstimate.putExtra("Stop Name", ((TextView)findViewById(R.id.main_nearest)).getText().toString());
        startActivity(stopsEstimate);

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
            public void onLocationChanged(final Location location) {
                // On Local. change
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

    @Override
    public void onBackPressed() {
        if(backPress == 0){
            Toast.makeText(getApplicationContext(),"在按一次返回結束", Toast.LENGTH_SHORT).show();
            backPress ++;
            Log.e("Now", "RUn");
            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        backPress = 0;
                    }
                }, 1000);
        } else if ( backPress > 0 ){
            this.finish();
        }
    }
    public void getCityName(View view){
        getCityName(getLastKnownLocation());
    }
    public void getCityName(final Location location) {
        new AsyncTask<Void, Integer, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... arg0) {
                Geocoder coder = new Geocoder(getApplicationContext(), Locale.TAIWAN);
                List<Address> results = null;
                try {
                    results = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    // nothing
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<Address> results) {
                if(results.size()>0){
                    String locality = results.get(0).getLocality();
                    ((TextView)findViewById(R.id.main_location)).setText(locality);
                }
            }
        }.execute();
    }


    public class APIWeather extends AsyncTask<Void,Integer,String> {

        String fetch = "";
        String endpoint = "http://sightbus.tenoz.asia/stops/weather?city=%s";
        RoutePlannerPlanningPage activity = null;
        private ArrayList<PlanList> planLists = new ArrayList<PlanList>();

        @Override
        protected String doInBackground(Void... unused) {
            if( "".equals(location) ){
                cancel(true);
                return "";
            }
            try {
                URL url = new URL(String.format(endpoint, URLEncoder.encode(location, "utf-8")));
                Log.i("Network", "connect");
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                int data = in.read();
                Log.i("Network", "read");

                while( data != -1 ){
                    fetch += String.valueOf((char)data);
                    data = in.read();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return fetch;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
            Log.i("PostExec", fetch);
            try {
                parserWeather();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void parserWeather() throws JSONException {
            JSONObject data = new JSONObject(fetch);
            ((TextView)findViewById(R.id.Main_Temp)).setText(data.optJSONObject("results").optString("temp"));

        }
    }
}
