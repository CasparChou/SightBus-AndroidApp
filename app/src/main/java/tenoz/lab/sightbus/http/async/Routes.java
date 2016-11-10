package tenoz.lab.sightbus.http.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

import tenoz.lab.sightbus.R;
import tenoz.lab.sightbus.StopsActivity;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class Routes extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    StopsActivity activity = null;

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (StopsActivity) activity[0];
        try {
            URL url = new URL("http://sightbus.tenoz.asia/stops/");
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
        this.activity.setRoutesList( parser() );
        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.ProgressBar );
        bar.setVisibility(View.INVISIBLE);
    }


    private List<Map<String,String>> parser(){
        List<Map<String,String>> routesList = new ArrayList<Map<String, String>>();
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray routes = jsonRootObject.optJSONArray("route");
            for( int i = 0; i < routes.length(); i++){
                Map<String, String> datum = new HashMap<String, String>(2);
                datum.put("title",  routes.getString(i));
                datum.put("date",  System.currentTimeMillis() / 1000L +"");
                routesList.add( datum );
            }
            return  routesList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
