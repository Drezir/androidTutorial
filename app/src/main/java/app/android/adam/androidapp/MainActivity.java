package app.android.adam.androidapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import app.android.adam.androidapp.broadcast.MyReceiver;
import app.android.adam.androidapp.interfaces.FragmentDataExtractor;
import app.android.adam.androidapp.maps.MapActivity;
import app.android.adam.androidapp.services.MyJobService;
import app.android.adam.androidapp.services.MyService;
import app.android.adam.androidapp.services.ServiceUi;
import app.android.adam.androidapp.shopitems.ShopItemsActivity;
import app.android.adam.androidapp.user.User;

public class MainActivity extends AuthenticatedActivity {

    private static final int GPS_FINE_LOCATION_REQUEST_CODE = 1;
    private static final int RECEIVE_SMS_REQUEST_CODE = 1;

    private Button topTextGps;
    private Button mainThreadCounter;

    private boolean counterRunning;

    private Intent intentService;

    private ServiceUi serviceUi;
    private boolean mBound;
    private ServiceConnection mConnection;

    private int jobId = 0;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ServiceUi.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mainToastButton).setOnClickListener(new ButtonToastListener());
        findViewById(R.id.mainListButton).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ShopItemsActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.mainPopupButton).setOnClickListener(new ButtonShowPopup());
        findViewById(R.id.mainAlertButton).setOnClickListener(new ButtonShowAlert());
        findViewById(R.id.mainShowNotification).setOnClickListener(new ButtonNotification());
        findViewById(R.id.mainSendBroadcast).setOnClickListener(new ButtonBroadcast());
        findViewById(R.id.mainToggleService).setOnClickListener(new ButtonService());
        findViewById(R.id.mainServiceData).setOnClickListener(new ButtonServiceData());
        findViewById(R.id.mainShowMap).setOnClickListener(new ButtonShowMap());

        topTextGps = findViewById(R.id.mainTopTextGps);
        topTextGps.setOnClickListener(v -> {
            checkGpsPermissions();
        });
        mainThreadCounter = findViewById(R.id.mainThreadCounter);
        mainThreadCounter.setOnClickListener(v -> {
            counterRunning = !counterRunning;
            if (counterRunning) {
                new Thread(new CounterRunner()).start();
            }
        });
        findViewById(R.id.mainShowContacts).setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        });

        checkReadSmsPermissions();
        checkGpsPermissions();
        setupBroadcast();
        jobService();

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServiceUi.ServiceUiBinder binder = (ServiceUi.ServiceUiBinder)service;
                serviceUi = binder.getService();
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBound = false;
            }
        };
    }

    private void jobService() {

        JobInfo.Builder builder = new JobInfo.Builder(jobId, new ComponentName(this, MyJobService.class));
        builder.setPeriodic(2000);
        builder.setPersisted(true);
        builder.setRequiresDeviceIdle(true);
        builder.setRequiresCharging(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
    private void setupBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MyAction");
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.addCategory("android.intent.category.DEFAULT");
        registerReceiver(new MyReceiver(), intentFilter);
    }
    private void service() {
        if (MyService.RUNNING) {
            stopService(intentService);
        } else {
            intentService = new Intent(this, MyService.class);
            startService(intentService);
        }
        MyService.RUNNING = !MyService.RUNNING;
    }

    private class CounterRunner implements Runnable {

        private int count = 0;

        @Override
        public void run() {
            while(counterRunning) {
                mainThreadCounter.setText(Integer.toString(count++));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    private void setupGps() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        topTextGps.setText(location.getLatitude() + ":" + location.getLongitude());
    }

    private void checkGpsPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        GPS_FINE_LOCATION_REQUEST_CODE);
                return;
            }
        }
        setupGps();
    }
    private void checkReadSmsPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.RECEIVE_SMS},
                        RECEIVE_SMS_REQUEST_CODE);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GPS_FINE_LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupGps();
                } else {
                    topTextGps.setText("Permissions denied, tap again");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.searchMenu).getActionView();
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu : {
                User.INSTANCE.logout();
                recreate();
                return true;
            }
        }
        return false;
    }

    private class ButtonToastListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View toastView = layoutInflater.inflate(R.layout.mytoastview, null);
            ((TextView)toastView.findViewById(R.id.toastTextView)).setText("It is locked !!!");
            Toast toast = new Toast(getApplicationContext());
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    private class ButtonShowPopup implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Popup popup = new Popup();
            popup.setDataExtractor(new FragmentDataExtractor<String>() {
                @Override
                public void provideData(String data) {
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            });
            popup.show(fragmentManager, "Show fragment");
        }
    }
    private class ButtonShowAlert implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setMessage("UFO just above you");
            alert.setTitle("UFO !!!!!");
            alert.setPositiveButton("Kill it", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"You have killed UFO", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("Do nothing", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"You have been killed", Toast.LENGTH_SHORT).show();
                }
            });
            alert.show();
        }
    }
    private class ButtonNotification implements View.OnClickListener {

        private int notId = 0;

        @Override
        public void onClick(View v) {
            /*
            // HARD WAY
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    MainActivity.this, String.valueOf(notId));
            builder.setContentTitle("A notification")
                    .setContentText("Here is a text of this notification")
                    .setSmallIcon(R.drawable.ic_search_black_24dp);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(notId, builder.build());
            }*/

            // EASY WAY
            CustomNotification customNotification = new CustomNotification();
            customNotification.notify(MainActivity.this, "It is raining", notId);
        }
    }
    private class ButtonBroadcast implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction("MyAction");
            intent.putExtra("message", "Hello World");
            sendBroadcast(intent); // every app can listen
        }
    }
    private class ButtonService implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            service();
        }
    }
    private class ButtonServiceData implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, String.valueOf(serviceUi.getRandomNumber()),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class ButtonShowMap implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
    }
}
