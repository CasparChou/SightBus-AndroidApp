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

import tenoz.lab.sightbus.EstimateRoutesActivity;
import tenoz.lab.sightbus.data.EstimateList;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class APIEstimateRoute extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    String queryRouteid = "";
    EstimateRoutesActivity activity = null;
    ArrayList<EstimateList> routesGoList = new ArrayList<>();
    ArrayList<EstimateList> routesBackList = new ArrayList<>();

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (EstimateRoutesActivity) activity[0];
        if( this.activity.isDestroyed() || this.activity.isDestroyed() ){
            cancel(true);
        }
        queryRouteid = this.activity.getRouteId();
        try {
            URL url = new URL("http://sightbus.tenoz.asia/routes/estimate/?route="+this.activity.getRouteId().toString());
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
        if( this.activity.isDestroyed() || this.activity.isDestroyed() ){
            cancel(true);
        }
    }

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.i("PostExec", fetch);
        if( queryRouteid != this.activity.getRouteId() ){
            return;
        }
        if( this.activity.isDestroyed() || this.activity.isDestroyed() ){
            cancel(true);
            return;
        }
        try {
            parser();
        } catch (NullPointerException e){

        }
//        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.ProgressBar );
//        bar.setVisibility(View.INVISIBLE);
    }


    private void parser(){
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray routes = jsonRootObject.optJSONArray("stops");
            for( int i = 0; i < routes.length(); i++){
                JSONObject stop = routes.getJSONObject(i);
                Integer time = Integer.parseInt(stop.getString("time"));
                Integer update = Integer.parseInt(stop.getString("update"));
                Integer event =  stop.optInt("event",-1);
                EstimateList data = new EstimateList(
                        stop.getString("stop"),
                        stop.getString("name"),
                        stop.getInt("seq"),
                        stop.getInt("goBack")==1?true:false,
                        time,
                        update,
                        event,
                        stop.getString("plate"),
                        false,
                        false
                ) ;

                if( stop.getInt("goBack") == 0){
                    routesGoList.add( data );
                } else if ( stop.getInt("goBack") == 1 ){
                    routesBackList.add(data);
                }
            }
            routesGoList.get(0).departure = true;
            routesGoList.get(routesGoList.size()-1).destination = true;
            routesBackList.get(0).departure = true;
            routesBackList.get(routesBackList.size()-1).destination = true;

            this.activity.setEstimateList(routesGoList, routesBackList);
            this.activity.isDownloading = false;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            this.activity.finish();
        }
    }
}
