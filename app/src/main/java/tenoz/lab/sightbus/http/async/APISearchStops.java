package tenoz.lab.sightbus.http.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tenoz.lab.sightbus.R;
import tenoz.lab.sightbus.SearchStops;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class APISearchStops extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    SearchStops activity = null;
    private List<Map<String, String>> routesList;

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (SearchStops) activity[0];
        try {
            EditText query = (EditText)this.activity.findViewById(R.id.SearchStops_SearchBar);
            URL url = new URL("http://sightbus.tenoz.asia/stops/search?query=" + URLEncoder.encode(this.activity.getQuery(), "utf-8"));
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
        this.activity.setRoutesList(routesList);
        this.activity.jobsDone();
        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.SearchStops_ProgressBar );
        bar.setVisibility(View.INVISIBLE);
    }


    private void parser(){
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray routes = jsonRootObject.optJSONArray("results");
            for( int i = 0; i < routes.length(); i++){
                Map<String, String> datum = new HashMap<String, String>(2);
                JSONObject route = routes.getJSONObject(i);
                datum.put("stop",  route.getString("stop"));
                datum.put("routes",  route.getString("routes"));
                list.add( datum );
            }
            this.routesList = list;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            this.activity.finish();
        }
    }
}
