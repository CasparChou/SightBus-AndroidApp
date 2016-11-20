package tenoz.lab.sightbus.http;

import android.app.Activity;
import android.os.AsyncTask;

import tenoz.lab.sightbus.http.async.APIEstimateRoute;
import tenoz.lab.sightbus.http.async.APISearchRoutes;
import tenoz.lab.sightbus.http.async.APISearchStops;
import tenoz.lab.sightbus.http.async.Routes;
import tenoz.lab.sightbus.http.async.Stops;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class Api {
    private static AsyncTask<Activity,Integer,String> apiTask;

    public static void getRoutes(Activity activity)
    {
        apiTask = new Routes();
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
}
