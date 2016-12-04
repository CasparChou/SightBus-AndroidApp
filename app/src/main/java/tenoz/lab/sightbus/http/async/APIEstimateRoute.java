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
    String queryRouteid = "";
    EstimateRoutesActivity activity = null;
    List<Map<String,String>> routesGoList = new ArrayList<Map<String, String>>();
    List<Map<String,String>> routesBackList = new ArrayList<Map<String, String>>();

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (EstimateRoutesActivity) activity[0];
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
    }

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.i("PostExec", fetch);
        if( queryRouteid != this.activity.getRouteId() ){
            return;
        }
        try {
            parser();
            this.activity.setData(routesGoList, routesBackList);
            this.activity.isDownloading = false;
        } catch (NullPointerException e){

        }
//        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.ProgressBar );
//        bar.setVisibility(View.INVISIBLE);
    }


    private void parser(){
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray routes = jsonRootObject.optJSONArray("stops");
            this.activity.setDestination(jsonRootObject.getString("destination"));
            this.activity.setDeparture(jsonRootObject.getString("departure"));
            for( int i = 0; i < routes.length(); i++){
                Map<String, String> datum = new HashMap<String, String>(2);
                JSONObject stop = routes.getJSONObject(i);
                datum.put("title",  stop.getString("name"));
                int time = Integer.parseInt(stop.getString("time"));
                int update = Integer.parseInt(stop.getString("update"));
                int event =  stop.optInt("event",-1);
                Log.i("ESTI", stop.getString("seq")+":"+time+":"+stop.getString("name")+":"+event);

                String estimate = estimateTime(time, update, event);

                datum.put("estimate",  estimate );
                if( stop.getInt("goBack") == 0){
                    routesGoList.add( datum );
                } else if ( stop.getInt("goBack") == 1 ){
                    routesBackList.add(datum);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            this.activity.finish();
        }
    }



    private String estimateTime( int countdown, int update, int event){

        long now = System.currentTimeMillis()/1000;
        long newtime = (countdown - (now - update));
        if( event != -1){
            if ( event == 0){
                return "已離站";
            } else {
                return "進站中";
            }
        }
        if( countdown < 0 ){
            return "未發車";
        }
        if (newtime < 0){
            return "已離站";
        }
//        if( newtime < 60 ){
//            return "將到站";
//        }
        if( newtime < 100 ){
            return "小於" + newtime +"秒";
        }
        return newtime/60 + "";
    }
}
