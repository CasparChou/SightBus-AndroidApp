package tenoz.lab.sightbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by AppleCaspar on 2016/11/16.
 */

public class FragmentRoute extends Fragment {

    private ListView stop_route;
    private ListView estimate_route;
    private SimpleAdapter stop_adapter;
    private SimpleAdapter estimate_adapter;

    private View clickSourceGo, clickSourceBack;
    private View touchSourceGo, touchSourceBack;
    private View view;
    private boolean goBack = false;

    private String routeId;
    private SimpleAdapter estimate, stops;
    private List<Map<String,String>> routesList = new ArrayList<Map<String, String>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        routeId =  getArguments().getString("RouteID");
        goBack = getArguments().getBoolean("goBack");

//        if( !goBack ) {
//            view = inflater.inflate(R.layout.fragment_route_go, container, false);
//            estimate_route =   ((ListView) view.findViewById(R.id.estimate_route_go));
//            stop_route =  ((ListView) container.getRootView().findViewById(R.id.stop_route_go));
//        } else {
//            view = inflater.inflate(R.layout.fragment_route_back, container, false);
//            estimate_route =  ((ListView) view.findViewById(R.id.estimate_route_back));
//            stop_route =  ((ListView) view.findViewById(R.id.stop_route_back));
//        }

//        estimate_route.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (touchSourceBack == null) touchSourceBack = v;
//                if (v == touchSourceBack) {
//                    estimate_route.dispatchTouchEvent(event);
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        clickSourceBack = v;
//                        touchSourceBack = null;
//                    }
//                }
//                return false;
//            }
//        });
        return (RelativeLayout)inflater.inflate((!goBack)?R.layout.fragment_route_go:R.layout.fragment_route_back,container,false);
    }

//    public void setData(Context context, List<Map<String, String>> list){
//        routesList = list;
//        estimateAdapterBuilder(context, list);
//        stopsAdapterBuilder(context, list);
//        updateListView();
//
//    }
//
//    public void estimateAdapterBuilder(Context context, List<Map<String, String>> list ){
//        estimate_adapter = new SimpleAdapter(
//                context,
//                list,
//                android.R.layout.simple_expandable_list_item_2,
//                new String[] {"estimate", ""},
//                new int[] {android.R.id.text1, android.R.id.text2}
//        ){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//                View view = super.getView(position, convertView, parent);
//                TextView title = (TextView) view.findViewById(android.R.id.text1);
//                title.setTextSize(25);
//                title.setTextColor(Color.argb(255,0,158,112));
//                title.setHeight(100);
//
//                TextView timeText = (TextView) view.findViewById(android.R.id.text2);
//                timeText.setText("分鐘");
//                timeText.setTextColor(Color.GRAY);
//
//                return view;
//            }
//        };
//    }
//    private void stopsAdapterBuilder(Context context, List<Map<String, String>> list){
//        stop_adapter = new SimpleAdapter(
//                context,
//                list,
//                android.R.layout.simple_expandable_list_item_1,
//                new String[] {"title"},
//                new int[] {android.R.id.text1}
//        ){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//                View view = super.getView(position, convertView, parent);
//                TextView title = (TextView) view.findViewById(android.R.id.text1);
//                title.setTextSize(23);
//                title.setHeight(100);
//                return view;
//            }
//        };
//
//    }
//
//    public void updateListView(){
//        if(stop_adapter != null){
//            stop_route.setAdapter(stop_adapter);
//            stop_adapter.notifyDataSetChanged();
//        }
//        if( estimate_adapter != null ){
//            estimate_route.setAdapter(estimate_adapter);
//            estimate_adapter.notifyDataSetChanged();
//        }
//    }

}
