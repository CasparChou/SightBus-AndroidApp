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


public class StopsListAdapter extends ArrayAdapter<StopsList> {
    public StopsListAdapter(Context context, ArrayList<StopsList> stopsLists) {
        super(context, 0, stopsLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StopsList stops = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_stops, parent, false);
        }

        TextView stop = (TextView) convertView.findViewById(R.id.list_stops_stop);
        TextView routes = (TextView) convertView.findViewById(R.id.list_stops_routes);

        stop.setText(stops.stop);
        routes.setText(stops.routes);

        return convertView;
    }
}