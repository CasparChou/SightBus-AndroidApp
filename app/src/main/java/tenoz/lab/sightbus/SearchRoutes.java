package tenoz.lab.sightbus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.commonsware.cwac.merge.MergeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tenoz.lab.sightbus.http.Api;

public class SearchRoutes extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String,String>> routesList = new ArrayList<Map<String, String>>();
    private Map<String,String> routesIds = new HashMap<String, String>();
    private EditText query;
    private String currentQuering = "";
    private int cwnd = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_routes);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        getSupportActionBar().setTitle("尋找路線");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x00DD00));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_search_routes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255,251,140,0));
        }
        listView = (ListView) findViewById( R.id.SearchRoutes_ResultsList );
        query = (EditText) findViewById(R.id.SearchRoutes_SearchBar);
        EditText editText = (EditText) findViewById(R.id.SearchRoutes_SearchBar);
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
                                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.SearchRoutes_ProgressBar);
                                        getSupportActionBar().setTitle("尋找中...");
                                        progressBar.setVisibility(View.VISIBLE);
                                        Api.searchRoutes(SearchRoutes.this);
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
                String itemSelected = (String) ((TwoLineListItem) view).getText1().getText();
                for( Map.Entry<String,String> i : routesIds.entrySet()){
                    if( itemSelected.equals(i.getKey())){
                        String routeId = i.getValue();
                        Intent routeEstimate = new Intent(getApplicationContext(), EstimateRoutesActivity.class);
                        routeEstimate.putExtra("Route ID", routeId);
                        routeEstimate.putExtra("Route Name", i.getKey());
                        startActivity(routeEstimate);
                        break;
                    }
                }

            }
        });
    }

    public String getQuery(){
        return query.getText().toString();
    }

    public void setRoutesIds(Map<String, String> ids){
        routesIds = ids;
        getSupportActionBar().setTitle("尋找路線");
        cwnd--;
    }
    public void setRoutesList(List<Map<String, String>> routesList) {
        this.routesList = routesList;
        adapter = new SimpleAdapter(
                this,
                routesList,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "goto"},
                new int[] {android.R.id.text1,android.R.id.text2}
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView title = (TextView) view.findViewById(android.R.id.text1);
                title.setTextSize(20);

                TextView timeText = (TextView) view.findViewById(android.R.id.text2);
                title.setTextColor(Color.BLACK);
                timeText.setTextColor(Color.GRAY);
                return view;
            }
        };
        try {
            MergeAdapter mergeAdapter = new MergeAdapter();
            mergeAdapter.addAdapter(adapter);

            listView.setAdapter(mergeAdapter);
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "應用程式錯誤", Toast.LENGTH_LONG);
            this.finish();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
