package tenoz.lab.sightbus.estimate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import tenoz.lab.sightbus.R;

public class EstimateRoutesPage extends PageView{

    private View view;
    public EstimateRoutesPage(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.layout_estimate_routes, null);
        addView(view);
    }

    public View getView(){
        return view;
    }
}
