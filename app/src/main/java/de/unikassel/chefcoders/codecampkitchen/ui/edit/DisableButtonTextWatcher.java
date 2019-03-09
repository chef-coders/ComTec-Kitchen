package de.unikassel.chefcoders.codecampkitchen.ui.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import java.util.function.BooleanSupplier;

public class DisableButtonTextWatcher implements TextWatcher
{
	// =============== Fields ===============

	private final Button          button;
	private final BooleanSupplier isValid;

	// =============== Constructors ===============

	private DisableButtonTextWatcher(Button button, BooleanSupplier isValid)
	{
		this.button = button;
		this.isValid = isValid;
	}

	// =============== Static Methods ===============

	public static void bind(Button button, TextView... textViews)
	{
		bind(button, () -> {
			for (TextView t : textViews)
			{
				if (t.getText().length() == 0)
				{
					return false;
				}
			}
			return true;
		}, textViews);
	}

	public static void bind(Button button, BooleanSupplier isValid, TextView... textViews)
	{
		final DisableButtonTextWatcher textWatcher = new DisableButtonTextWatcher(button, isValid);
		for (TextView textView : textViews)
		{
			textView.addTextChangedListener(textWatcher);
		}
	}

	// =============== Methods ===============

	@Override
	public void afterTextChanged(Editable editable)
	{
		if (!this.isValid.getAsBoolean())
		{
			this.button.setEnabled(false);
			this.button.setAlpha(0.5f);
		}
		else
		{
			this.button.setEnabled(true);
			this.button.setAlpha(1f);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{
	}
}
