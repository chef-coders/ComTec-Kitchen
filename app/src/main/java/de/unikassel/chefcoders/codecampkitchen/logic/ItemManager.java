package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.*;

public class ItemManager
{
	private final KitchenManager kitchenManager;

	private final Map<String, Item> items = new HashMap<>();

	public ItemManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	public List<Item> getItems()
	{
		return new ArrayList<>(this.items.values());
	}

	public void refreshItems()
	{
		this.items.clear();
		JsonTranslator.toItems(this.kitchenManager.getConnection().getAllItems()).forEach(this::updateLocal);
	}

	public Map<String, List<Item>> getGroupedItems()
	{
		return KitchenManager.group(this.items.values(), Item::getKind,
		                            Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER));
	}

	public void createItem(String id, String name, double price, int amount, String kind)
	{
		if (!this.kitchenManager.session().isAdmin())
		{
			return;
		}

		final Item item = new Item().set_id(id).setName(name).setPrice(price).setAmount(amount).setKind(kind);
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson;

		resultJson = this.kitchenManager.getConnection().createItem(itemJson);

		final Item createdItem = JsonTranslator.toItem(resultJson);

		this.updateLocal(createdItem);
	}

	public void updateItem(String id, String name, double price, int amount, String kind)
	{
		if (!this.kitchenManager.session().isAdmin())
		{
			return;
		}

		final Item currentItem = this.getItemById(id);

		final Item item = new Item().setName(name).setPrice(price).setAmount(amount).setKind(kind);
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson;

		resultJson = this.kitchenManager.getConnection().updateItem(id, itemJson);

		final Item updatedItem = JsonTranslator.toItem(resultJson);

		currentItem.setName(updatedItem.getName()).setPrice(updatedItem.getPrice()).setAmount(updatedItem.getAmount())
		           .setKind(updatedItem.getKind());
	}

	public void deleteItem(String id)
	{
		if (!this.kitchenManager.session().isAdmin())
		{
			return;
		}

		Item currentItem = this.getItemById(id);

		this.kitchenManager.getConnection().deleteItem(id);

		this.items.remove(currentItem);
	}

	public void buyItem(Item item, int amount)
	{
		final Purchase purchase = new Purchase().setUser_id(this.kitchenManager.session().getLoggedInUser().get_id())
		                                        .setItem_id(item.get_id()).setAmount(amount);

		final Purchase createdPurchase = JsonTranslator.toPurchase(
			this.kitchenManager.getConnection().buyItem(JsonTranslator.toJson(purchase)));

		this.kitchenManager.purchases().updateLocal(createdPurchase);
	}

	public boolean containsItem(String id)
	{
		return this.items.get(id) != null;
	}

	public Item getItemById(String id)
	{
		return this.items.get(id);
	}

	private void updateLocal(Item item)
	{
		this.items.put(item.get_id(), item);
	}
}
