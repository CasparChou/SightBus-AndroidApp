package tenoz.lab.sightbus.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tenoz.lab.sightbus.R;

/**
 * Created by AppleCaspar on 2016/12/4.
 */


public class EstimateListAdapter extends ArrayAdapter<EstimateList> {
    public EstimateListAdapter(Context context, ArrayList<EstimateList> estimateLists) {
        super(context, 0, estimateLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EstimateList estimate = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_estimate, parent, false);
        }
        TextView stop = (TextView) convertView.findViewById(R.id.list_estimate_stop);
        TextView event = (TextView) convertView.findViewById(R.id.list_estimate_event);
        TextView countdown = (TextView) convertView.findViewById(R.id.list_estimate_time);
        TextView hint = (TextView) convertView.findViewById(R.id.list_estimate_time_hint);
        TextView plate = (TextView) convertView.findViewById(R.id.list_estimate_plate);

        estimateTime(countdown, hint, estimate.countdown, estimate.update, estimate.event);
        stop.setText(estimate.name+"");
        if(estimate.destination){
            ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_img)))
                    .setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.stop_dst));
        } else if(estimate.departure){
            ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_img)))
                    .setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.stop_dep));
        } else {
            ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_img)))
                    .setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.stop_both));
        }
        if( estimate.name.length() > 9 ){
            stop.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            stop.setMarqueeRepeatLimit(100);
            stop.setSelected(true);
        }
        if( estimate.plate != "null" ){
            plate.setText(estimate.plate+"");
            convertView.findViewById(R.id.list_estimate_plate_layout).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.list_estimate_time_layout).setVisibility(View.INVISIBLE);
            if(estimate.event != -1){
                if( estimate.event == 0 ){
                    ((ImageView)(convertView.findViewById(R.id.list_estimate_bus))).setImageResource(R.drawable.bus_gray64);
                } else {
                    ((ImageView)(convertView.findViewById(R.id.list_estimate_bus))).setImageResource(R.drawable.bus64);
                }
            }
        } else {
            convertView.findViewById(R.id.list_estimate_plate_layout).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.list_estimate_time_layout).setVisibility(View.VISIBLE);
        }

        if(estimate.event != -1){
            event.setText(estimate.event == 0 ?"已經離站。":"進站中");
            stop.setHeight(110);
            event.setHeight(80);

        } else {
            stop.setHeight(100);
            event.setHeight(0);
        }

        return convertView;
    }





    private void estimateTime(TextView time, TextView hint, Integer countdown, Integer update, Integer event){

        Long now = System.currentTimeMillis()/1000;
        Long newTime = (countdown - (now - update));
        if( event != -1){
            if ( event == 0 ){
                time.setText("已離站");
                hint.setText("");
            } else {
                time.setText("進站中");
                if( newTime > 0 ){
                    hint.setText(newTime+"秒內");
                } else {
                    hint.setText("");
                }
            }
        }
        if( countdown < 0 ){
            time.setText("未發車");
            hint.setText("");
        } else if (newTime.compareTo(0L) < 0){
            time.setText("已離站");
            hint.setText("");
        } else if( newTime.compareTo(60L) < 0 ){
            time.setText("將到站");
            hint.setText("");
            if( newTime.compareTo(0L) > 0 ){
                hint.setText(newTime+"秒內");
            }
        } else if( (new Long(newTime/60)).compareTo(60L) < 0 ){
            time.setText( newTime/60 + "");
            hint.setText("分鐘");
        } else {
            time.setText( newTime/3600 + "");
            hint.setText("小時"+(newTime - 3600 * (newTime/3600))%3600+"秒");
        }
    }
}