package de.unikassel.chefcoders.codecampkitchen.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import de.unikassel.chefcoders.codecampkitchen.R;

public class SimpleDialog extends DialogFragment
{
	// =============== Fields ===============

	protected DialogInterface.OnClickListener yesClickListener;
	protected DialogInterface.OnClickListener noClickListener;

	// =============== Constructors ===============

	public SimpleDialog()
	{
	}

	// =============== Static Methods ===============

	public static Builder builder()
	{
		return new Builder();
	}

	// =============== Classes ===============

	public static class Builder
	{
		private final SimpleDialog simpleDialog;
		private final Bundle       bundle;

		public Builder()
		{
			this.simpleDialog = new SimpleDialog();
			this.bundle = new Bundle();
		}

		public Builder title(String title)
		{
			this.bundle.putString("title", title);
			return this;
		}

		public Builder message(String message)
		{
			this.bundle.putString("message", message);
			return this;
		}

		public Builder yesButton(String yesButtonLabel, DialogInterface.OnClickListener clickListener)
		{
			this.bundle.putString("positive", yesButtonLabel);
			this.simpleDialog.yesClickListener = clickListener;
			return this;
		}

		public Builder noButton(String noButtonLabel, DialogInterface.OnClickListener clickListener)
		{
			this.bundle.putString("negative", noButtonLabel);
			this.simpleDialog.noClickListener = clickListener;
			return this;
		}

		public DialogFragment build(Context context)
		{
			if (!this.bundle.containsKey("positive"))
			{
				this.bundle.putString("positive", context.getString(R.string.yes));
			}

			if (!this.bundle.containsKey("negative"))
			{
				this.bundle.putString("negative", context.getString(R.string.no));
			}

			this.simpleDialog.setArguments(this.bundle);
			return this.simpleDialog;
		}
	}

	// =============== Methods ===============

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
		if (message != null && !message.isEmpty())
		{
			builder.setMessage(message);
		}
		builder.setPositiveButton(positive, this.yesClickListener);
		builder.setNegativeButton(negative, this.noClickListener);
		return builder.create();
	}
}
