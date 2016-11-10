package tenoz.lab.sightbus;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tenoz.lab.sightbus.http.Api;

public class StopsActivity extends AppCompatActivity {

    private ListView listView;
    private ListView listInfoView;


    View clickSource;
    View touchSource;

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
        listInfoView = (ListView) findViewById( R.id.route_info_list );
        overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    listInfoView.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });


        loadList();
    }

    private void loadList(){
        Api.getRoutes(this);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_in, R.anim.translate_out);
    }

    public void setRoutesList(List<Map<String, String>> routesList) {
        this.routesList = routesList;

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
        //        listView.setAdapter(adapter);
        //        listInfoView.setAdapter(adapter);
        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(adapter);
//        mergeAdapter.addAdapter(adapter);
        listView.setAdapter(mergeAdapter);
        listInfoView.setAdapter(mergeAdapter);
    }
}
