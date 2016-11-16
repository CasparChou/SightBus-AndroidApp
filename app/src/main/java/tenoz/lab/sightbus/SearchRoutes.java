package tenoz.lab.sightbus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
    private Map<String,Integer> routesIds = new HashMap<String, Integer>();
    private EditText query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stop);
        getSupportActionBar().setTitle("尋找路線");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x00DD00));
        listView = (ListView) findViewById( R.id.ResultsList );
        query = (EditText) findViewById(R.id.SearchBar);
        Button searchBtn = (Button) findViewById(R.id.SearchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
                if( progressBar.getVisibility() == View.INVISIBLE ){
                    progressBar.setVisibility(View.VISIBLE);
                    Api.searchRoutes(SearchRoutes.this);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = (String) ((TwoLineListItem) view).getText1().getText();
                for( Map.Entry<String,Integer> i : routesIds.entrySet()){
                    if( itemSelected.equals(i.getKey())){
                        Integer routeId = i.getValue();
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

    public void setRoutesIds(Map<String, Integer> ids){
        routesIds = ids;
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
                timeText.setTextColor(Color.GRAY);

                return view;
            }
        };

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(adapter);
        listView.setAdapter(mergeAdapter);
    }
}
