package tenoz.lab.sightbus.routeplanner;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import tenoz.lab.sightbus.R;
import tenoz.lab.sightbus.RoutePlanner;
import tenoz.lab.sightbus.data.PlanList;
import tenoz.lab.sightbus.data.PlanListAdapter;

public class RoutePlannerPlanningResults extends PageView {

    private final RoutePlanner activity;
    private final Context context;
    private View view;
    private int image_tick = 0;
    private int cwnd = 0;
    private String destination;
    private String departure;
    private String endpoint = "http://sightbus.tenoz.asia/routes/plan/?dep=%s&dst=%s";
    private ArrayList<PlanList> planList;
    private String currentQuering = "";

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
                        if( image_tick%5 == 0 ){
                            planning();
                        }
                    }
                });

            }
            }
        },0,500);
    }

    public void planning() {
        departure = activity.getDeparture();
        destination = activity.getDestination();
        if ( !currentQuering.equals(departure+","+destination)) {
            currentQuering = (departure+","+destination);
            (view.findViewById(R.id.RoutePlanningResults_ResultsList)).setVisibility(INVISIBLE);
            (view.findViewById(R.id.RoutePlanningResults_Not_Found)).setVisibility(VISIBLE);
        } else {
            return;
        }
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    if( cwnd < 5 && (departure!= null && departure.length()>0) && (destination!= null && destination.length()>0 )){
                        Log.i("Run Plan", departure+"  ,  "+destination);
                        cwnd++;
//                        ((TextView)(findViewById(R.id.RoutePlanning_Hint))).setText("尋找中...");
                        new APIRoutePlanner().execute();

                    }
                }
            },
            1000+ cwnd*1000);

    }
    public View getView(){
        return view;
    }


    public void setResultList(ArrayList<PlanList> planList) {
        (view.findViewById(R.id.RoutePlanningResults_Not_Found)).setVisibility(INVISIBLE);
        (view.findViewById(R.id.RoutePlanningResults_ResultsList)).setVisibility(VISIBLE);
        this.planList = planList;
//        (findViewById(R.id.SearchRoutes_Not_Found)).setVisibility(View.INVISIBLE);
//        try {
            PlanListAdapter adapter = new PlanListAdapter(this.getContext(), this.planList);
            ListView listView = (ListView) findViewById(R.id.RoutePlanningResults_ResultsList);
            listView.setAdapter(adapter);
//            if(routesList.size() == 0){
//                (findViewById(R.id.SearchRoutes_Not_Found)).setVisibility(View.VISIBLE);
//            }
//        }catch (NullPointerException e){
//            Toast.makeText(activity.getApplicationContext(), "應用程式錯誤", Toast.LENGTH_LONG);
//        }
    }
    public class APIRoutePlanner extends AsyncTask<Void,Integer,String> {

        String fetch = "";
        RoutePlannerPlanningPage activity = null;
        private ArrayList<PlanList> planLists = new ArrayList<PlanList>();

        @Override
        protected String doInBackground(Void... unused) {
            try {
                URL url = new URL(String.format(endpoint, URLEncoder.encode(departure, "utf-8"), URLEncoder.encode(destination, "utf-8")));
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
            parser();
            setResultList(planLists);
            cwnd--;
        }


        private void parser(){
            ArrayList<PlanList> list = new ArrayList<PlanList>();
            try {
                JSONObject jsonRootObject = new JSONObject(fetch);
                JSONArray routes = jsonRootObject.optJSONArray("routes");
                for( int i = 0; i < routes.length(); i++){
                    JSONObject route = routes.getJSONObject(i);
                    PlanList stop2 = new PlanList(route);
                    list.add(stop2);
                    Log.i("lll", stop2.toString());
                }

                for ( int i = 0; i < list.size(); i++){
                    for ( int j = 0; j < list.size(); j++){
                        if ( list.get(i).compareTo(list.get(j))  < 0  ){
                            Collections.swap(list, i, j);
//                            if( list.get(j).time < 0 ){
//                                list.remove(j);
//                            }
                        }
                    }
                }

                this.planLists = list;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                Toast.makeText(context, "應用程式錯誤", Toast.LENGTH_LONG);
            }
        }
    }
}
