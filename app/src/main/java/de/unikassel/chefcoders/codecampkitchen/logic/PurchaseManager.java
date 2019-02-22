package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.*;
import java.util.stream.Collectors;

public class PurchaseManager
{
	// =============== Fields ===============

	private final KitchenManager kitchenManager;

	private Map<String, Purchase> purchases = new HashMap<>();

	// =============== Constructors ===============

	public PurchaseManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	public List<Purchase> getAll()
	{
		return new ArrayList<>(this.purchases.values());
	}

	public void refreshAll()
	{
		JsonTranslator.toPurchases(this.kitchenManager.getConnection().getAllPurchases()).forEach(this::updateLocal);
	}

	public void updateLocal(Purchase purchase)
	{
		this.purchases.put(purchase.get_id(), purchase);
	}

	public List<Purchase> getMine()
	{
		final String userId = this.kitchenManager.session().getLoggedInUser().get_id();
		return this.purchases.values().stream().filter(KitchenManager.userFilter(userId)).collect(Collectors.toList());
	}

	public Map<String, List<Purchase>> getMineGrouped()
	{
		final Collection<Purchase> purchases = this.purchases.values();
		if (purchases.isEmpty())
		{
			return Collections.singletonMap("Nothing here", Collections.emptyList());
		}

		return purchases.stream().collect(Collectors.groupingBy(p -> p.getCreated().substring(0, 10)));
	}

	public void refreshMine()
	{
		JsonTranslator.toPurchases(this.kitchenManager.getConnection().getPurchasesForUser())
		              .forEach(this::updateLocal);
	}
}
