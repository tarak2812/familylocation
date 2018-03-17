package familylocation.bhulijyo.com.familylocation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class MapFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GoogleMap mMap;

    // TODO: Rename and change types of parameters
    static String TAG;

    private LocationsDO currentLocation ;
    DynamoDBMapper dynamoDBMapper;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = MapFragment.this.getClass().getName();

    }

    private void updateLocationOnMap(final LatLng position, final String userName)
    {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addMarker(new MarkerOptions().position(position).title(userName));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.maplocation_tab, container, false);



        final IdentityManager identityManager = AWSProvider.getInstance().getIdentityManager();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
        Runnable runnable = new Runnable() {
            public void run() {

                currentLocation = getLocation("tarakbhatt");
                // Add a marker in Sydney and move the camera
                LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MapFragment.this.updateLocationOnMap(current, "Tarak");

                //LocationsDO locationItem = dynamoDBMapper.load(LocationsDO.class, "tarakbhatt", "Tarak"
                //);

                //Log.d(TAG, "Location Item: "+ locationItem.getLatitude().toString()+ locationItem.getLongitude().toString());
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        return V;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //this.getMapAsync(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


}
