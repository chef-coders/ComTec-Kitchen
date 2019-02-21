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
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.edit);
	}
}
