package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import java.beans.PropertyChangeListener;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PurchaseTest extends DataModelTest<Purchase>
{
	private static final int NUM_PROPS = 6;
	private static final String[] NULLABLE_PROPS = { Purchase.PROPERTY__id, Purchase.PROPERTY_created,
		Purchase.PROPERTY_user_id, Purchase.PROPERTY_item_id };

	@Override
	protected int getNumProps()
	{
		return NUM_PROPS;
	}

	@Override
	protected String[] getNullableProps()
	{
		return NULLABLE_PROPS;
	}

	@Override
	protected Purchase create()
	{
		return new Purchase();
	}

	@Override
	protected void setProps(Purchase purchase)
	{
		purchase.set_id("asd").setCreated("sometime").setUser_id("usr").setItem_id("itm").setPrice(1.2).setAmount(14);
	}

	@Override
	protected void setNulls(Purchase purchase)
	{
		purchase.set_id(null).setCreated(null).setUser_id(null).setItem_id(null);
	}

	@Override
	protected void addPropertyChangeListener(Purchase object, PropertyChangeListener listener)
	{
		object.addPropertyChangeListener(listener);
	}

	@Override
	protected void addPropertyChangeListener(Purchase object, String name, PropertyChangeListener listener)
	{
		object.addPropertyChangeListener(name, listener);
	}

	@Override
	protected void removePropertyChangeListener(Purchase object, PropertyChangeListener listener)
	{
		object.removePropertyChangeListener(listener);
	}

	@Override
	protected void removePropertyChangeListener(Purchase object, String name, PropertyChangeListener listener)
	{
		object.removePropertyChangeListener(name, listener);
	}

	@Test
	public void setAndGet()
	{
		final Purchase purchase = new Purchase();
		this.setProps(purchase);

		assertThat(purchase.get_id(), is("asd"));
		assertThat(purchase.getCreated(), is("sometime"));
		assertThat(purchase.getUser_id(), is("usr"));
		assertThat(purchase.getItem_id(), is("itm"));
		assertThat(purchase.getPrice(), is(1.2));
		assertThat(purchase.getAmount(), is(14));
	}

	@Test
	public void _toString()
	{
		final Purchase purchase = new Purchase();
		this.setProps(purchase);
		assertThat(purchase, hasToString("asd sometime usr itm"));
	}

	@Test
	public void removeYou()
	{
		new Purchase().removeYou();
	}
}
