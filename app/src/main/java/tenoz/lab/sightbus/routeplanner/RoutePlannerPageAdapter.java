package tenoz.lab.sightbus.routeplanner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by AppleCaspar on 2016/12/4.
 */

public class RoutePlannerPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{
    final private List<PageView>pageList;
    public RoutePlannerPageAdapter( List<PageView> pages){
        this.pageList = pages;
    }
    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pageList.get(position));
        return pageList.get(position);
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if( position == 2 ){
            ((RoutePlannerPlanningResults)pageList.get(2)).planning();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}