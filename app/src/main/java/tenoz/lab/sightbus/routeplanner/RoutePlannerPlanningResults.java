package tenoz.lab.sightbus.routeplanner;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import tenoz.lab.sightbus.R;
import tenoz.lab.sightbus.RoutePlanner;

public class RoutePlannerPlanningResults extends PageView {

    private final RoutePlanner activity;
    private final Context context;
    private View view;
    private int image_tick = 0;
    public RoutePlannerPlanningResults(final Context context, final Activity activity) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.activity_route_planner_results, null);
        this.activity = (RoutePlanner)activity;
        this.context = context;
        addView(view);


        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if( ((RoutePlanner) activity).getViewPager().getCurrentItem() == 2 ){
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            image_tick = (image_tick+1)%3;
                            ((ImageView)view.findViewById(R.id.RoutePlannerResult_img))
                                    .setImageDrawable(
                                            ContextCompat.getDrawable(
                                                    context,
                                                    context.getResources().getIdentifier("tunnel256_"+image_tick, "drawable", context.getPackageName())
                                            )
                                    );
                        }
                    });

                }
            }
        },0,500);
    }

    public View getView(){
        return view;
    }

}
