package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;

import java.util.*;

public class Items
{
	// =============== Fields ===============

	private final KitchenManager kitchenManager;

	private final Map<String, Item> items = new HashMap<>();

	// =============== Constructors ===============

	public Items(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	// --------------- Access ---------------

	public Item get(String id)
	{
		return this.items.get(id);
	}

	public boolean exists(String id)
	{
		return this.items.containsKey(id);
	}

	public List<Item> getAll()
	{
		return new ArrayList<>(this.items.values());
	}

	public Map<String, List<Item>> getGrouped()
	{
		return KitchenManager.group(this.items.values(), Item::getKind,
		                            Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER));
	}

	// --------------- Modification ---------------

	public void updateLocal(Item item)
	{
		this.items.put(item.get_id(), item);
	}

	public void deleteLocal(String id)
	{
		this.items.remove(id);
	}

	// --------------- Communication ---------------

	public void refreshAll()
	{
		this.items.clear();
		final String resultJson = this.kitchenManager.getConnection().getAllItems();
		final List<Item> resultItems = JsonTranslator.toItems(resultJson);
		resultItems.forEach(this::updateLocal);
	}

	public void create(Item item)
	{
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson = this.kitchenManager.getConnection().createItem(itemJson);
		final Item createdItem = JsonTranslator.toItem(resultJson);
		this.updateLocal(createdItem);
	}

	@Deprecated
	public void createItem(String id, String name, double price, int amount, String kind)
	{
		if (!this.kitchenManager.session().isAdmin())
		{
			return;
		}

		this.create(new Item().set_id(id).setName(name).setPrice(price).setAmount(amount).setKind(kind));
	}

	public void update(Item item)
	{
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson = this.kitchenManager.getConnection().updateItem(item.get_id(), itemJson);
		final Item updatedItem = JsonTranslator.toItem(resultJson);
		this.updateLocal(updatedItem);
	}

	@Deprecated
	public void updateItem(String id, String name, double price, int amount, String kind)
	{
		if (!this.kitchenManager.session().isAdmin())
		{
			return;
		}

		this.update(new Item().set_id(id).setName(name).setPrice(price).setAmount(amount).setKind(kind));
	}

	public void delete(String id)
	{
		if (!this.kitchenManager.session().isAdmin())
		{
			return;
		}

		this.kitchenManager.getConnection().deleteItem(id);
		this.deleteLocal(id);
	}
}
