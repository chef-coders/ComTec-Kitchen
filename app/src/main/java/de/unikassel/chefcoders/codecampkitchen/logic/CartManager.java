package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.LocalDataStore;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.Collections;
import java.util.List;

public class CartManager
{
	private final KitchenManager kitchenManager;

	public CartManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	public List<Purchase> getPurchases()
	{
		return Collections.unmodifiableList(this.kitchenManager.getLocalDataStore().getShoppingCart());
	}

	public void clear()
	{
		this.kitchenManager.getLocalDataStore().getShoppingCart().clear();
	}

	public void submit()
	{
		for (Purchase purchase : this.kitchenManager.getLocalDataStore().getShoppingCart())
		{
			this.kitchenManager.getConnection().buyItem(JsonTranslator.toJson(purchase));
		}

		this.kitchenManager.refreshLoggedInUser();

		this.kitchenManager.getLocalDataStore().getShoppingCart().clear();
	}

	public double getTotal()
	{
		return this.kitchenManager.getLocalDataStore().getShoppingCart().stream().mapToDouble(Purchase::getPrice).sum();
	}

	public int getAmount(Item item)
	{
		final String itemId = item.get_id();
		return this.kitchenManager.getLocalDataStore().getShoppingCart().stream()
		                          .filter(KitchenManager.itemFilter(itemId)).mapToInt(Purchase::getAmount).sum();
	}

	/**
	 * Adds the given item to the cart or increments its cart amount.
	 *
	 * @param item
	 * 	the item to add
	 *
	 * @return 1 if the item was added successfully, 0 if not because not enough items are in stock.
	 *
	 * @see CartManager#add(Item, int)
	 */
	public int add(Item item)
	{
		return this.add(item, 1);
	}

	/**
	 * Adds the given item to the cart or adds its cart amount.
	 *
	 * @param item
	 * 	the item to add
	 * @param amount
	 * 	the amount to add
	 *
	 * @return the amount that was actually added, may be less than {@code amount} if there are not enough items in stock.
	 */
	public int add(Item item, int amount)
	{
		final LocalDataStore localDataStore = this.kitchenManager.getLocalDataStore();
		final String itemId = item.get_id();

		for (Purchase purchase : localDataStore.getShoppingCart())
		{
			if (itemId.equals(purchase.getItem_id()))
			{
				final int oldAmount = purchase.getAmount();
				final int newAmount = Math.min(oldAmount + amount, item.getAmount());
				purchase.setAmount(newAmount);
				purchase.setPrice(newAmount * item.getPrice());
				return newAmount - oldAmount; // difference is how many have actually been added
			}
		}

		final int actualAmount = Math.min(amount, item.getAmount());
		final String loginId = localDataStore.getLoginId();
		final Purchase purchase = new Purchase().setItem_id(itemId).setUser_id(loginId).setAmount(actualAmount)
		                                        .setPrice(actualAmount * item.getPrice());
		localDataStore.getShoppingCart().add(purchase);
		return actualAmount;
	}

	/**
	 * Removes the item from the cart completely.
	 *
	 * @param item
	 * 	the item to remove
	 *
	 * @return true iff the item was in the cart
	 */
	public boolean remove(Item item)
	{
		final String itemId = item.get_id();
		return this.kitchenManager.getLocalDataStore().getShoppingCart().removeIf(KitchenManager.itemFilter(itemId));
	}
}
