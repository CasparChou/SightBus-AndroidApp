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
import java.net.URLEncoder;
import java.util.ArrayList;

import tenoz.lab.sightbus.EstimateStopsActivity;
import tenoz.lab.sightbus.data.EstimateStopList;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class APIEstimateStop extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    String queryStop = "";
    EstimateStopsActivity activity = null;
    ArrayList<EstimateStopList> stopsList = new ArrayList<>();

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (EstimateStopsActivity) activity[0];
        if( this.activity.isDestroyed() || this.activity.isDestroyed() ){
            cancel(true);
        }
        queryStop = this.activity.getStop();
        try {
            URL url = new URL(
                    String.format(
                            "http://sightbus.tenoz.asia/stops/estimate/?stop=%s",
                            URLEncoder.encode(queryStop.toString())
                    )
            );
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
        if( queryStop != this.activity.getStop() ){
            return;
        }
        if( this.activity.isDestroyed() || this.activity.isDestroyed() ){
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
            JSONArray routes = jsonRootObject.optJSONArray("results");
            for( int i = 0; i < routes.length(); i++){
                EstimateStopList data = new EstimateStopList( routes.getJSONObject(i) ) ;
                stopsList.add(data);
            }

            this.activity.setEstimateList(stopsList);
            this.activity.isDownloading = false;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            this.activity.finish();
        }
    }
}
