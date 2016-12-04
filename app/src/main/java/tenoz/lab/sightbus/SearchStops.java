package tenoz.lab.sightbus;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        final Drawable homeIcon = ContextCompat.getDrawable(this,R.drawable.abc_ic_ab_back_material);
        homeIcon.setColorFilter(ContextCompat.getColor(this,R.color.defaultGray), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(homeIcon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255,251,140,0));
        }
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
//                String itemSelected = (String) ((TwoLineListItem) view).getText1().getText();
//                for( Map.Entry<String,String> i : routesIds.entrySet()){
//                    if( itemSelected.equals(i.getKey())){
//                        String routeId = i.getValue();
//                        Intent routeEstimate = new Intent(getApplicationContext(), EstimateRoutesActivity.class);
//                        routeEstimate.putExtra("Route ID", routeId);
//                        routeEstimate.putExtra("Route Name", i.getKey());
//                        startActivity(routeEstimate);
//                        break;
//                    }
//                }

            }
        });
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
        try {
            StopsListAdapter adapter = new StopsListAdapter(this, stopsLists);
            ListView listView = (ListView) findViewById(R.id.SearchStops_ResultsList);
            listView.setAdapter(adapter);
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "應用程式錯誤", Toast.LENGTH_LONG);
            this.finish();
        }
    }
}
