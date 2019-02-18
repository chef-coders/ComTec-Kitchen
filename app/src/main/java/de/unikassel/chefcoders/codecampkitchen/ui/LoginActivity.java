package de.unikassel.chefcoders.codecampkitchen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

public class LoginActivity extends AppCompatActivity
{

    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
    }


    public void loginClick(View v)
    {
        progressBar.setVisibility(View.VISIBLE);
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

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
}
