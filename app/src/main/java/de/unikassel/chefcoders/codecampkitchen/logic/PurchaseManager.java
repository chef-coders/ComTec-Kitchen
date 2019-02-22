package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PurchaseManager
{
	// =============== Fields ===============

	private final KitchenManager kitchenManager;

	private final Map<String, Purchase> purchases = new HashMap<>();

	// =============== Constructors ===============

	public PurchaseManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	// --------------- Access ---------------

	public List<Purchase> getAll()
	{
		return new ArrayList<>(this.purchases.values());
	}

	public Stream<Purchase> myPurchases()
	{
		final String userId = this.kitchenManager.session().getLoggedInUser().get_id();
		return this.purchases.values().stream().filter(userFilter(userId));
	}

	public List<Purchase> getMine()
	{
		return this.myPurchases().collect(Collectors.toList());
	}

	public Map<String, List<Purchase>> getMineGrouped()
	{
		return this.myPurchases().collect(Collectors.groupingBy(p -> p.getCreated().substring(0, 10)));
	}

	// --------------- Modification ---------------

	public void updateLocal(Purchase purchase)
	{
		this.purchases.put(purchase.get_id(), purchase);
	}

	// --------------- Communication ---------------

	public void refreshAll()
	{
		final String resultJson = this.kitchenManager.getConnection().getAllPurchases();
		final List<Purchase> resultPurchases = JsonTranslator.toPurchases(resultJson);
		resultPurchases.forEach(this::updateLocal);
	}

	public void refreshMine()
	{
		final String resultJson = this.kitchenManager.getConnection().getPurchasesForUser();
		final List<Purchase> resultPurchases = JsonTranslator.toPurchases(resultJson);
		resultPurchases.forEach(this::updateLocal);
	}

	private static Predicate<Purchase> userFilter(String userId)
	{
		return p -> userId.equals(p.getUser_id());
	}
}
