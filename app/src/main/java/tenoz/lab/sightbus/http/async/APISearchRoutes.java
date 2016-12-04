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
import java.util.Map;

import tenoz.lab.sightbus.R;
import tenoz.lab.sightbus.SearchRoutes;
import tenoz.lab.sightbus.data.RoutesList;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class APISearchRoutes extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    SearchRoutes activity = null;
    private ArrayList<RoutesList> routesList = new ArrayList<RoutesList>();
    private Map<String, String> routesIds = new HashMap<String, String>();

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (SearchRoutes) activity[0];
        try {
            EditText query = (EditText)this.activity.findViewById(R.id.SearchRoutes_SearchBar);
            URL url = new URL("http://sightbus.tenoz.asia/routes/search/?query=" + URLEncoder.encode(this.activity.getQuery(), "utf-8"));
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
        this.activity.setRoutesIds(routesIds);
        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.SearchRoutes_ProgressBar );
        bar.setVisibility(View.INVISIBLE);
    }


    private void parser(){
        ArrayList<RoutesList> list = new ArrayList<RoutesList>();
        Map<String, String> ids = new HashMap<String, String>();
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray routes = jsonRootObject.optJSONArray("routes");
            for( int i = 0; i < routes.length(); i++){
                JSONObject route = routes.getJSONObject(i);
                list.add(new RoutesList(route.getString("id"),route.getString("name"),route.getString("departure"),route.getString("destination")));
                ids.put(route.getString("name"), route.getString("id"));
            }
            this.routesIds = ids;
            this.routesList = list;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            this.activity.finish();
        }
    }
}
