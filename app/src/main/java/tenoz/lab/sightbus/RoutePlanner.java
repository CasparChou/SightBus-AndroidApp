package tenoz.lab.sightbus;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import tenoz.lab.sightbus.routeplanner.PageView;
import tenoz.lab.sightbus.routeplanner.RoutePlannerPageAdapter;
import tenoz.lab.sightbus.routeplanner.RoutePlannerPlanningPage;
import tenoz.lab.sightbus.routeplanner.RoutePlannerPlanningResults;

public class RoutePlanner extends AppCompatActivity {

    private ArrayList<PageView> pageList;
    private ViewPager mViewPager;
    private String departure;
    private String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_planner);
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
        setup();

    }

    private void setup() {
        pageList = new ArrayList<>();
        pageList.add( new RoutePlannerPlanningPage(this, 0, this) );
        pageList.add( new RoutePlannerPlanningPage(this, 1, this) );
        pageList.add( new RoutePlannerPlanningResults(this, this) );
        mViewPager = (ViewPager) findViewById(R.id.RoutePlannerPager);
        mViewPager.setAdapter( new RoutePlannerPageAdapter(pageList));
    }
    public ViewPager getViewPager() {
        if (null == mViewPager) {
            mViewPager = (ViewPager) findViewById(R.id.RoutePlannerPager);
        }
        return mViewPager;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}
