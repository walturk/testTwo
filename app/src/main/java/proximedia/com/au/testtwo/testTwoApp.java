package proximedia.com.au.testtwo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import proximedia.com.au.testtwo.util.Constants;

/**
 * Created by Walid Al-Turk on 24/02/2018.
 */

public class testTwoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // we can do any app level initialisations here
        //
        Log.d(Constants.TAG, "App starting...");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
