package app.android.adam.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import app.android.adam.androidapp.user.Credentials;
import app.android.adam.androidapp.user.User;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;

    private static final String USERNAME_KEY = "usernameKey";
    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new LoginClickListener());

        loadPreferences();
        focusPasswordIfUsernameIsSet();
    }

    private void loadPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        }
        String username = sharedPreferences.getString(USERNAME_KEY, "");
        this.username.setText(username);
    }
    private void focusPasswordIfUsernameIsSet() {
        if (!username.getText().toString().isEmpty()) {
            this.password.requestFocus();
        }
    }
    private void savePreferences() {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(USERNAME_KEY, username.getText().toString());
        sharedPreferencesEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (User.INSTANCE.isLoggedIn()) {
            redirectToMain();
        }
    }

    private void redirectToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class LoginClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Credentials credentials = new Credentials(
                    username.getText().toString(),
                    password.getText().toString());
            if (User.INSTANCE.login(credentials)) {
                savePreferences();
                redirectToMain();
            } else {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                v.startAnimation(animation);
            }
        }
    }
}
