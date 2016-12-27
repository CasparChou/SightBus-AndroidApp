package tenoz.lab.sightbus.routeplanner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tenoz.lab.sightbus.RoutePlanner;

/**
 * Created by AppleCaspar on 2016/12/4.
 */

public class RoutePlannerPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    final private List<PageView>pageList;
    private final RoutePlanner activity;

    public RoutePlannerPageAdapter(List<PageView> pages, RoutePlanner routePlanner){
        this.pageList = pages;
        this.activity = routePlanner;
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

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
