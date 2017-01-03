package tenoz.lab.sightbus.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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


public class EstimateStopListAdapter extends ArrayAdapter<EstimateStopList> {
    public EstimateStopListAdapter(Context context, ArrayList<EstimateStopList> estimateLists) {
        super(context, 0, estimateLists);
    }
    public enum StopListView{
        LAYOUT,
        TITLE_TEXT,
        LEFT_TIME,
        TIME_HINT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EstimateStopList estimate = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_estimate_stop, parent, false);
        }
        View[] goDestination = {
                convertView.findViewById(R.id.list_estimate_stop_from_destination_time_layout),
                convertView.findViewById(R.id.list_estimate_stop_from_destination_text),
                convertView.findViewById(R.id.list_estimate_stop_from_destination_time),
//                convertView.findViewById(R.id.list_estimate_stop_from_destination_time_hint)
        };
        View[] goDeparture = {
                convertView.findViewById(R.id.list_estimate_stop_from_departure_time_layout),
                convertView.findViewById(R.id.list_estimate_stop_from_departure_text),
                convertView.findViewById(R.id.list_estimate_stop_from_departure_time),
//                convertView.findViewById(R.id.list_estimate_stop_from_departure_time_hint)
        };

        ((TextView)convertView.findViewById(R.id.list_estimate_stop_route)).setText(estimate.routeName+"");
        if( !estimate.goDestination.destination.equals("null") ){
            ((TextView) goDestination[StopListView.TITLE_TEXT.ordinal()]).setText("往"+estimate.goDestination.destination);
            estimateTime(
                    (TextView) goDestination[StopListView.LEFT_TIME.ordinal()],
                    estimate.goDestination.countDown,
                    estimate.goDestination.updateTime
            );
        } else {
            ((TextView) goDestination[StopListView.TITLE_TEXT.ordinal()]).setText("");
            ((TextView) goDestination[StopListView.LEFT_TIME.ordinal()]).setText("不經此站");
        }


        if( !estimate.goDeparture.destination.equals("null") ) {
            ((TextView) goDeparture[StopListView.TITLE_TEXT.ordinal()]).setText("往" + estimate.goDeparture.destination);
            estimateTime(
                (TextView) goDeparture[StopListView.LEFT_TIME.ordinal()],
                estimate.goDeparture.countDown,
                estimate.goDeparture.updateTime
            );
        } else {
            ((TextView) goDeparture[StopListView.TITLE_TEXT.ordinal()]).setText((estimate.goDeparture.toString().equals("null")?"去程":"回程"));
            ((TextView) goDeparture[StopListView.LEFT_TIME.ordinal()]).setText("不經此站");
            ((TextView) goDeparture[StopListView.LEFT_TIME.ordinal()]).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_white));
            ((TextView) goDeparture[StopListView.LEFT_TIME.ordinal()]).setTextColor(ContextCompat.getColor(getContext(),R.color.defaultGray));
//            ((TextView) goDeparture[StopListView.TIME_HINT.ordinal()]).setText("");
        }



//
//        if( estimate.name.length() > 9 ){
//            route.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            route.setMarqueeRepeatLimit(100);
//            route.setSelected(true);
//        }
//        if( estimate.plate != "null" ){
//            plate.setText(estimate.plate+"");
//            convertView.findViewById(R.id.list_estimate_stop_plate_layout).setVisibility(View.VISIBLE);
//            convertView.findViewById(R.id.list_estimate_stop_time_layout).setVisibility(View.INVISIBLE);
//            if(estimate.event != -1){
//                if( estimate.event == 0 ){
//                    ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_bus))).setImageResource(R.drawable.bus_gray64);
//                } else {
//                    ((ImageView)(convertView.findViewById(R.id.list_estimate_stop_bus))).setImageResource(R.drawable.bus64);
//                }
//            }
//        } else {
//            convertView.findViewById(R.id.list_estimate_stop_plate_layout).setVisibility(View.INVISIBLE);
//            convertView.findViewById(R.id.list_estimate_stop_time_layout).setVisibility(View.VISIBLE);
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





    private void estimateTime(TextView time, Integer countdown, Integer update){

        Long now;
        Long newTime;
        try{
            now = System.currentTimeMillis()/1000;
            newTime = (countdown - (now - update));
        }catch (NullPointerException n){
            time.setText("N");
//            hint.setText("N");
            return;
        }
//        if( event != -1){
//            if ( event == 0 ){
//                time.setText("已離站");
//                hint.setText("");
//            } else {
//                time.setText("進站中");
//                if( newTime > 0 ){
//                    hint.setText(newTime+"秒內");
//                } else {
//                    hint.setText("");
//                }
//            }
//        }
        if( countdown < 0 ){
            time.setText("未發車");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
//            hint.setText("");
        } else if (newTime.compareTo(0L) < 0){
            time.setText("已離站");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
//            hint.setText("");
        } else if( newTime.compareTo(60L) < 0 ){
            time.setText("將到站");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_apporaching));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.isGood));
//            hint.setText("");
            if( newTime.compareTo(0L) > 0 ){
//                hint.setText(newTime+"秒內");
            }
        } else if( (new Long(newTime/60)).compareTo(60L) < 0 ){
            time.setText( newTime/60 + "分鐘");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_orange));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
//            hint.setText("分鐘");
        } else {
            time.setText( "> 1小時");
            time.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_text_not_recommend));
            time.setTextColor(ContextCompat.getColor(getContext(),R.color.defaultWhite));
//            hint.setText("小時"+(newTime - 3600 * (newTime/3600))%3600+"秒");
        }
    }
}