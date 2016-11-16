package tenoz.lab.sightbus;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import tenoz.lab.sightbus.http.Api;

/**
 * Created by AppleCaspar on 2016/11/16.
 */

public class EstimateRoutesActivity extends AppCompatActivity{

    ViewPager viewPager;
    Integer routeId;
    String routeName;
    FragmentRoute fragRouteGo = new FragmentRoute();
    FragmentRoute fragRouteBack = new FragmentRoute();

    View clickSourceGo, clickSourceBack;
    View touchSourceGo, touchSourceBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_route);
        routeId = getIntent().getExtras().getInt("Route ID");
        routeName = getIntent().getExtras().getString("Route Name");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));


        Api.getEstimateRoute(this);
    }

    public Integer getRouteId() {
        return routeId;
    }

    private class CustomAdapter extends FragmentPagerAdapter{
        private String fragments [] = {"Fa", "Fb"};
        public CustomAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("RouteID", routeId);
            switch (position){
                case 0:
                    bundle.putBoolean("goBack",false);
                    fragRouteGo.setArguments(bundle);
                    return fragRouteGo;
                case 1:
                    bundle.putBoolean("goBack",true);
                    fragRouteBack.setArguments(bundle);
                    return fragRouteBack;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }



    public SimpleAdapter estimateAdapterBuilder( List<Map<String, String>> list ){
        return new SimpleAdapter(
                this,
                list,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {"estimate", ""},
                new int[] {android.R.id.text1, android.R.id.text2}
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView title = (TextView) view.findViewById(android.R.id.text1);
                title.setTextSize(25);
                title.setTextColor(Color.argb(255,0,158,112));
                title.setHeight(100);

                TextView timeText = (TextView) view.findViewById(android.R.id.text2);
                timeText.setText("分鐘");
                timeText.setTextColor(Color.GRAY);


                return view;
            }
        };
    }
    private SimpleAdapter stopsAdapterBuilder(List<Map<String, String>> list){
        return new SimpleAdapter(
                this,
                list,
                android.R.layout.simple_expandable_list_item_1,
                new String[] {"title"},
                new int[] {android.R.id.text1}
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView title = (TextView) view.findViewById(android.R.id.text1);
                title.setTextSize(23);
                title.setHeight(100);
                return view;
            }
        };
    }
    public void setRoutesList(List<Map<String, String>> routesGoList, List<Map<String, String>> routesBackList) {

        ((ListView) findViewById(R.id.estimate_route_go)).setAdapter(estimateAdapterBuilder(routesGoList));
        ((ListView) findViewById(R.id.estimate_route_back)).setAdapter(estimateAdapterBuilder(routesBackList));

        ((ListView) findViewById(R.id.stop_route_go)).setAdapter(stopsAdapterBuilder(routesGoList));
        ((ListView) findViewById(R.id.stop_route_back)).setAdapter(stopsAdapterBuilder(routesBackList));

        ((TextView) findViewById(R.id.route_title_go)).setText(routeName);

        ((ListView) findViewById(R.id.estimate_route_go)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSourceGo == null) touchSourceGo = v;
                if(v == touchSourceGo) {
                    ((ListView) findViewById(R.id.stop_route_go)).dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSourceGo = v;
                        touchSourceGo = null;
                    }
                }
                return false;
            }
        });

        ((ListView) findViewById(R.id.estimate_route_back)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSourceBack == null) touchSourceBack = v;
                if(v == touchSourceBack) {
                    ((ListView) findViewById(R.id.stop_route_back)).dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSourceBack = v;
                        touchSourceBack = null;
                    }
                }
                return false;
            }
        });

    }



}
