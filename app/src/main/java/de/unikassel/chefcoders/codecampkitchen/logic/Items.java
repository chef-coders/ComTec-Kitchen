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
		return KitchenManager.group(this.items.values().stream(), Item::getKind,
		                            Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER));
	}

	// --------------- Modification ---------------

	public void updateLocal(Item item)
	{
		this.items.put(item.get_id(), item);
	}

	public void deleteLocal(Item item)
	{
		this.items.remove(item.get_id());
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

	public void update(Item item)
	{
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson = this.kitchenManager.getConnection().updateItem(item.get_id(), itemJson);
		final Item updatedItem = JsonTranslator.toItem(resultJson);
		this.updateLocal(updatedItem);
	}

	public void delete(Item item)
	{
		this.kitchenManager.getConnection().deleteItem(item.get_id());
		this.deleteLocal(item);
	}
}
