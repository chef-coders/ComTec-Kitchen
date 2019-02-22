package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class CartManager
{
	// =============== Fields ===============

	private final KitchenManager kitchenManager;

	private final List<Purchase> purchases = new ArrayList<>();

	// =============== Constructors ===============

	public CartManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	public List<Purchase> getPurchases()
	{
		return Collections.unmodifiableList(this.purchases);
	}

	public void clear()
	{
		this.purchases.clear();
	}

	public void submit()
	{
		for (Purchase purchase : this.purchases)
		{
			this.kitchenManager.getConnection().buyItem(JsonTranslator.toJson(purchase));
		}

		this.kitchenManager.session().refreshLoggedInUser();

		this.purchases.clear();
	}

	public double getTotal()
	{
		return this.purchases.stream().mapToDouble(Purchase::getPrice).sum();
	}

	public int getAmount(Item item)
	{
		final String itemId = item.get_id();
		return this.purchases.stream().filter(itemFilter(itemId)).mapToInt(Purchase::getAmount).sum();
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
		final String itemId = item.get_id();

		for (Purchase purchase : this.purchases)
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
		final String loginId = this.kitchenManager.session().getLoggedInUser().get_id();
		final Purchase purchase = new Purchase().setItem_id(itemId).setUser_id(loginId).setAmount(actualAmount)
		                                        .setPrice(actualAmount * item.getPrice());
		this.purchases.add(purchase);
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
		return this.purchases.removeIf(itemFilter(itemId));
	}

	private static Predicate<Purchase> itemFilter(String itemId)
	{
		return p -> itemId.equals(p.getItem_id());
	}
}
