package gpslocater.project.plotter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {
    Log log;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getlatlng();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getlatlng();
    }

    private void getlatlng(){
        DBHelper mydb = new DBHelper(this);
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Double lng = location.getLongitude();
        Double lat = location.getLatitude();
        log.d("lat",lat.toString());
        log.d("lng",lng.toString());
//        try {
//            mydb.insertReading(lat.toString(), lng.toString());
//        }catch(NullPointerException Ne) {
//            mydb.insertReading(lat.toString(), lng.toString());
//           // Toast.makeText(getBaseContext(),Ne.toString(),Toast.LENGTH_LONG).show();
//        }
        setUpMapIfNeeded(lat, lng);
    }

    private void setUpMapIfNeeded(double lat,double lng) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(lat,lng);
            }
        }
    }

    private void setUpMap(double lat,double lng) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_save) {
            DBHelper mydb = new DBHelper(this);
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Double lng = location.getLongitude();
            Double lat = location.getLatitude();
            log.d("lat",lat.toString());
            log.d("lng",lng.toString());
            try {
                mydb.insertReading(lat.toString(), lng.toString());
                Toast.makeText(getBaseContext(),"Saved",Toast.LENGTH_LONG).show();
            }catch(NullPointerException Ne) {
                 Toast.makeText(getBaseContext(),Ne.toString(),Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.menu_show) {
            Intent i = new Intent(this,DbActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
