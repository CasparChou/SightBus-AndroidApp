package tenoz.lab.sightbus.http.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tenoz.lab.sightbus.EstimateRoutesActivity;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class APIEstimateRoute extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    EstimateRoutesActivity activity = null;
    List<Map<String,String>> routesGoList = new ArrayList<Map<String, String>>();
    List<Map<String,String>> routesBackList = new ArrayList<Map<String, String>>();

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (EstimateRoutesActivity) activity[0];
        try {
            URL url = new URL("http://sightbus.tenoz.asia/routes/estimate?route="+this.activity.getRouteId().toString());
            Log.i("GET", url.toString());
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
        parser();
        this.activity.setRoutesList( routesGoList, routesBackList );
//        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.ProgressBar );
//        bar.setVisibility(View.INVISIBLE);
    }


    private void parser(){
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray routes = jsonRootObject.optJSONArray("stops");
            for( int i = 0; i < routes.length(); i++){
                Map<String, String> datum = new HashMap<String, String>(2);
                JSONObject stop = routes.getJSONObject(i);
                datum.put("title",  stop.getString("name"));
                int time = stop.getInt("time")/60;
                datum.put("estimate",  (time < 10?" "+time:""+time) );
                if( stop.getInt("goBack") == 0){
                    routesGoList.add( datum );
                } else if ( stop.getInt("goBack") == 1 ){
                    routesBackList.add(datum);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
