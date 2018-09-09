package app.android.adam.androidapp;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import app.android.adam.androidapp.interfaces.FragmentDataExtractor;
import app.android.adam.androidapp.shopitems.ShopItemsActivity;
import app.android.adam.androidapp.user.User;

public class MainActivity extends AuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mainToastButton).setOnClickListener(new ButtonToastListener());
        findViewById(R.id.mainListButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShopItemsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.mainPopupButton).setOnClickListener(new ButtonShowPopup());
        findViewById(R.id.mainAlertButton).setOnClickListener(new ButtonShowAlert());
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
}
