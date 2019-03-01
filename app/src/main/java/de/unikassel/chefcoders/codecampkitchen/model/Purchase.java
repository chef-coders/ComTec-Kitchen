package de.unikassel.chefcoders.codecampkitchen.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Purchase
{

	public static final String PROPERTY__id = "_id";

	private String _id;

	public String get_id()
	{
		return this._id;
	}

	public Purchase set_id(String value)
	{
		if (value == null ? this._id != null : !value.equals(this._id))
		{
			String oldValue = this._id;
			this._id = value;
			this.firePropertyChange("_id", oldValue, value);
		}
		return this;
	}

	public static final String PROPERTY_created = "created";

	private String created;

	public String getCreated()
	{
		return this.created;
	}

	public Purchase setCreated(String value)
	{
		if (value == null ? this.created != null : !value.equals(this.created))
		{
			String oldValue = this.created;
			this.created = value;
			this.firePropertyChange("created", oldValue, value);
		}
		return this;
	}

	public static final String PROPERTY_amount = "amount";

	private int amount;

	public int getAmount()
	{
		return this.amount;
	}

	public Purchase setAmount(int value)
	{
		if (value != this.amount)
		{
			int oldValue = this.amount;
			this.amount = value;
			this.firePropertyChange("amount", oldValue, value);
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

	public void removeYou()
	{
	}

	public static final String PROPERTY_user_id = "user_id";

	private String user_id;

	public String getUser_id()
	{
		return this.user_id;
	}

	public Purchase setUser_id(String value)
	{
		if (value == null ? this.user_id != null : !value.equals(this.user_id))
		{
			String oldValue = this.user_id;
			this.user_id = value;
			this.firePropertyChange("user_id", oldValue, value);
		}
		return this;
	}

	public static final String PROPERTY_item_id = "item_id";

	private String item_id;

	public String getItem_id()
	{
		return this.item_id;
	}

	public Purchase setItem_id(String value)
	{
		if (value == null ? this.item_id != null : !value.equals(this.item_id))
		{
			String oldValue = this.item_id;
			this.item_id = value;
			this.firePropertyChange("item_id", oldValue, value);
		}
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();

		result.append(" ").append(this.get_id());
		result.append(" ").append(this.getCreated());
		result.append(" ").append(this.getUser_id());
		result.append(" ").append(this.getItem_id());

		return result.substring(1);
	}

	public static final String PROPERTY_price = "price";

	private double price;

	public double getPrice()
	{
		return this.price;
	}

	public Purchase setPrice(double value)
	{
		if (value != this.price)
		{
			double oldValue = this.price;
			this.price = value;
			this.firePropertyChange("price", oldValue, value);
		}
		return this;
	}
}
