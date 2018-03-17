package familylocation.bhulijyo.com.familylocation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends FragmentActivity  {

    static String TAG;

    private FragmentTabHost mTabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = MainActivity.this.getClass().getName();
        AWSMobileClient.getInstance().initialize(this).execute();
        setContentView(R.layout.main_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        AWSProvider.initialize(this);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab1"),
                MapFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Tab2"),
                FriendsFragment.class, null);
        //mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Tab3"),
          //      Tab3Fragment.class, null);
    }




}
