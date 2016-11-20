package tenoz.lab.sightbus;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tenoz.lab.sightbus.http.Api;

/**
 * Created by AppleCaspar on 2016/11/16.
 */

public class EstimateRoutesActivity extends AppCompatActivity{

    ViewPager viewPager;
    String routeId;
    String routeName;
    FragmentRoute fragRouteGo = new FragmentRoute();
    FragmentRoute fragRouteBack = new FragmentRoute();
    FragmentRoute currentFragment = fragRouteGo;
    View clickSourceGo, clickSourceBack;
    View touchSourceGo, touchSourceBack;
    private String departure = "";
    private String destination = "";
    private ProgressBar progress;
    private List<Map<String, String>> routesGoList;
    private List<Map<String, String>> routesBackList;

    public Boolean isDownloading = true;
    Boolean isFirst = true;
    SimpleAdapter estiamte_adapter_go;
    SimpleAdapter estiamte_adapter_back;
    SimpleAdapter stop_adapter_go;
    SimpleAdapter stop_adapter_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_route);
        getSupportActionBar().setTitle( "正在同步..." );
        routeId = getIntent().getExtras().getString("Route ID");
        routeName = getIntent().getExtras().getString("Route Name");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
        progress = (ProgressBar) (findViewById(R.id.Estimate_ProgressBar));
        progress.setMax(160);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x00DD00));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255,251,140,0));
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        currentFragment = fragRouteGo;
                        getSupportActionBar().setTitle( routeName );
                        getSupportActionBar().setSubtitle( "往" + destination );
                        break;
                    case 1:
                        currentFragment = fragRouteBack;
                        getSupportActionBar().setTitle( routeName );
                        getSupportActionBar().setSubtitle( "往" + departure );
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Api.getEstimateRoute(this);

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if( isDownloading ){
                    return;
                }
                int current = (progress.getProgress());
                if( current+1 == progress.getMax() ){
                    isDownloading = true;
                    Api.getEstimateRoute(EstimateRoutesActivity.this);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progress.setProgress(++current%161, true);
                } else {
                    progress.setProgress(++current%161);
                }
            }
        },0,62);
    }

    public String getRouteId() {
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
            bundle.putString("RouteID", routeId);
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



    public SimpleAdapter estimateAdapterBuilder(boolean go ){
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                go?this.routesGoList:this.routesBackList,
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
                if( title.getText() == "未發車" ||  title.getText() == "將到站"   ){
                    timeText.setText("");
                    title.setTextSize(18);
                    if(title.getText() == "未發車" ){
                        title.setTextColor(Color.argb(255,80,80,80));
                    }
                    title.setBackgroundColor(Color.argb(0,255,255,255));
                    timeText.setBackgroundColor(Color.argb(0,255,255,255));
                    title.setPadding(0,25,0,0);
                } else {
                    title.setPadding(0,0,0,5);
                    timeText.setText("分鐘");
                    if( Integer.valueOf(title.getText().toString()) >= 10 && Integer.valueOf(title.getText().toString()) < 20 ) {
                        title.setTextColor(Color.argb(255,255,255,255));
                        title.setBackgroundColor(Color.argb(255,27,94,32));
                        timeText.setTextColor(Color.argb(255,255,255,255));
                        timeText.setBackgroundColor(Color.argb(255,27,94,32));
                    } else if( Integer.valueOf(title.getText().toString()) >= 20 ){
                        title.setTextColor(Color.argb(255,255,255,255));
                        title.setBackgroundColor(Color.argb(255,66,66,66));
                        timeText.setTextColor(Color.argb(255,255,255,255));
                        timeText.setBackgroundColor(Color.argb(255,66,66,66));
                    } else {
                        title.setTextColor(Color.argb(255,255,255,255));
                        title.setBackgroundColor(Color.argb(255,76,175,180));
                        timeText.setTextColor(Color.argb(255,255,255,255));
                        timeText.setBackgroundColor(Color.argb(255,76,175,180));
                    }

                }
                timeText.setTextColor(Color.GRAY);
                timeText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                return view;
            }
        };
        adapter.notifyDataSetChanged();

        return adapter;
    }
    private SimpleAdapter stopsAdapterBuilder(boolean go){
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                go?this.routesGoList:this.routesBackList,
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
        adapter.notifyDataSetChanged();

        return adapter;
    }
    public void setData(List<Map<String, String>> routesGoList, List<Map<String, String>> routesBackList){
        if( isFirst ) {
            this.routesGoList = routesGoList;
            this.routesBackList = routesBackList;
        } else {
            this.routesGoList.clear();
            this.routesBackList.clear();
            this.routesGoList.addAll(routesGoList);
            this.routesBackList.addAll(routesBackList);
            final ListView  estimate_go  = ((ListView) fragRouteGo.getView().findViewById(R.id.estimate_route_go));
            final ListView  estimate_back  =((ListView) fragRouteBack.getView().findViewById(R.id.estimate_route_back));
            ((SimpleAdapter) estimate_go.getAdapter()).notifyDataSetChanged();
            ((SimpleAdapter) estimate_back.getAdapter()).notifyDataSetChanged();
        }
        setRoutesList();

//        fragRouteGo.setData(this.getApplicationContext(), routesGoList);
//        fragRouteBack.setData(this.getApplicationContext(), routesBackList);
    }

    private void setRoutesList() {
        final ListView  estimate_go  = ((ListView) fragRouteGo.getView().findViewById(R.id.estimate_route_go));
        final ListView  estimate_back  =((ListView) fragRouteBack.getView().findViewById(R.id.estimate_route_back));
        final ListView  stops_go  =((ListView) fragRouteGo.getView().findViewById(R.id.stop_route_go));
        final ListView  stops_back  =((ListView) fragRouteBack.getView().findViewById(R.id.stop_route_back));

        int i = 0;

        if( isFirst ) {
            estiamte_adapter_go = estimateAdapterBuilder(true);
            estiamte_adapter_back = estimateAdapterBuilder(false);
            stop_adapter_go = stopsAdapterBuilder(true);
            stop_adapter_back = stopsAdapterBuilder(false);
            estimate_go.setAdapter(estiamte_adapter_go);
            estimate_back.setAdapter(estiamte_adapter_back);
            stops_go.setAdapter(stop_adapter_go);
            stops_back.setAdapter(stop_adapter_back);

            stops_go.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (touchSourceGo == null) touchSourceGo = v;
                    if (v == touchSourceGo) {
                        estimate_go.dispatchTouchEvent(event);
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            clickSourceGo = v;
                            touchSourceGo = null;
                        }
                    }
                    return false;
                }
            });

            stops_back.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (touchSourceBack == null) touchSourceBack = v;
                    if (v == touchSourceBack) {
                        estimate_back.dispatchTouchEvent(event);
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            clickSourceBack = v;
                            touchSourceBack = null;
                        }
                    }
                    return false;
                }
            });
            isFirst = false;
        } else {

            ((SimpleAdapter) estimate_go.getAdapter()).notifyDataSetChanged();
            for( i = 0; i < routesGoList.size(); i++ ){
                Map<String, String> item = routesGoList.get(i);
                if( ((ListView) fragRouteGo.getView().findViewById(R.id.estimate_route_go)) ==null) {
                    Log.i("NULL", "NULL");
                }
                try {

                    Log.i("LIST", i + ":" + estimate_go.getCount() + "--" +
                            ((TextView) estimate_go.getAdapter().getView(i, null, estimate_go).findViewById(android.R.id.text1)).getText()+":"+
                            ((TextView) stops_go.getAdapter().getView(i, null, estimate_go).findViewById(android.R.id.text1)).getText()+
                            "----------------"+
                            item.get("estimate").toString()+"-------"+
                            item.get("title").toString()
                    );
//                    ((TextView) estimate_go.getAdapter().getView(i, null, estimate_go).findViewById(android.R.id.text1)).setText(item.get("estimate"));
//                    ((TextView) stops_go.getAdapter().getView(i, null, estimate_go).findViewById(android.R.id.text1)).setText(item.get("title"));
//                    estimate_go.getAdapter().getView(i, null, estimate_go).invalidate();
//                    stops_go.getAdapter().getView(i, null, estimate_go).invalidate();
                }catch (NullPointerException e){
                    Log.i("NULL", "NULLAT   "+ i);
                }
            }

           /* i = 0;
            while( routesBackList.iterator().hasNext() ){
                Map<String, String> item = routesBackList.iterator().next();
                ((TextView)estimate_back.getChildAt(i).findViewById(android.R.id.text1)).setText(item.get("estimate"));
                ((TextView)stops_back.getChildAt(i).findViewById(android.R.id.text1)).setText(item.get("title"));
                i++;
            }*/
            estiamte_adapter_go.notifyDataSetChanged();
            stop_adapter_go.notifyDataSetChanged();
            stop_adapter_back.notifyDataSetChanged();
            estiamte_adapter_back.notifyDataSetChanged();
        }
    }


    public void setDestination(String destination){
        this.destination = destination;
        if( isFirst ){
            getSupportActionBar().setTitle( routeName );
            getSupportActionBar().setSubtitle( "往" + destination );
        }
    }
    public void setDeparture(String departure){
        this.departure = departure;
    }

}
