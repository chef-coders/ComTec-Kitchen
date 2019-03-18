package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import java.beans.PropertyChangeListener;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ItemTest extends DataModelTest<Item>
{
	private static final int      NUM_PROPS      = 5;
	private static final String[] NULLABLE_PROPS = { Item.PROPERTY__id, Item.PROPERTY_name, Item.PROPERTY_kind };

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
	protected Item create()
	{
		return new Item();
	}

	@Override
	protected void setProps(Item item)
	{
		item.set_id("asd").setName("Item").setPrice(1.2).setAmount(14).setKind("water");
	}

	@Override
	protected void setNulls(Item item)
	{
		item.set_id(null).setName(null).setKind(null);
	}

	@Override
	protected void addPropertyChangeListener(Item object, PropertyChangeListener listener)
	{
		object.addPropertyChangeListener(listener);
	}

	@Override
	protected void addPropertyChangeListener(Item object, String name, PropertyChangeListener listener)
	{
		object.addPropertyChangeListener(name, listener);
	}

	@Override
	protected void removePropertyChangeListener(Item object, PropertyChangeListener listener)
	{
		object.removePropertyChangeListener(listener);
	}

	@Override
	protected void removePropertyChangeListener(Item object, String name, PropertyChangeListener listener)
	{
		object.removePropertyChangeListener(name, listener);
	}

	@Test
	public void setAndGet()
	{
		final Item item = new Item();
		this.setProps(item);

		assertThat(item.get_id(), is("asd"));
		assertThat(item.getName(), is("Item"));
		assertThat(item.getPrice(), is(1.2));
		assertThat(item.getAmount(), is(14));
		assertThat(item.getKind(), is("water"));
	}

	@Test
	public void _toString()
	{
		final Item item = new Item();
		this.setProps(item);
		assertThat(item, hasToString("asd Item water"));
	}

	@Test
	public void removeYou()
	{
		new Item().removeYou();
	}
}
