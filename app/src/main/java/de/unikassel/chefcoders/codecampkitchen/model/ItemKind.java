package de.unikassel.chefcoders.codecampkitchen.model;

import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;

import java.util.Arrays;

import de.unikassel.chefcoders.codecampkitchen.R;

public enum ItemKind
{
	WATER(R.string.water),
	JUICE(R.string.juice),
	ICE(R.string.ice),
	COFFEE(R.string.coffee),
	LATTE(R.string.latte),
	ESPRESSO(R.string.espresso);

	private @StringRes int resourceId;

	ItemKind(@StringRes int id){
		this.resourceId = id;
	}

	public static Entry[] createEntries(Context context) {
		return Arrays.stream(ItemKind.values()).map(item -> item.new Entry(context)).toArray(ItemKind.Entry[]::new);
	}

	class Entry {
		private Context context;

		Entry(Context context) {
			this.context = context;
		}

		@Override
		public String toString() {
			return context.getString(resourceId);
		}
	}
}
