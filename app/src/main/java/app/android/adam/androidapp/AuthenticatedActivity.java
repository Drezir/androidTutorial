package app.android.adam.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import app.android.adam.androidapp.user.User;

public class AuthenticatedActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (!User.INSTANCE.isLoggedIn()) {
            final Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
