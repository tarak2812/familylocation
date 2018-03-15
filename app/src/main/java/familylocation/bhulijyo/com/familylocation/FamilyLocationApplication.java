package familylocation.bhulijyo.com.familylocation;

import android.app.Application;

/**
 * Created by tarak on 24/02/18.
 */

public class FamilyLocationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the AWS Provider
        AWSProvider.initialize(getApplicationContext());

    }
}