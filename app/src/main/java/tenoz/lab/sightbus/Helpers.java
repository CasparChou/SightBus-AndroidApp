package tenoz.lab.sightbus;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by AppleCaspar on 2016/12/25.
 */

public class Helpers {

    public static void initialActionbar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary));
        }
    }

    public static void initialActionbar(AppCompatActivity activity, String title){
        activity.getSupportActionBar().setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary));
        }
    }
    public static void initialHomeBtnActionbar(AppCompatActivity activity){
        activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(activity, R.color.colorPrimary));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setElevation(0);
        final Drawable homeIcon = ContextCompat.getDrawable(activity,R.drawable.abc_ic_ab_back_material);
        homeIcon.setColorFilter(ContextCompat.getColor(activity,R.color.defaultGray), PorterDuff.Mode.SRC_ATOP);
        activity.getSupportActionBar().setHomeAsUpIndicator(homeIcon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimary));
        }
    }

}
