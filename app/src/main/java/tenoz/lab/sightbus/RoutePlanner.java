package tenoz.lab.sightbus;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import tenoz.lab.sightbus.routeplanner.PageView;
import tenoz.lab.sightbus.routeplanner.RoutePlannerPageAdapter;
import tenoz.lab.sightbus.routeplanner.RoutePlannerPlanningPage;
import tenoz.lab.sightbus.routeplanner.RoutePlannerPlanningResults;

public class RoutePlanner extends AppCompatActivity {

    private ArrayList<PageView> pageList;
    private ViewPager mViewPager;
    private String departure = "";
    private String destination = "";
    private RoutePlannerPageAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_planner);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Helpers.initialHomeBtnActionbar(this);
        setup();

    }

    private void setup() {
        pageList = new ArrayList<>();
        pageList.add( new RoutePlannerPlanningPage(this, 0, this) );
        pageList.add( new RoutePlannerPlanningPage(this, 1, this) );
        pageList.add( new RoutePlannerPlanningResults(this, this) );
        viewPagerAdapter =  new RoutePlannerPageAdapter(pageList, this);
        mViewPager = (ViewPager) findViewById(R.id.RoutePlannerPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return false;
            }
        });

    }
    public ViewPager getViewPager() {
        if (null == mViewPager) {
            mViewPager = (ViewPager) findViewById(R.id.RoutePlannerPager);
        }
        return mViewPager;
    }


    public String getDeparture() {
        return this.departure;
    }
    public String getDestination() {
        return this.destination;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if( getViewPager().getCurrentItem() > 0){
                    getViewPager().setCurrentItem(getViewPager().getCurrentItem()-1);
                } else {
                    this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void hideKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e){

        }
    }
    @Override
    public void onBackPressed() {
        if( getViewPager().getCurrentItem() > 0){
            getViewPager().setCurrentItem(getViewPager().getCurrentItem()-1);
        } else {
            this.finish();
        }
    }
}
