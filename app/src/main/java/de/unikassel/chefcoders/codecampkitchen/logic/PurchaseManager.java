package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.*;
import java.util.stream.Collectors;

public class PurchaseManager
{
	// =============== Fields ===============

	private final KitchenManager kitchenManager;

	// =============== Constructors ===============

	public PurchaseManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	public List<Purchase> getAll()
	{
		return new ArrayList<>(this.kitchenManager.getLocalDataStore().getPurchases().values());
	}

	public void refreshAll()
	{
		JsonTranslator.toPurchases(this.kitchenManager.getConnection().getAllPurchases()).forEach(
			purchase -> this.kitchenManager.getLocalDataStore().getPurchases().put(purchase.get_id(), purchase));
	}

	public List<Purchase> getMine()
	{
		final String userId = this.kitchenManager.session().getLoggedInUser().get_id();
		return this.kitchenManager.getLocalDataStore().getPurchases().values().stream()
		                          .filter(KitchenManager.userFilter(userId)).collect(Collectors.toList());
	}

	public Map<String, List<Purchase>> getMineGrouped()
	{
		final Collection<Purchase> purchases = this.kitchenManager.getLocalDataStore().getPurchases().values();
		if (purchases.isEmpty())
		{
			return Collections.singletonMap("Nothing here", Collections.emptyList());
		}

		return purchases.stream().collect(Collectors.groupingBy(p -> p.getCreated().substring(0, 10)));
	}

	public void refreshMine()
	{
		final Map<String, Purchase> purchases = this.kitchenManager.getLocalDataStore().getPurchases();
		JsonTranslator.toPurchases(this.kitchenManager.getConnection().getPurchasesForUser())
		              .forEach(purchase -> purchases.put(purchase.get_id(), purchase));
	}
}
