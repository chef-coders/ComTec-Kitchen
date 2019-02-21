package de.unikassel.chefcoders.codecampkitchen.ui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserFragment extends KitchenFragment
{

	private EditText editTextEmail;
	private EditText editTextName;
	private FloatingActionButton floatingActionButton;

	public EditUserFragment()
	{
		// Required empty public constructor
	}

	public static EditUserFragment newInstance(String userId)
	{
		EditUserFragment fragment = new EditUserFragment();
		Bundle bundle = new Bundle();
		bundle.putString("userId", userId);
		fragment.setArguments(bundle);
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
		init(view);
		return view;
	}

	private void init(View view)
	{
		String id = getArguments().getString("userId");

		ResultAsyncTask.execute(getActivity(), () -> MainActivity.kitchenManager.getUserById(id),
				user ->
				{
					editTextEmail = view.findViewById(R.id.editTextEmail);
					editTextName = view.findViewById(R.id.editTextName);

					floatingActionButton = view.findViewById(R.id.buttonSave);
					floatingActionButton.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							String name = editTextName.getText().toString();
							String email = editTextEmail.getText().toString();


						}
					});
				});


	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.edit);
	}
}
