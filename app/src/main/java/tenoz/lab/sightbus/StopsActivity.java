package tenoz.lab.sightbus;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StopsActivity extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String,String>> routesList = new ArrayList<Map<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);
        Log.i("Test", getIntent().getExtras().getString("Title@Actionbar"));
        getSupportActionBar().setTitle(getIntent().getExtras().getString("Title@Actionbar"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x00DD00));
        listView = (ListView) findViewById( R.id.route_list );


//        setListAdapter(adapter);
        loadList();
    }

    private void loadList(){
        String data = "{\"route\":[\"99\",\"111\",\"235\",\"513\",\"635\",\"636\",\"637\",\"638\",\"639\",\"797\",\"799\",\"800\",\"801\",\"802\",\"802區\",\"810\",\"842\",\"845\",\"880\",\"883\",\"1501\",\"1503\",\"5009\",\"5675\"]}";

        try {
            JSONObject  jsonRootObject = new JSONObject(data);
            JSONArray routes = jsonRootObject.optJSONArray("route");

            for( int i = 0; i < routes.length(); i++){
                Log.i("Route", routes.getString(i));
                Map<String, String> datum = new HashMap<String, String>(2);
                datum.put("title",  routes.getString(i));
                datum.put("date",  System.currentTimeMillis() / 1000L +"");
                routesList.add( datum );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                this,
                routesList,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "date"},
                new int[] {android.R.id.text1,android.R.id.text2}
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView title = (TextView) view.findViewById(android.R.id.text1);
                title.setTextSize(20);

                TextView timeText = (TextView) view.findViewById(android.R.id.text2);
                timeText.setTextColor(Color.GRAY);
                return view;
            }
        };
        listView.setAdapter(adapter);


        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    ProgressBar bar = (ProgressBar) findViewById( R.id.ProgressBar );
                    bar.setVisibility(View.INVISIBLE);
                }
            },
            10000);

    }
}
