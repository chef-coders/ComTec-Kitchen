package de.unikassel.chefcoders.codecampkitchen.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Item
{

	public static final String PROPERTY__id = "_id";

	private String _id;

	public String get_id()
	{
		return this._id;
	}

	public Item set_id(String value)
	{
		if (value == null ? this._id != null : !value.equals(this._id))
		{
			String oldValue = this._id;
			this._id = value;
			this.firePropertyChange("_id", oldValue, value);
		}
		return this;
	}

	public static final String PROPERTY_name = "name";

	private String name;

	public String getName()
	{
		return this.name;
	}

	public Item setName(String value)
	{
		if (value == null ? this.name != null : !value.equals(this.name))
		{
			String oldValue = this.name;
			this.name = value;
			this.firePropertyChange("name", oldValue, value);
		}
		return this;
	}

	public static final String PROPERTY_price = "price";

	private double price;

	public double getPrice()
	{
		return this.price;
	}

	public Item setPrice(double value)
	{
		if (value != this.price)
		{
			double oldValue = this.price;
			this.price = value;
			this.firePropertyChange("price", oldValue, value);
		}
		return this;
	}

	public static final String PROPERTY_amount = "amount";

	private int amount;

	public int getAmount()
	{
		return this.amount;
	}

	public Item setAmount(int value)
	{
		if (value != this.amount)
		{
			int oldValue = this.amount;
			this.amount = value;
			this.firePropertyChange("amount", oldValue, value);
		}
		return this;
	}

	public static final String PROPERTY_kind = "kind";

	private String kind;

	public String getKind()
	{
		return this.kind;
	}

	public Item setKind(String value)
	{
		if (value == null ? this.kind != null : !value.equals(this.kind))
		{
			String oldValue = this.kind;
			this.kind = value;
			this.firePropertyChange("kind", oldValue, value);
		}
		return this;
	}

	protected PropertyChangeSupport listeners = null;

	public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
		if (this.listeners != null)
		{
			this.listeners.firePropertyChange(propertyName, oldValue, newValue);
			return true;
		}
		return false;
	}

	public boolean addPropertyChangeListener(PropertyChangeListener listener)
	{
		if (this.listeners == null)
		{
			this.listeners = new PropertyChangeSupport(this);
		}
		this.listeners.addPropertyChangeListener(listener);
		return true;
	}

	public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		if (this.listeners == null)
		{
			this.listeners = new PropertyChangeSupport(this);
		}
		this.listeners.addPropertyChangeListener(propertyName, listener);
		return true;
	}

	public boolean removePropertyChangeListener(PropertyChangeListener listener)
	{
		if (this.listeners != null)
		{
			this.listeners.removePropertyChangeListener(listener);
		}
		return true;
	}

	public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		if (this.listeners != null)
		{
			this.listeners.removePropertyChangeListener(propertyName, listener);
		}
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();

		result.append(" ").append(this.get_id());
		result.append(" ").append(this.getName());
		result.append(" ").append(this.getKind());

		return result.substring(1);
	}

	public void removeYou()
	{
	}
}
