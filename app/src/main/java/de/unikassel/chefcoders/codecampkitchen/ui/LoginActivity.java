package de.unikassel.chefcoders.codecampkitchen.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.unikassel.chefcoders.codecampkitchen.R;

public class LoginActivity extends AppCompatActivity
{

    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LoginTheme);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonLogin = findViewById(R.id.buttonLogin);
    }


    public void loginClick(View v)
    {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

    }
}
