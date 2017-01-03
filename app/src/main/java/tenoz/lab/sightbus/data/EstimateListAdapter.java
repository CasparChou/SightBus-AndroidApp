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
        TextView countdown = (TextView) convertView.findViewById(R.id.list_estimate_time);

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

        if(!estimate.isPath){
            estimateTime(countdown, estimate.countdown, estimate.update, estimate.event);
            if( estimate.name.length() > 9 ){
                stop.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                stop.setMarqueeRepeatLimit(100);
                stop.setSelected(true);
            }
        } else {
            countdown.setText("載入中");
            countdown.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
            countdown.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
        }


        return convertView;
    }





    private void estimateTime(TextView time, Integer countdown, Integer update, Integer event){

        Long now = System.currentTimeMillis()/1000;
        Long newTime = (countdown - (now - update));
        if( event != -1){
            if ( event == 0 ){
                time.setText("已離站");
                time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
                time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
            } else {
                time.setText("進站中");
                time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_apporaching));
                time.setTextColor(ContextCompat.getColor(getContext(),R.color.isGood));
            }
        }
        if( countdown == -99 ){
            time.setText("未發車");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
        } else if( countdown < 0 ){
            time.setText("已離站");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
        } else if (newTime.compareTo(0L) < 0){
            time.setText("已離站");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
        } else if( newTime.compareTo(60L) < 0 ){
            time.setText("將到站");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_apporaching));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.isGood));
        } else if( (new Long(newTime/60)).compareTo(60L) < 0 ){
            time.setText( newTime/60 + " 分鐘");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_orange));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
            if((new Long(newTime/60)).compareTo(10L) > 0){
                time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
                time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
            }
        } else {
            time.setText( newTime/3600 + " 小時");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_orange));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
        }
    }
}