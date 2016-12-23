package tenoz.lab.sightbus.routeplanner;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import tenoz.lab.sightbus.R;
import tenoz.lab.sightbus.RoutePlanner;
import tenoz.lab.sightbus.data.StopsList;

public class RoutePlannerPlanningResults extends PageView {

    private final RoutePlanner activity;
    private final Context context;
    private View view;
    private int image_tick = 0;
    private int cwnd = 0;
    private String destination;
    private String departure;
    private String endpoint = "http://sightbus.tenoz.asia/routes/plan/?dep=%s&dst=%s";

    public RoutePlannerPlanningResults(final Context context, final Activity activity) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.activity_route_planner_results, null);
        this.activity = (RoutePlanner)activity;
        this.context = context;
        addView(view);


        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if( ((RoutePlanner) activity).getViewPager().getCurrentItem() == 2 ){
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            image_tick = (image_tick+1)%3;
                            ((ImageView)view.findViewById(R.id.RoutePlannerResult_img))
                                    .setImageDrawable(
                                            ContextCompat.getDrawable(
                                                    context,
                                                    context.getResources().getIdentifier("tunnel256_"+image_tick, "drawable", context.getPackageName())
                                            )
                                    );
                        }
                    });

                }
            }
        },0,500);
    }

    public void planning() {
        departure = activity.getDeparture();
        destination = activity.getDestination();
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    if( cwnd < 5 && departure.length()>0 && destination.length()>0 ){
                        cwnd++;
                        ((TextView)(findViewById(R.id.RoutePlanning_Hint))).setText("尋找中...");
                        new APIRoutePlanner().execute();

                    }
                }
            },
            1000+ cwnd*1000);

    }
    public View getView(){
        return view;
    }

    public class APIRoutePlanner extends AsyncTask<Void,Integer,String> {

        String fetch = "";
        RoutePlannerPlanningPage activity = null;
        private ArrayList<StopsList> stopsLists = new ArrayList<StopsList>();

        @Override
        protected String doInBackground(Void... unused) {
            try {
                URL url = new URL(String.format(endpoint, URLEncoder.encode(departure, "utf-8"), URLEncoder.encode(destination, "utf-8")));
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
//            setResultList(stopsLists);
            cwnd--;
        }


        private void parser(){
            ArrayList<StopsList> list = new ArrayList<StopsList>();
            try {
                JSONObject jsonRootObject = new JSONObject(fetch);
                JSONArray routes = jsonRootObject.optJSONArray("results");
                for( int i = 0; i < routes.length(); i++){
                    JSONObject route = routes.getJSONObject(i);
                    list.add(new StopsList(route.getString("stop"),route.getString("routes")));
                }
                this.stopsLists = list;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                Toast.makeText(context, "應用程式錯誤", Toast.LENGTH_LONG);
            }
        }
    }
}
