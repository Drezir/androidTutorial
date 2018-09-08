package app.android.adam.androidapp;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new LoginClickListener());
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
                redirectToMain();
            } else {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                v.startAnimation(animation);
            }
        }
    }
}
