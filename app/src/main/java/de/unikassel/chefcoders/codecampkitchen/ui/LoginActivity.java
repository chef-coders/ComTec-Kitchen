package de.unikassel.chefcoders.codecampkitchen.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class LoginActivity extends AppCompatActivity
{

    private EditText editTextName;
    private EditText editTextEmail;
    private Switch switchAdmin;
    private Button buttonLogin;
    private TextView textViewConnection;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        switchAdmin = findViewById(R.id.switchAdmin);
        buttonLogin = findViewById(R.id.buttonLogin);
        DisableButtonTextWatcher.bind(this.buttonLogin, this.editTextEmail, this.editTextName);
        textViewConnection = findViewById(R.id.textViewConnection);
        progressBar = findViewById(R.id.progressBar);
        if (!isConnected()) {
            textViewConnection.setText(getString(R.string.connection_request));
            disableButton();
        }
    }


    public void loginClick(View v)
    {
        progressBar.setVisibility(View.VISIBLE);
        disableButton();

        SimpleAsyncTask.execute(this.getApplicationContext(), () -> {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();

	        MainActivity.kitchenManager.session().register(this, name, email, switchAdmin.isChecked());
        }, this::startMainActivity);
    }


    private void disableButton()
    {
        buttonLogin.setClickable(false);
        buttonLogin.setActivated(false);
        buttonLogin.setEnabled(false);
    }

    private void enableButton()
    {
        buttonLogin.setClickable(true);
        buttonLogin.setActivated(true);
        buttonLogin.setEnabled(true);
    }

    private void startMainActivity()
    {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnected();
    }
}
