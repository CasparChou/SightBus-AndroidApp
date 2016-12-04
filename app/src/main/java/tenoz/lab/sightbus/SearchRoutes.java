package tenoz.lab.sightbus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tenoz.lab.sightbus.data.RoutesList;
import tenoz.lab.sightbus.data.RoutesListAdapter;
import tenoz.lab.sightbus.http.Api;

public class SearchRoutes extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<RoutesList> routesList = new ArrayList<RoutesList>();
    private Map<String,String> routesIds = new HashMap<String, String>();
    private EditText query;
    private String currentQuering = "";
    private int cwnd = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_routes);
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
                                        ((TextView)(findViewById(R.id.SearchRoutes_Title))).setText("尋找中...");
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

    public void setRoutesIds(Map<String, String> ids){
        routesIds = ids;
        ((TextView)(findViewById(R.id.SearchRoutes_Title))).setText("尋找路線");
        cwnd--;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    public void setRoutesList(ArrayList<RoutesList> routesList) {
        this.routesList = routesList;
        try {
            RoutesListAdapter adapter = new RoutesListAdapter(this, routesList);
            ListView listView = (ListView) findViewById(R.id.SearchRoutes_ResultsList);
            listView.setAdapter(adapter);
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "應用程式錯誤", Toast.LENGTH_LONG);
            this.finish();
        }
    }

}
