package grandpapa.com.gameoflife;

import android.app.Application;
import android.util.Log;

/**
 * Created by sergey on 4/14/16.
 */
public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    public void onCreate()
    {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });
    }

    private void handleUncaughtException (Thread thread, Throwable e)
    {
        // The following shows what I'd like, though it won't work like this.
        Log.e(TAG, "Exception", e);
    }
}
