package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ItemTest
{
	private void setProps(Item item)
	{
		item.set_id("asd").setName("Item").setPrice(1.2).setAmount(14).setKind("water");
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
	public void addPropertyChangeListener()
	{
		final AtomicInteger counter = new AtomicInteger();
		final Item item = new Item();
		final PropertyChangeListener listener = evt -> counter.incrementAndGet();

		item.addPropertyChangeListener(listener);

		this.setProps(item);
		assertThat(counter.get(), is(5));

		this.setProps(item);
		assertThat(counter.get(), is(5));

		item.addPropertyChangeListener(Item.PROPERTY__id, listener);
		item.addPropertyChangeListener(Item.PROPERTY_name, listener);
		item.addPropertyChangeListener(Item.PROPERTY_kind, listener);

		counter.set(0);
		item.set_id(null);
		item.setName(null);
		item.setKind(null);
		item.set_id(null);
		item.setName(null);
		item.setKind(null);
		item.set_id("id");
		item.setName("name");
		item.setKind("kind");
		item.set_id("id");
		item.setName("name");
		item.setKind("kind");
		assertThat(counter.get(), is(12));
	}

	@Test
	public void removePropertyChangeListener()
	{
		final AtomicInteger counter = new AtomicInteger();
		final PropertyChangeListener listener = evt -> counter.incrementAndGet();
		final Item item = new Item();

		item.removePropertyChangeListener(Item.PROPERTY_name, listener);
		item.removePropertyChangeListener(listener);

		item.addPropertyChangeListener(Item.PROPERTY_name, listener);
		item.addPropertyChangeListener(listener);

		counter.set(0);
		item.setName("item");
		assertThat(counter.get(), is(2));

		item.removePropertyChangeListener(Item.PROPERTY_name, listener);
		item.removePropertyChangeListener(listener);

		counter.set(0);
		item.setName(null);
		assertThat(counter.get(), is(0));
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
