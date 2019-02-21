package de.unikassel.chefcoders.codecampkitchen.model;

import android.content.Context;
import android.support.annotation.StringRes;

import java.util.Arrays;

import de.unikassel.chefcoders.codecampkitchen.R;

public enum ItemKind
{
	WATER(R.string.water, "Wasser"),
	JUICE(R.string.juice, "Saft"),
	ICE(R.string.ice, "Eis"),
	COFFEE(R.string.coffee, "Kaffee"),
	LATTE(R.string.latte, "Milchkaffee"),
	ESPRESSO(R.string.espresso, "Espresso");

	private @StringRes int resourceId;
	private String value;

	ItemKind(@StringRes int id, String value) {
		this.resourceId = id;
		this.value = value;
	}

	public static Entry[] createEntries(Context context) {
		return Arrays.stream(ItemKind.values()).map(item -> item.new Entry(context)).toArray(ItemKind.Entry[]::new);
	}

	public static ItemKind getValue(String value) {
		for (ItemKind itemKind : ItemKind.values()) {
			if (itemKind.value.equals(value)) {
				return itemKind;
			}
		}
		return null;
	}

	public static int getIndex(String value) {
		ItemKind actualValue = ItemKind.getValue(value);

		if (actualValue == null) {
			return -1;
		}

		int index = 0;
		for (ItemKind itemKind : ItemKind.values()) {
			if (itemKind.value.equals(actualValue.value)) {
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
