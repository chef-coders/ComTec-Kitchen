package de.unikassel.chefcoders.codecampkitchen.ui.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.Users;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;

public class EditUserFragment extends KitchenFragment
{
	// =============== Fields ===============

	private String userId;

	private EditText editTextEmail;
	private EditText editTextName;
	private EditText editTextCredit;

	public EditUserFragment()
	{
		super(R.string.edit_user, true);
	}

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
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
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

		DisableButtonTextWatcher.bind(this.saveButton, this.editTextEmail, this.editTextName, this.editTextCredit);

		final User user = Users.shared.get(this.userId);
		if (user != null)
		{
			this.editTextName.setText(user.getName());
			this.editTextEmail.setText(user.getMail());
			this.editTextCredit.setText(String.valueOf(user.getCredit()));
		}
	}

	private User getUser()
	{
		final String name = this.editTextName.getText().toString();
		final String email = this.editTextEmail.getText().toString();

		final double credit;
		try
		{
			credit = Double.valueOf(this.editTextCredit.getText().toString());
		}
		catch (NumberFormatException e)
		{
			this.editTextCredit.setText("0.0");
			return null;
		}

		return new User().set_id(this.userId).setName(name).setMail(email).setCredit(credit);
	}

	@Override
	protected void handleClickedOnSave(View v)
	{
		User user = this.getUser();
		if (user == null)
		{
			return;
		}

		final FragmentActivity activity = this.getActivity();

		SimpleAsyncTask.execute(activity, () -> {
			Users.shared.update(user);
		}, () -> {
			Toast.makeText(activity, R.string.edit_user_successful, Toast.LENGTH_SHORT).show();
			final MainActivity mainActivity = (MainActivity) activity;
			if (mainActivity != null)
			{
				mainActivity.changeFragmentBack();
			}
		});
	}
}
