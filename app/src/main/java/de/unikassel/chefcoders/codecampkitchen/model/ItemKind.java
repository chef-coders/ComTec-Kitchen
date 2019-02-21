package de.unikassel.chefcoders.codecampkitchen.model;

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

	public static int getIndex(String value) {
		int index = 0;
		ItemKind actualValue = ItemKind.valueOf(value);
		for (ItemKind itemKind : ItemKind.values()) {
			if (itemKind.equals(actualValue)) {
				return index;
			} else {
				index++;
			}
		}
		return -1;
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
