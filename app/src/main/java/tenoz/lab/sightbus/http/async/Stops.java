package tenoz.lab.sightbus.http.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import tenoz.lab.sightbus.MapActivity;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class Stops extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    MapActivity activity = null;

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (MapActivity) activity[0];
        try {
            URL url = new URL(
                    String.format(
                            "http://sightbus.tenoz.asia/stops/nearby?lat0=%f&lng0=%f&lat1=%f&lng1=%f",
                            this.activity.bounds[0].latitude,
                            this.activity.bounds[0].longitude,
                            this.activity.bounds[1].latitude,
                            this.activity.bounds[1].longitude
                    )
            );
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            int data = in.read();
            Log.i("Network", "InputStream");
            while( data != -1 ){
                fetch += String.valueOf((char)data);
                data = in.read();
            }
            return fetch;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.i("PostExec", fetch);
//        parser();
        this.activity.setNearbyStops( parser() );
//        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.ProgressBar );
//        bar.setVisibility(View.INVISIBLE);
    }


    private Map<String,LatLng> parser(){
        Map<String,LatLng> stopsList = new HashMap<String, LatLng>();
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray stops = jsonRootObject.optJSONArray("stops");
            for( int i = 0; i < stops.length(); i++){

                Log.i("Stops", stops.getJSONObject(i).toString());
                stopsList.put(stops.getJSONObject(i).getString("name"), new LatLng(stops.getJSONObject(i).getDouble("lat"),stops.getJSONObject(i).getDouble("lng")));
            }
            return  stopsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
