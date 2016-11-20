package tenoz.lab.sightbus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255,251,140,0));
        }
    }

    public void selfDestruct(View view) {

        Intent intent = new Intent(getApplicationContext(), EstimateRoutesActivity.class);
        startActivity(intent);

    }

    public void openMap(View view){

        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);

    }

    public void openSearchRoutes(View view){

        Intent intent = new Intent(getApplicationContext(), SearchRoutes.class);
        startActivity(intent);

    }
    public void openSearchStops(View view){

        Intent intent = new Intent(getApplicationContext(), SearchStops.class);
        startActivity(intent);

    }

}
