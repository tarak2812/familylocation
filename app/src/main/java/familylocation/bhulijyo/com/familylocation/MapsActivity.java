package familylocation.bhulijyo.com.familylocation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DynamoDBMapper dynamoDBMapper;
    static String TAG;
    LocationsDO currentLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = MapsActivity.this.getClass().getName();
        AWSMobileClient.getInstance().initialize(this).execute();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        AWSProvider.initialize(this);
        final IdentityManager identityManager = AWSProvider.getInstance().getIdentityManager();
        Runnable runnable = new Runnable() {
            public void run() {

                currentLocation = getLocation("tarakbhatt");
                // Add a marker in Sydney and move the camera
                LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MapsActivity.this.updateLocationOnMap(current);

                //LocationsDO locationItem = dynamoDBMapper.load(LocationsDO.class, "tarakbhatt", "Tarak"
                        //);

                //Log.d(TAG, "Location Item: "+ locationItem.getLatitude().toString()+ locationItem.getLongitude().toString());
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    private void updateLocationOnMap(final LatLng position)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addMarker(new MarkerOptions().position(position).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            }
        });
    }

    private LocationsDO getLocation(String userId)
    {
        LocationsDO key = new LocationsDO();
        key.setUserId(userId);
        DynamoDBQueryExpression<LocationsDO> queryExpression = new DynamoDBQueryExpression<LocationsDO>().withHashKeyValues(key);
        final List<LocationsDO> storedEntries = dynamoDBMapper
                .query(LocationsDO.class, queryExpression);
        for (LocationsDO entry: storedEntries) {
            Log.d(TAG, "Location Item: " + entry.getLatitude() + entry.getLongitude());
        }
        if (!storedEntries.isEmpty()){
            return storedEntries.get(0);
        }
        return null;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }
}
