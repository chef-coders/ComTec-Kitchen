package de.unikassel.chefcoders.codecampkitchen.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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

	public static SimpleDialog createDialog(
			@StringRes int title,
			ConfirmClick confirmClick)
	{
		return createDialog(title, 0,
				confirmClick);
	}


	public static SimpleDialog createDialog(
			@StringRes int title,
			@StringRes int message,
			ConfirmClick confirmClick)
	{
		return createDialog(title, message, R.string.yes, R.string.no,
				confirmClick);
	}

	public static SimpleDialog createDialog(
			@StringRes int title,
			@StringRes int message,
			@StringRes int positive,
			@StringRes int negative,
			ConfirmClick confirmClick)
	{
		SimpleDialog simpleDialog = new SimpleDialog();
		simpleDialog.setConfirmClick(confirmClick);
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		bundle.putInt("message", message);
		bundle.putInt("positive", positive);
		bundle.putInt("negative", negative);
		simpleDialog.setArguments(bundle);
		return simpleDialog;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
	{
		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");
		int positive = getArguments().getInt("positive");
		int negative = getArguments().getInt("negative");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		if (message != 0) {
			builder.setMessage(message);
		}
		builder
				.setPositiveButton(positive, (dialog, id) -> confirmClick.confirmPositive())
				.setNegativeButton(negative, (dialog, id) -> confirmClick.confirmNegative());
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void setConfirmClick(ConfirmClick confirmClick)
	{
		this.confirmClick = confirmClick;
	}
}
