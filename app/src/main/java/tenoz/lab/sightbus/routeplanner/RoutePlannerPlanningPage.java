package tenoz.lab.sightbus.routeplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Collections;

import tenoz.lab.sightbus.R;
import tenoz.lab.sightbus.RoutePlanner;
import tenoz.lab.sightbus.data.StopsList;
import tenoz.lab.sightbus.data.StopsListAdapter;

import static android.content.Context.MODE_PRIVATE;

public class RoutePlannerPlanningPage extends PageView {

    private final ListView listView;
    private View view;
    private EditText query;
    private String currentQuering = "";
    private int cwnd = 0;
    final private Context context;
    final private int step;
    final private RoutePlanner activity;
    private ArrayList<StopsList> stopsLists;

    public RoutePlannerPlanningPage(final Context context, final Integer  step, final RoutePlanner activity) {
        super(context);
        this.step = step;
        this.context = context;
        this.activity = activity;

        view = LayoutInflater.from(context).inflate(R.layout.layout_route_planner_steps, null);
        if( this.step == 0 ){
            ((TextView)(view.findViewById(R.id.RoutePlanning_Hint))).setText(R.string.route_planning_dep);
        } else {
            ((TextView)(view.findViewById(R.id.RoutePlanning_Hint))).setText(R.string.route_planning_dst);
        }


        listView = (ListView) view.findViewById(R.id.RoutePlanning_ResultsList);
        query = (EditText) view.findViewById(R.id.RoutePlanning_SearchStops);
        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    if( cwnd < 5 && query.getText().length()>0 && !currentQuering.equals(query.getText().toString())){
                                        cwnd++;
                                        currentQuering = query.getText().toString();
                                        ((TextView)(findViewById(R.id.RoutePlanning_Hint))).setText("尋找中...");
                                        new APIStopsRoutePlanner().execute();

                                    }
                                }
                            },
                            1000+ cwnd*1000);
                    handled = true;
                }
                return handled;
            }
        });
        addView(view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((StopsList) parent.getAdapter().getItem(position)).stop;
                String routes = ((StopsList) parent.getAdapter().getItem(position)).routes;

                String buffer = name+":"+routes;
                String cache = name+":"+routes+";";
                SharedPreferences pref = context.getSharedPreferences("routeplanner.search.sightbus", MODE_PRIVATE);
                String current;
                if( step == 0 ) {
                    current = pref.getString("departure", "");
                } else {
                    current = pref.getString("destination", "");
                }
                Log.i("Current", current);
                ArrayList<String> split = new ArrayList<String>();
                Collections.addAll(split, current.split(";"));
                Log.i("CACHE", cache.substring(0,cache.length()-1));
                if(!split.contains(buffer)){
                    if(split.size() > 5){
                        for( int i = 0; i <  4; i++){
                            cache += split.get(i)+";";
                        }
                    } else {
                        cache += current;
                    }
                } else {
                    for( String in : split){
                        if( !in.equals(buffer) ){
                            cache += in+";";
                        }
                    }
                }
                Log.i("Next", cache);
                if( step == 0 ){
                    pref.edit().remove("departure").commit();
                    pref.edit().putString("departure",cache).commit();
                    activity.setDeparture(name);
                    activity.getViewPager().setCurrentItem(1);
                } else {
                    pref.edit().remove("destination").commit();
                    pref.edit().putString("destination",cache).commit();
                    activity.hideKeyboard();
                    activity.setDestination(name);
                    activity.getViewPager().setCurrentItem(2);
                }
            }
        });
        getCacheToList();
    }
    private void getCacheToList() {
        SharedPreferences pref = context.getSharedPreferences("routeplanner.search.sightbus", MODE_PRIVATE);
        String current = "";
        if( step == 0 ) {
            current = pref.getString("departure","");
        } else {
            current = pref.getString("destination","");
        }
        if( current.contains(";") ){
            ArrayList <StopsList> stopsRecord = new ArrayList<>();
            String[] list = current.split(";");
            for( String item : list ){
                String[] field = item.split(":");
                stopsRecord.add(new StopsList(field[0],field[1]));
            }
            setStopsList(stopsRecord);
        }
    }
    public View getView(){
        return view;
    }

    public String getQuery() {
        return currentQuering;
    }

    public void jobsDone(){
        if( this.step == 0 ){
            ((TextView)(view.findViewById(R.id.RoutePlanning_Hint))).setText(R.string.route_planning_dep);
        } else {
            ((TextView)(view.findViewById(R.id.RoutePlanning_Hint))).setText(R.string.route_planning_dst);
        }
        cwnd--;
    }
    public void setStopsList(ArrayList<StopsList> stopsLists) {
        this.stopsLists = stopsLists;
        (findViewById(R.id.RoutePlanning_Not_Found)).setVisibility(View.INVISIBLE);
        try {
            StopsListAdapter adapter = new StopsListAdapter(context, stopsLists);
            ListView listView = (ListView) findViewById(R.id.RoutePlanning_ResultsList);
            listView.setAdapter(adapter);
            if(stopsLists.size() == 0){
                (findViewById(R.id.RoutePlanning_Not_Found)).setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException e){
            Toast.makeText(context, "應用程式錯誤", Toast.LENGTH_LONG);
        }
    }
    public class APIStopsRoutePlanner extends AsyncTask<Void,Integer,String> {

        String fetch = "";
        RoutePlannerPlanningPage activity = null;
        private ArrayList<StopsList> stopsLists = new ArrayList<StopsList>();

        @Override
        protected String doInBackground(Void... unused) {
            try {
                URL url = new URL(
                        "http://sightbus.tenoz.asia/stops/search/?query=" + URLEncoder.encode(currentQuering, "utf-8"));
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
            setStopsList(stopsLists);
            jobsDone();
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
