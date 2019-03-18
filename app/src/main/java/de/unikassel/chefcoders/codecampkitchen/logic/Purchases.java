package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Purchases
{
	// =============== Static Fields ===============

	public static final Purchases shared = new Purchases(KitchenConnection.shared, Session.shared);

	// =============== Fields ===============

	private final KitchenConnection connection;
	private final Session           session;

	private final Map<String, Purchase> purchases = new HashMap<>();

	// =============== Constructors ===============

	public Purchases(KitchenConnection connection, Session session)
	{
		this.connection = connection;
		this.session = session;
	}

	// =============== Methods ===============

	// --------------- Access ---------------

	public List<Purchase> getAll()
	{
		return new ArrayList<>(this.purchases.values());
	}

	public Stream<Purchase> myPurchases()
	{
		final String userId = this.session.getLoggedInUser().get_id();
		return this.purchases.values().stream().filter(userFilter(userId));
	}

	public List<Purchase> getMine()
	{
		return this.myPurchases().collect(Collectors.toList());
	}

	public Map<String, List<Purchase>> getMineGrouped()
	{
		// comparing strings, but works for ISO 8601 timestamps
		return StreamHelper.group(this.myPurchases(), p -> p.getCreated().substring(0, 10),
		                          Comparator.comparing(Purchase::getCreated));
	}

	// --------------- Modification ---------------

	public void updateLocal(Purchase purchase)
	{
		this.purchases.put(purchase.get_id(), purchase);
	}

	// --------------- Communication ---------------

	public void refreshAll()
	{
		final String resultJson = this.connection.getAllPurchases();
		final List<Purchase> resultPurchases = JsonTranslator.toPurchases(resultJson);
		resultPurchases.forEach(this::updateLocal);
	}

	public void refreshMine()
	{
		final String resultJson = this.connection.getMyPurchases();
		final List<Purchase> resultPurchases = JsonTranslator.toPurchases(resultJson);
		resultPurchases.forEach(this::updateLocal);
	}

	private static Predicate<Purchase> userFilter(String userId)
	{
		return p -> userId.equals(p.getUser_id());
	}
}
