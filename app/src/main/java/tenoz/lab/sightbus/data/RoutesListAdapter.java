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


public class RoutesListAdapter extends ArrayAdapter<RoutesList> {
    public RoutesListAdapter(Context context, ArrayList<RoutesList> routesLists) {
        super(context, 0, routesLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoutesList routes = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_routes, parent, false);
        }
        TextView header = (TextView) convertView.findViewById(R.id.list_routs_routes);
        TextView text = (TextView) convertView.findViewById(R.id.list_routs_goto);

        header.setText(routes.name);
        text.setText(routes.departure+" - "+ routes.destination);

        return convertView;
    }
}