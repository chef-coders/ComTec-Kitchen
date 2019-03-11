package de.unikassel.chefcoders.codecampkitchen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
		this.setContentView(R.layout.activity_login);

		this.editTextName = this.findViewById(R.id.editTextName);
		this.editTextEmail = this.findViewById(R.id.editTextEmail);
		this.switchAdmin = this.findViewById(R.id.switchAdmin);
		this.textViewConnection = this.findViewById(R.id.textViewConnection);
		this.progressBar = this.findViewById(R.id.progressBar);
		this.buttonLogin = this.findViewById(R.id.buttonLogin);

		DisableButtonTextWatcher.bind(this.buttonLogin, this.editTextEmail, this.editTextName);
	}

	public void loginClick(View v)
	{
		this.buttonLogin.setEnabled(false);
		this.textViewConnection.setText("");
		this.progressBar.setVisibility(View.VISIBLE);

		SimpleAsyncTask.execute(() -> {
			String name = this.editTextName.getText().toString();
			String email = this.editTextEmail.getText().toString();

			MainActivity.kitchenManager.session().register(this, name, email, this.switchAdmin.isChecked());
		}, () -> {
			// signup successful, show Main view

			this.finish();
			this.startActivity(new Intent(this, MainActivity.class));
		}, exception -> {
			// signup failed, display and shake warning

			this.textViewConnection.setText(getString(R.string.connection_request));
			this.textViewConnection.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
		}, () -> {
			// in either case, re-enable the button to allow "try again"

			this.buttonLogin.setEnabled(true);
			this.progressBar.setVisibility(View.GONE);
		});
	}
}
