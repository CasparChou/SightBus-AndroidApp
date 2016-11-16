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

    private ListView stopList;
    private ListView estimateList;
    private boolean goBack = false;

    private Integer routeId;

    private SimpleAdapter estimate, stops;
    private List<Map<String,String>> routesList = new ArrayList<Map<String, String>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        routeId =  getArguments().getInt("RouteID");
        goBack = getArguments().getBoolean("goBack");

        return (RelativeLayout)inflater.inflate((!goBack)?R.layout.fragment_route_go:R.layout.fragment_route_back,container,false);
    }

}
