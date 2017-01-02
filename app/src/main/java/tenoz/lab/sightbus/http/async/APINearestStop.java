package tenoz.lab.sightbus.http.async;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import tenoz.lab.sightbus.MainActivity;
import tenoz.lab.sightbus.R;

/**
 * Created by AppleCaspar on 2016/10/24.
 */

public class APINearestStop extends AsyncTask<Activity,Integer,String>{

    String fetch = "";
    MainActivity activity = null;
    private List<Map<String, String>> routesList;

    @Override
    protected String doInBackground(Activity... activity) {
        this.activity = (MainActivity) activity[0];
        try {
            Location location = this.activity.getLastKnownLocation();
            URL url = new URL("http://sightbus.tenoz.asia/stops/nearest/" +
                    "?lat=" + URLEncoder.encode(location.getLatitude()+"", "utf-8") +
                    "&lng="+ URLEncoder.encode(location.getLongitude()+"", "utf-8")
            );
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            int data = in.read();
            Log.i("Network", "read");

            while( data != -1 ){
                fetch += String.valueOf((char)data);
                data = in.read();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fetch;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.i("PostExec", fetch);
        parser();
//        this.activity.setRoutesList(routesList);
//        this.activity.jobsDone();
//        ProgressBar bar = (ProgressBar) this.activity.findViewById( R.id.SearchStops_ProgressBar );
//        bar.setVisibility(View.INVISIBLE);
    }


    private void parser(){

        Log.i("Nearest", fetch);
        try {
            JSONObject jsonRootObject = new JSONObject(fetch);
            JSONArray routes = jsonRootObject.optJSONArray("nearest");
            String name = "";
            for( int i = 0; i < 1; i++){
                name = routes.getJSONObject(i).getString("name");
                /*if( !routes.getJSONObject(i).getString("name").equals(((Button)this.activity.findViewById(R.id.main_nearest_btn)).getText()) ){

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.activity.getApplicationContext());
                    mBuilder.setSmallIcon(R.drawable.marker);
                    mBuilder.setContentTitle("接近 ["+ name +"]" );
                    mBuilder.setColor(Color.rgb(250,250,250));
                    mBuilder.setContentText(Math.ceil(Double.valueOf(routes.getJSONObject(i).getString("distance"))*10000)/10+" 公尺");
                    mBuilder.setVibrate(new long[]{0L,0L,0L,0L});
                    mBuilder.setSound(Uri.parse("android.resource://"+this.activity.getPackageName()+"/"+R.raw.silence));
                    mBuilder.setPriority(Notification.PRIORITY_MAX);

                    Intent resultIntent = new Intent(this.activity.getApplicationContext(), MainActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.activity);
                    stackBuilder.addParentStack(MainActivity.class);

                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager = (NotificationManager) this.activity.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }*/
                ((Button)this.activity.findViewById(R.id.main_nearest)).setText(name);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            this.activity.finish();
        }
    }
}
