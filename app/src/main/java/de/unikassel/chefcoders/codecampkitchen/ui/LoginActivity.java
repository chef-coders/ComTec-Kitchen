package de.unikassel.chefcoders.codecampkitchen.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.edit.DisableButtonTextWatcher;

public class LoginActivity extends AppCompatActivity
{

	private EditText    editTextName;
	private EditText    editTextEmail;
	private Switch      switchAdmin;
	private Button      buttonLogin;
	private TextView    textViewConnection;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        this.editTextName = this.findViewById(R.id.editTextName);
        this.editTextEmail = this.findViewById(R.id.editTextEmail);
        this.switchAdmin = this.findViewById(R.id.switchAdmin);
        this.buttonLogin = this.findViewById(R.id.buttonLogin);
		DisableButtonTextWatcher.bind(this.buttonLogin, this.editTextEmail, this.editTextName);
        this.textViewConnection = this.findViewById(R.id.textViewConnection);
        this.progressBar = this.findViewById(R.id.progressBar);
		if (!this.isConnected())
		{
            this.textViewConnection.setText(getString(R.string.connection_request));
		}
	}

	public void loginClick(View v)
	{
		if (!this.isConnected())
		{
            this.textViewConnection.setText(getString(R.string.connection_request));
			final Animation shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
			this.textViewConnection.startAnimation(shakeAnim);
			return;
		}

        this.setButtonEnabled(false);
        this.textViewConnection.setText("");
		this.progressBar.setVisibility(View.VISIBLE);

		SimpleAsyncTask.execute(this.getApplicationContext(), () -> {
			String name = this.editTextName.getText().toString();
			String email = this.editTextEmail.getText().toString();

			MainActivity.kitchenManager.session().register(this, name, email, this.switchAdmin.isChecked());
		}, this::startMainActivity, () -> {
			this.setButtonEnabled(true);
			this.progressBar.setVisibility(View.GONE);
		});
	}

	private void setButtonEnabled(boolean enable)
	{
        this.buttonLogin.setClickable(enable);
        this.buttonLogin.setActivated(enable);
        this.buttonLogin.setEnabled(enable);
	}

	private void startMainActivity()
	{
        this.finish();
        this.startActivity(new Intent(this, MainActivity.class));
	}

	private boolean isConnected()
	{
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		return activeNetwork != null && activeNetwork.isConnected();
	}
}
