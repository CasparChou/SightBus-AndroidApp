package tenoz.lab.sightbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void selfDestruct(View view) {

        Intent intent = new Intent(getApplicationContext(), EstimateRoutesActivity.class);
        startActivity(intent);

    }

    public void openMap(View view){

        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);

    }

    public void openSearchStops(View view){

        Intent intent = new Intent(getApplicationContext(), SearchRoutes.class);
        startActivity(intent);

    }

}
