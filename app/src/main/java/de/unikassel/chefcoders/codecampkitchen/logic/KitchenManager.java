package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.Database;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;

import java.util.ArrayList;
import java.util.List;

public class KitchenManager
{
	// =============== Fields ===============

	private final Database          database;
	private final KitchenConnection connection;

	// =============== Constructor ===============

	public KitchenManager(Database database, KitchenConnection connection)
	{
		this.database = database;
		this.connection = connection;
	}

	// =============== Static Methods ===============

	public static KitchenManager create()
	{
		return new KitchenManager(new Database(), new KitchenConnection(new SyncHttpConnection()));
	}

	// =============== Methods ===============

	// --------------- Items ---------------

	public List<Item> getItems()
	{
		return new ArrayList<>(this.database.getItems());
	}

	public void refreshItems()
	{
		for (Item item : JsonTranslator.toItems(connection.getAllItems()))
		{
			this.database.addItem(item);
		}
	}
}
