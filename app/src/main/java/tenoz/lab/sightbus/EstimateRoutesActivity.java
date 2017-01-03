package tenoz.lab.sightbus;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tenoz.lab.sightbus.data.EstimateList;
import tenoz.lab.sightbus.data.EstimateListAdapter;
import tenoz.lab.sightbus.estimate.EstimatePageAdapter;
import tenoz.lab.sightbus.estimate.EstimateRoutesPage;
import tenoz.lab.sightbus.estimate.PageView;
import tenoz.lab.sightbus.http.Api;

/**
 * Created by AppleCaspar on 2016/11/16.
 */

public class EstimateRoutesActivity extends AppCompatActivity{

    private String routeId;
    private String routeName;
    private String departure;
    private String destination;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<PageView> pageList;
    private ArrayList<EstimateList> estimateGo;
    private ArrayList<EstimateList> estimateBack;
    private View pageGo, pageBack;
    private ListView listGo, listBack;
    private EstimateListAdapter adapterGo, adapterBack;
    public boolean isDownloading = false;
    private long updateTick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_route);

        routeId = getIntent().getExtras().getString("Route ID");
        routeName = getIntent().getExtras().getString("Route Name");
        departure = getIntent().getExtras().getString("Route Departure");
        destination = getIntent().getExtras().getString("Route Destination");
//        listGo = (new Gson().fromJson(getIntent().getExtras().getString("GoList"), EstimateList.class));


        Helpers.initialActionbar(this);
        Api.getEstimateRoute(this);
        setup();
    }

    private void setup(){

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.layout_estimate_routes_actionbar);
        ((TextView)(findViewById(R.id.EstimateRoutes_Actionbar_Title))).setText(routeName);

        final Drawable homeIcon = ContextCompat.getDrawable(this,R.drawable.abc_ic_ab_back_material);
        homeIcon.setColorFilter(ContextCompat.getColor(this,R.color.defaultGray), PorterDuff.Mode.SRC_ATOP);
        ((ImageButton)(findViewById(R.id.EstimateRoutes_BackButton))).setImageDrawable(homeIcon);

        mTablayout = (TabLayout) findViewById(R.id.EstimateRoutesTabs);
        mTablayout.addTab(mTablayout.newTab().setText(destination));
        mTablayout.addTab(mTablayout.newTab().setText(departure));

        pageList = new ArrayList<>();
        pageList.add(new EstimateRoutesPage(this));
        pageList.add(new EstimateRoutesPage(this));

        mViewPager = (ViewPager) findViewById(R.id.EstimateRoutesPager);
        mViewPager.setAdapter( new EstimatePageAdapter(pageList));
//        ((TextView)(((EstimateRoutesPage)pageList.get(0)).getView().findViewById(R.id.button2))).setText("AAA");
//        ((TextView)(((EstimateRoutesPage)pageList.get(1)).getView().findViewById(R.id.button2))).setText("BB");

        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));


        pageGo = ((EstimateRoutesPage)pageList.get(0)).getView();
        pageBack = ((EstimateRoutesPage)pageList.get(1)).getView();

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if( EstimateRoutesActivity.this.isDestroyed() || EstimateRoutesActivity.this.isFinishing() ){
                    cancel();
                }
                if( isDownloading ){
                    return;
                } else {
                    isDownloading = true;
                    Api.getEstimateRoute(EstimateRoutesActivity.this);
                }
//                int current = (progress.getProgress());
//                if( current+1 == progress.getMax() ){
//                    isDownloading = true;
//                    Api.getEstimateRoute(EstimateRoutesActivity.this);
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    progress.setProgress(++current%161, true);
//                } else {
//                    progress.setProgress(++current%161);
//                }
            }
        },0, 10*1000);
        listGo = (ListView) pageGo.findViewById(R.id.EstimateRoutes_EstimateList);
        listBack = (ListView) pageBack.findViewById(R.id.EstimateRoutes_EstimateList);
        setEstimateList(
                (ArrayList<EstimateList>) getIntent().getExtras().getSerializable("GoList"),
                (ArrayList<EstimateList>) getIntent().getExtras().getSerializable("BackList")
        );
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

    public String getRouteId() {
        return routeId;
    }

    public void setEstimateList(ArrayList<EstimateList> estimateGo, ArrayList<EstimateList> estimateBack) {
        boolean dirty = (null == this.estimateGo || this.estimateGo.size() <1)?false:true;
        Log.i("IS DIRTY?", dirty?"TRUE":"FALSE");
        try {
            if(dirty){
                this.estimateGo.clear();
                this.estimateBack.clear();
                this.estimateGo.addAll(estimateGo);
                this.estimateBack.addAll(estimateBack);
                adapterGo.notifyDataSetChanged();
                adapterBack.notifyDataSetChanged();
                for ( int i = 0; i < this.estimateGo.size(); i ++ ){
                    Log.i(i+"", this.estimateGo.get(i).toString() + "  ----  " + adapterGo.getItem(i).toString() );
                }
            } else {

                this.estimateGo = new ArrayList<>();
                this.estimateBack = new ArrayList<>();
                this.estimateGo.addAll(estimateGo);
                this.estimateBack.addAll(estimateBack);
                adapterGo = new EstimateListAdapter(this, this.estimateGo);
                adapterBack = new EstimateListAdapter(this, this.estimateBack);
                listGo.setAdapter(adapterGo);
                listBack.setAdapter(adapterBack);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "應用程式錯誤", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
    public void backHome (View view){
        this.finish();
    }


}




