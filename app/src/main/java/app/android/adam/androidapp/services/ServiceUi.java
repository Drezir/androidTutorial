package app.android.adam.androidapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Random;

public class ServiceUi extends Service {

    private final IBinder binder = new ServiceUiBinder();
    private static final Random mGenerator = new Random();

    public static class ServiceUiBinder extends Binder {
        public ServiceUi getService() {
            return new ServiceUi();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
}
