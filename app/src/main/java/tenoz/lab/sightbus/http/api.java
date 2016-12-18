package tenoz.lab.sightbus.http;

import android.app.Activity;
import android.os.AsyncTask;

import tenoz.lab.sightbus.estimate.PageView;
import tenoz.lab.sightbus.http.async.APIEstimateRoute;
import tenoz.lab.sightbus.http.async.APIEstimateStop;
import tenoz.lab.sightbus.http.async.APINearestStop;
import tenoz.lab.sightbus.http.async.APIRoutesAtStop;
import tenoz.lab.sightbus.http.async.APISearchRoutes;
import tenoz.lab.sightbus.http.async.APISearchStops;
import tenoz.lab.sightbus.http.async.Stops;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class Api {
    private static AsyncTask<Activity,Integer,String> apiTask;
    private static AsyncTask<PageView,Integer,String> apiPageViewTask;

    public static void getRoutesAtStops(Activity activity)
    {
        apiTask = new APIRoutesAtStop();
        apiTask.execute(activity,null,null);
    }


    public static void searchRoutes(Activity activity)
    {
        apiTask = new APISearchRoutes();
        apiTask.execute(activity,null,null);
    }

    public static void searchStops(Activity activity)
    {
        apiTask = new APISearchStops();
        apiTask.execute(activity,null,null);
    }
    public static void getNearbyStops(Activity activity)
    {
        apiTask = new Stops();
        apiTask.execute(activity,null,null);
    }

    public static void getEstimateRoute(Activity activity) {
        apiTask = new APIEstimateRoute();
        apiTask.execute(activity,null,null);
    }


    public static void getEstimateStop(Activity activity) {
        apiTask = new APIEstimateStop();
        apiTask.execute(activity,null,null);
    }

    public static void getNearest(Activity activity) {
        apiTask = new APINearestStop();
        apiTask.execute(activity,null,null);

    }

}
