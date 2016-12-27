package tenoz.lab.sightbus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import tenoz.lab.sightbus.data.StopsList;
import tenoz.lab.sightbus.data.StopsListAdapter;
import tenoz.lab.sightbus.http.Api;

public class SearchStops extends AppCompatActivity {

    private ListView listView;
    private EditText query;
    private String currentQuering = "";
    private int cwnd = 0;
    private ArrayList<StopsList> stopsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stops);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Helpers.initialHomeBtnActionbar(this);
        listView = (ListView) findViewById( R.id.SearchStops_ResultsList);
        query = (EditText) findViewById(R.id.SearchStops_SearchBar);

        EditText editText = (EditText) findViewById(R.id.SearchStops_SearchBar);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.SearchStops_ProgressBar);
                                        ((TextView)(findViewById(R.id.SearchStops_Title))).setText("尋找中...");
                                        progressBar.setVisibility(View.VISIBLE);
                                        Api.searchStops(SearchStops.this);
                                    }
                                }
                            },
                            1000+ cwnd*1000);
                    handled = true;
                }
                return handled;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((StopsList) parent.getAdapter().getItem(position)).stop;
                String routes = ((StopsList) parent.getAdapter().getItem(position)).routes;

                Intent stopsEstimate = new Intent(getApplicationContext(), EstimateStopsActivity.class);
                stopsEstimate.putExtra("Stop Name", name);
                stopsEstimate.putExtra("Stop Routes", routes);
                String buffer = name+":"+routes;
                String cache = name+":"+routes+";";
                SharedPreferences pref = getSharedPreferences("search.sightbus", MODE_PRIVATE);
                String current = pref.getString("stops","");
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
                            Log.i("SPLIT", in);
                            cache += in+";";
                        }

                    }
                }
                Log.i("Next", cache);
                pref.edit().remove("stops").commit();
                pref.edit().putString("stops",cache).commit();

                startActivity(stopsEstimate);
            }
        });
        getCacheToList();
    }
    private void getCacheToList() {
        SharedPreferences pref = getSharedPreferences("search.sightbus", MODE_PRIVATE);
        String current = pref.getString("stops","");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getQuery(){
        return query.getText().toString();
    }

    public void jobsDone(){
        ((TextView)(findViewById(R.id.SearchStops_Title))).setText("尋找站牌");
        cwnd--;
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void setStopsList(ArrayList<StopsList> stopsLists) {
        this.stopsLists = stopsLists;
        (findViewById(R.id.SearchStops_Not_Found)).setVisibility(View.INVISIBLE);
        try {
            StopsListAdapter adapter = new StopsListAdapter(this, stopsLists);
            ListView listView = (ListView) findViewById(R.id.SearchStops_ResultsList);
            listView.setAdapter(adapter);
            if(stopsLists.size() == 0){
                (findViewById(R.id.SearchStops_Not_Found)).setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "應用程式錯誤", Toast.LENGTH_LONG);
            this.finish();
        }
    }
}
