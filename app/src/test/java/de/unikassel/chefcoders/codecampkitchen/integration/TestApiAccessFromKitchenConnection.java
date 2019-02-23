package de.unikassel.chefcoders.codecampkitchen.integration;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpConnectionException;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestApiAccessFromKitchenConnection
{
	private HttpConnection httpConnection;
	private KitchenConnection kitchenConnection;
	private List<User> usersToDelete;
	private List<Item> itemsToDelete;
	private List<Purchase> purchasesToDelete;
	private User admin;

	@Before
	public void setup()
	{
		usersToDelete = new ArrayList<>();
		itemsToDelete = new ArrayList<>();
		purchasesToDelete = new ArrayList<>();

		httpConnection = new OkHttpConnection();
		kitchenConnection = new KitchenConnection(httpConnection);

		admin = createAdminAndRegisterOnKitchenConnection();
	}

	@After
	public void teardown()
	{
		deleteUsers();
		deleteItems();
		deletePurchases();

		kitchenConnection.deleteUser(admin.get_id());
	}


	@Test
	public void createUser()
	{
		User user = createUserWith("Name", "Mail");
		usersToDelete.add(user);
	}

	@Test
	public void createAdmin()
	{
		User user = createAdminWith("Name", "Mail");
		usersToDelete.add(user);
	}

	@Test
	public void updateUser()
	{
		User user = createUserWith("Name", "Mail");
		usersToDelete.add(user);

		User updatedUser = new User().setName("new");
		kitchenConnection.updateUser(user.get_id(), JsonTranslator.toJson(updatedUser));
	}

	@Test
	public void deleteUser()
	{
		User user = createUserWith("Name", "Mail");

		kitchenConnection.deleteUser(user.get_id());
	}

	@Test
	public void getUser()
	{
		User user = createUserWith("Name", "Mail");
		usersToDelete.add(user);

		kitchenConnection.getUser(user.get_id());
	}

	@Test
	public void getAllUsers()
	{
		kitchenConnection.getAllUsers();
	}

	@Test
	public void buyItem()
	{
		Item item = createItemFor("123456789", "Item", "Saft", 0.5, 1);
		itemsToDelete.add(item);

		Purchase purchase = userBuysItem(admin, item);
		purchasesToDelete.add(purchase);
	}

	@Test
	public void getAllPurchase()
	{
		kitchenConnection.getAllPurchases();
	}

	@Test
	public void getPurchase()
	{
		Item item = createItemFor("123456789", "Item", "Saft", 0.5, 1);
		itemsToDelete.add(item);

		Purchase purchase = userBuysItem(admin, item);
		purchasesToDelete.add(purchase);

		kitchenConnection.getPurchase(purchase.get_id());
	}

	@Test
	public void getPurchasesForUser()
	{
		Item item = createItemFor("123456789", "Item", "Saft", 0.5, 1);
		itemsToDelete.add(item);

		Purchase purchase = userBuysItem(admin, item);
		purchasesToDelete.add(purchase);

		kitchenConnection.getMyPurchases();
	}

	@Test
	public void deletePurchase()
	{
		Item item = createItemFor("123456789", "Item", "Saft", 0.5, 1);
		itemsToDelete.add(item);

		Purchase purchase = userBuysItem(admin, item);

		kitchenConnection.deletePurchase(purchase.get_id());
	}

	@Test
	public void updatePurchase()
	{
		Item item = createItemFor("123456789", "Item", "Saft", 0.5, 1);
		itemsToDelete.add(item);

		Item newItem = new Item().setAmount(2);

		Purchase purchase = userBuysItem(admin, item);
		purchasesToDelete.add(purchase);

		String newItemJson = JsonTranslator.toJson(newItem);
		kitchenConnection.updatePurchase(purchase.get_id(), newItemJson);
	}

	@Test
	public void createItem()
	{
		Item item = createItemFor("12354657864986198", "Item", "Saft", 2, 1);
		itemsToDelete.add(item);
	}

	@Test(expected = HttpConnectionException.class)
	public void createItemTooExpensive()
	{
		Item item = createItemFor("12354657864986198", "Item", "Saft", 10000, 1);
	}

	@Test
	public void getItem()
	{
		Item item = createItemFor("12354657864986198", "Item", "Saft", 2, 1);
		itemsToDelete.add(item);

		kitchenConnection.getItem(item.get_id());
	}

	@Test
	public void getAllItems()
	{
		kitchenConnection.getAllItems();
	}

	@Test
	public void deleteItem()
	{
		Item item = createItemFor("12354657864986198", "Item", "Saft", 2, 1);

		kitchenConnection.deleteItem(item.get_id());
	}

	@Test
	public void updateItems()
	{
		Item item = createItemFor("12354657864986198", "Item", "Saft", 2, 1);
		itemsToDelete.add(item);

		Item update = new Item().setAmount(2);

		String itemJson = JsonTranslator.toJson(update);
		kitchenConnection.updateItem(item.get_id(), itemJson);
	}

	private Purchase userBuysItem(User user, Item item)
	{
		Purchase purchase = new Purchase().setItem_id(item.get_id()).setUser_id(user.get_id()).setAmount(1);
		String purchaseJson = JsonTranslator.toJson(purchase);
		purchaseJson = kitchenConnection.createPurchase(purchaseJson);
		purchase = JsonTranslator.toPurchase(purchaseJson);

		return purchase;
	}

	private Item createItemFor(String id, String name, String type, double price, int amount)
	{
		Item item = new Item().set_id(id).setName(name).setKind(type).setPrice(price).setAmount(amount);
		String itemJson = JsonTranslator.toJson(item);
		itemJson = kitchenConnection.createItem(itemJson);
		item = JsonTranslator.toItem(itemJson);

		return item;
	}

	private User createUserWith(String name, String mail)
	{
		User user = new User().setMail(mail).setName(name);
		String userJson = JsonTranslator.toJson(user);
		userJson = kitchenConnection.createRegularUser(userJson);
		user = JsonTranslator.toUser(userJson);

		return user;
	}

	private User createAdminWith(String name, String mail)
	{
		User user = new User().setMail(mail).setName(name);
		String userJson = JsonTranslator.toJson(user);
		userJson = kitchenConnection.createAdminUser(userJson);
		user = JsonTranslator.toUser(userJson);

		return user;
	}

	private User createAdminAndRegisterOnKitchenConnection()
	{
		User admin = new User().setName("admin").setMail("admin@example.com");
		admin = JsonTranslator.toUser(kitchenConnection.createAdminUser(JsonTranslator.toJson(admin)));

		kitchenConnection.setUserToken(admin.getToken());

		return admin;
	}

	private void deleteUsers()
	{
		for (User u : usersToDelete)
		{
			kitchenConnection.deleteUser(u.get_id());
		}
	}

	private void deleteItems()
	{
		for (Item i : itemsToDelete)
		{
			kitchenConnection.deleteItem(i.get_id());
		}
	}

	private void deletePurchases()
	{
		for (Purchase p : purchasesToDelete)
		{
			kitchenConnection.deletePurchase(p.get_id());
		}
	}
}
