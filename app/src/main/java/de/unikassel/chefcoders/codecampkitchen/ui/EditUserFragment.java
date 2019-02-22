package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class EditUserFragment extends KitchenFragment
{
	// =============== Fields ===============

	private String userId;

	private EditText editTextEmail;
	private EditText editTextName;
	private EditText editTextCredit;

	// =============== Static Methods ===============

	public static EditUserFragment newInstance(String userId)
	{
		EditUserFragment fragment = new EditUserFragment();
		Bundle bundle = new Bundle();
		bundle.putString("userId", userId);
		fragment.setArguments(bundle);
		return fragment;
	}

	// =============== Methods ===============

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.edit_user);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
		this.init(view);
		return view;
	}

	private void init(View view)
	{
		this.userId = this.getArguments().getString("userId");

		this.editTextEmail = view.findViewById(R.id.editTextEmail);
		this.editTextName = view.findViewById(R.id.editTextName);
		this.editTextCredit = view.findViewById(R.id.editTextCredit);

		final FloatingActionButton floatingActionButton = view.findViewById(R.id.buttonSave);

		final User user = MainActivity.kitchenManager.getUserById(this.userId);
		if (user != null)
		{
			this.editTextName.setText(user.getName());
			this.editTextEmail.setText(user.getMail());
			this.editTextCredit.setText(String.valueOf(user.getCredit()));
		}

		floatingActionButton.setOnClickListener(this::onSaveClicked);
	}

	private void onSaveClicked(View v)
	{
		final String name = this.editTextName.getText().toString();
		final String email = this.editTextEmail.getText().toString();
		final double credit = Double.valueOf(this.editTextCredit.getText().toString());
		final User user = new User().set_id(this.userId).setName(name).setMail(email).setCredit(credit);

		final FragmentActivity activity = this.getActivity();

		SimpleAsyncTask.execute(activity, () -> {
			MainActivity.kitchenManager.updateUser(user);
		}, () -> {
			Toast.makeText(activity, R.string.edit_user_successful, Toast.LENGTH_SHORT).show();
			final MainActivity mainActivity = (MainActivity) activity;
			if (mainActivity != null)
			{
				mainActivity.changeFragmentBack(new AllUserFragment());
			}
		});
	}
}
