package tenoz.lab.sightbus;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import tenoz.lab.sightbus.data.EstimateStopList;
import tenoz.lab.sightbus.data.EstimateStopListAdapter;
import tenoz.lab.sightbus.http.Api;

/**
 * Created by AppleCaspar on 2016/11/16.
 */

public class EstimateStopsActivity extends AppCompatActivity{

    private ArrayList<EstimateStopList> stopList;
    private EstimateStopListAdapter adapter;
    public boolean isDownloading = false;
    private String stopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_stop);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
        getSupportActionBar().setElevation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255,251,140,0));
        }
        setup();
        Api.getEstimateStop(this);
    }

    private void setup(){
        stopName = getIntent().getExtras().getString("Stop Name");

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.layout_estimate_stops_actionbar);
        TextView title = ((TextView) (findViewById(R.id.EstimateStops_Actionbar_Title)));
        title.setText(stopName);
        if( stopName.length() > 9 ){
            title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            title.setMarqueeRepeatLimit(100);
            title.setSelected(true);
        }
        final Drawable homeIcon = ContextCompat.getDrawable(this,R.drawable.abc_ic_ab_back_material);
        homeIcon.setColorFilter(ContextCompat.getColor(this,R.color.defaultGray), PorterDuff.Mode.SRC_ATOP);
        ((ImageButton)(findViewById(R.id.EstimateStops_BackButton))).setImageDrawable(homeIcon);



        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if( EstimateStopsActivity.this.isDestroyed() || EstimateStopsActivity.this.isFinishing() ){
                    cancel();
                }
                if( isDownloading ){
                    return;
                } else {
                    isDownloading = true;
                    Api.getEstimateStop(EstimateStopsActivity.this);
                }
//                int current = (progress.getProgress());
//                if( current+1 == progress.getMax() ){
//                    isDownloading = true;
//                    Api.getEstimateRoute(EstimateRoutesActivity.this);
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    progress.setProgress(++current%161, true);
//                } else {
//                    progress.setProgress(++current%161);
//                }
            }
        },0, 10*1000);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void backHome (View view){
        this.finish();
    }

    public String getStop() {
        return stopName;
    }

    public void setEstimateList(ArrayList<EstimateStopList> fetchList) {
        boolean dirty = (null == this.stopList || this.stopList.size() <1)?false:true;
        Log.i("IS DIRTY?", dirty?"TRUE":"FALSE");
        try {
            if(dirty){
                this.stopList.clear();
                this.stopList.addAll(fetchList);
                adapter.notifyDataSetChanged();
            } else {

                this.stopList = new ArrayList<>();
                this.stopList.addAll(fetchList);
                adapter = new EstimateStopListAdapter(this, this.stopList);
                ((ListView) findViewById(R.id.EstimateStops_EstimateList)).setAdapter(adapter);
            }
            for ( int i = 0; i < fetchList.size(); i ++ ){
                Log.i(i+"", fetchList.get(i).toString() + "  ----  " + adapter.getItem(i).toString() );
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "應用程式錯誤", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
}




