package de.unikassel.chefcoders.codecampkitchen.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import de.unikassel.chefcoders.codecampkitchen.R;

public class SimpleDialog extends DialogFragment
{

	public interface ConfirmClick
	{
		void confirmPositive();

		void confirmNegative();
	}

	private ConfirmClick confirmClick;

	public SimpleDialog()
	{
	}

	public static SimpleDialog createDialog(Context context, String title, ConfirmClick confirmClick)
	{
		return createDialog(context, title, "", confirmClick);
	}

	public static SimpleDialog createDialog(Context context, String title, String message, ConfirmClick confirmClick)
	{
		return createDialog(title, message, context.getString(R.string.yes), context.getString(R.string.no),
		                    confirmClick);
	}

	public static SimpleDialog createDialog(String title, String message, String positive, String negative,
		ConfirmClick confirmClick)
	{
		SimpleDialog simpleDialog = new SimpleDialog();
		simpleDialog.setConfirmClick(confirmClick);
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("message", message);
		bundle.putString("positive", positive);
		bundle.putString("negative", negative);
		simpleDialog.setArguments(bundle);
		return simpleDialog;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
	{
		String title = this.getArguments().getString("title");
		String message = this.getArguments().getString("message");
		String positive = this.getArguments().getString("positive");
		String negative = this.getArguments().getString("negative");

		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle(title);
		if (!message.isEmpty())
		{
			builder.setMessage(message);
		}
		builder.setPositiveButton(positive, (dialog, id) -> this.confirmClick.confirmPositive())
		       .setNegativeButton(negative, (dialog, id) -> this.confirmClick.confirmNegative());
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void setConfirmClick(ConfirmClick confirmClick)
	{
		this.confirmClick = confirmClick;
	}
}
