package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public abstract class DataModelTest<T>
{
	protected abstract int getNumProps();

	protected abstract String[] getNullableProps();

	protected abstract T create();

	protected abstract void setProps(T object);

	protected abstract void setNulls(T object);

	protected abstract void addPropertyChangeListener(T object, PropertyChangeListener listener);

	protected abstract void addPropertyChangeListener(T object, String name, PropertyChangeListener listener);

	protected abstract void removePropertyChangeListener(T object, PropertyChangeListener listener);

	protected abstract void removePropertyChangeListener(T object, String name, PropertyChangeListener listener);

	@Test
	public void addPropertyChangeListener()
	{
		final AtomicInteger counter = new AtomicInteger();
		final PropertyChangeListener listener = evt -> counter.incrementAndGet();

		final T item = this.create();
		final int numProps = this.getNumProps();
		final String[] nullableProps = this.getNullableProps();

		this.addPropertyChangeListener(item, listener);

		this.setProps(item);
		assertThat(counter.get(), is(numProps));

		this.setProps(item);
		assertThat(counter.get(), is(numProps));

		for (String nullableProp : nullableProps)
		{
			this.addPropertyChangeListener(item, nullableProp, listener);
		}

		counter.set(0);
		this.setNulls(item);
		this.setNulls(item);
		this.setProps(item);
		this.setProps(item);
		assertThat(counter.get(), is(nullableProps.length * 4));
	}

	@Test
	public void removePropertyChangeListener()
	{
		final AtomicInteger counter = new AtomicInteger();
		final PropertyChangeListener listener = evt -> counter.incrementAndGet();

		final T item = this.create();
		final String[] nullableProps = this.getNullableProps();
		final String nullableProp = nullableProps[0];
		final int numProps = this.getNumProps();

		this.removePropertyChangeListener(item, nullableProp, listener);
		this.removePropertyChangeListener(item, listener);

		this.addPropertyChangeListener(item, nullableProp, listener);
		this.addPropertyChangeListener(item, listener);

		counter.set(0);
		this.setProps(item);
		assertThat(counter.get(), is(numProps + 1));

		this.removePropertyChangeListener(item, nullableProp, listener);
		this.removePropertyChangeListener(item, listener);

		counter.set(0);
		this.setNulls(item);
		assertThat(counter.get(), is(0));
	}
}
