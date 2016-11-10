package tenoz.lab.sightbus.http;

import android.app.Activity;
import android.os.AsyncTask;

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


    public static void getNearbyStops(Activity activity)
    {
        apiTask = new Stops();
        apiTask.execute(activity,null,null);
    }
}
