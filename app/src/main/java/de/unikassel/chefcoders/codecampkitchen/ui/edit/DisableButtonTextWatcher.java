package de.unikassel.chefcoders.codecampkitchen.ui.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

public class DisableButtonTextWatcher implements TextWatcher
{
	private Button button;
	private TextView[] relatedTextViews;

	public DisableButtonTextWatcher(Button button, TextView...relatedTextViews)
	{
		this.button = button;
		this.relatedTextViews = relatedTextViews;
	}

	public static void bind(Button button, TextView...textViews)
	{
		for(TextView textView : textViews)
		{
			textView.addTextChangedListener(new DisableButtonTextWatcher(button, textViews));
		}
	}

	@Override
	public void afterTextChanged(Editable editable)
	{
		if(editable.length() == 0)
		{
			this.button.setEnabled(false);
			this.button.setAlpha(0.5f);
		}
		else
		{
			for(TextView textView : this.relatedTextViews)
			{
				if(textView.getText().length() == 0)
				{
					return;
				}
			}

			this.button.setEnabled(true);
			this.button.setAlpha(1f);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
}
