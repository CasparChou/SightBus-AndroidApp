package tenoz.lab.sightbus.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tenoz.lab.sightbus.R;

/**
 * Created by AppleCaspar on 2016/12/4.
 */


public class PlanListAdapter extends ArrayAdapter<PlanList> {
    public PlanListAdapter(Context context, ArrayList<PlanList> PlanLists) {
        super(context, 0, PlanLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlanList plan = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_plan_results, parent, false);
        }
//        RoutePlannerResult_Estimate
//        RoutePlannerResult_Route
//        RoutePlannerResult_Direction
//        RoutePlannerResult_Far
//        RoutePlannerResult_Wait
//        RoutePlannerResult_RouteTime

        TextView estimate = (TextView) convertView.findViewById(R.id.RoutePlannerResult_Estimate);
        TextView route = (TextView) convertView.findViewById(R.id.RoutePlannerResult_Route);
        TextView direction = (TextView) convertView.findViewById(R.id.RoutePlannerResult_Direction);
        TextView far = (TextView) convertView.findViewById(R.id.RoutePlannerResult_Far);
        TextView wait = (TextView) convertView.findViewById(R.id.RoutePlannerResult_Wait);
        TextView routeTime = (TextView) convertView.findViewById(R.id.RoutePlannerResult_RouteTime);

        direction.setText(plan.destination);

        int waitTime = estimateTime(plan.time, plan.update);
        int avgTime = (int) Math.round(plan.avgTime);

        far.setText("經過 "+plan.far+"站");
        route.setText(plan.name);
        estimate.setText((waitTime + avgTime/60)+"");
        if(waitTime == 0){
            wait.setText("進站中");
            estimate.setText("進站中");
        } else {
            wait.setText("等車"+waitTime+"分鐘");
        }
        routeTime.setText("路程" + avgTime/60+"分鐘");
//        stop.setText(estimate.name+"");
//        if(estimate.destination){
//            ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_img)))
//                    .setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.stop_dst));
//        } else if(estimate.departure){
//            ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_img)))
//                    .setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.stop_dep));
//        } else {
//            ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_img)))
//                    .setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.stop_both));
//        }
//        if( estimate.name.length() > 9 ){
//            stop.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            stop.setMarqueeRepeatLimit(100);
//            stop.setSelected(true);
//        }
//        if( estimate.plate != "null" ){
//            plate.setText(estimate.plate+"");
//            convertView.findViewById(R.id.list_estimate_plate_layout).setVisibility(View.VISIBLE);
//            convertView.findViewById(R.id.list_estimate_time_layout).setVisibility(View.INVISIBLE);
//            if(estimate.event != -1){
//                if( estimate.event == 0 ){
//                    ((ImageView)(convertView.findViewById(R.id.list_estimate_bus))).setImageResource(R.drawable.bus_gray64);
//                } else {
//                    ((ImageView)(convertView.findViewById(R.id.list_estimate_bus))).setImageResource(R.drawable.bus64);
//                }
//            }
//        } else {
//            convertView.findViewById(R.id.list_estimate_plate_layout).setVisibility(View.INVISIBLE);
//            convertView.findViewById(R.id.list_estimate_time_layout).setVisibility(View.VISIBLE);
//        }
//
//        if(estimate.event != -1){
//            event.setText(estimate.event == 0 ?"已經離站。":"進站中");
//            stop.setHeight(110);
//            event.setHeight(80);
//
//        } else {
//            stop.setHeight(100);
//            event.setHeight(0);
//        }

        return convertView;
    }





    private int estimateTime(Integer countdown, Integer update){

        Long now = System.currentTimeMillis()/1000;
        Long new_Time = (countdown - (now - update));
        if( countdown < 0 ){
            return -1;
        } else if (new_Time.compareTo(0L) < 0){
            return 0;
        } else if( new_Time.compareTo(60L) < 0 ){
           return -1;
        } else if( (new Long(new_Time/60)).compareTo(60L) < 0 ){
            return (int) (new_Time/60);
        } else {
            return -2;
        }
    }
}