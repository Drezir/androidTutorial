package app.android.adam.androidapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class MyService extends IntentService {

    public static boolean RUNNING = false;

    public MyService() {
        super("myService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // background code running
        while (RUNNING) {
            try {
                Intent broadIntent = new Intent();
                broadIntent.setAction("MyAction");
                broadIntent.putExtra("message", "Message from service");
                sendBroadcast(broadIntent); // every app can listen
                Thread.sleep(3000);
            } catch (InterruptedException ignored) { }
        }
    }
}
