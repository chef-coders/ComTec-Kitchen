package de.unikassel.chefcoders.codecampkitchen.end2end;

import de.unikassel.chefcoders.codecampkitchen.communication.*;
import de.unikassel.chefcoders.codecampkitchen.model.*;
import org.junit.*;


public class TestApiAccess
{
	private String ADMIN_ID;

	HttpConnection httpConnection;
	KitchenConnection kitchenConnection;

	@Before
	public void setup()
	{
		httpConnection = new OkHttpConnection();
		kitchenConnection = new KitchenConnection(httpConnection);

		User admin = new User().setName("admin").setMail("admin@example.com");
		admin = JsonTranslator.toUser(kitchenConnection.createAdminUser(JsonTranslator.toJson(admin)));
		ADMIN_ID = admin.get_id();

		kitchenConnection.setUserToken(admin.getToken());
	}

	@After
	public void teardown() {
		kitchenConnection.deleteUser(ADMIN_ID);
	}

	@Test
	public void createAndDeleteUser()
	{
		User user = new User().setMail("example@web.de").setName("Peter");
		String userJson = kitchenConnection.createRegularUser(JsonTranslator.toJson(user));

		user = JsonTranslator.toUser(userJson);

		kitchenConnection.deleteUser(user.get_id());
	}

	@Test
	public void createAndDeleteItem()
	{
		Item item = new Item().setAmount(1).setName("itemName").setPrice(20).setKind("item");
		String itemJson = kitchenConnection.createItem(JsonTranslator.toJson(item));

		item = JsonTranslator.toItem(itemJson);

		kitchenConnection.deleteItem(item.get_id());
	}

	@Test
	public void createAndDeletePurchase()
	{
		User user = new User().setMail("example@web.de").setName("Peter");
		Item item = new Item().setAmount(1).setName("itemName").setPrice(20).setKind("item");

		user = JsonTranslator.toUser(kitchenConnection.createRegularUser(JsonTranslator.toJson(user)));
		item = JsonTranslator.toItem(kitchenConnection.createItem(JsonTranslator.toJson(item)));

		Purchase purchase = new Purchase().setItem_id(item.get_id()).setUser_id(user.get_id()).setAmount(1);
		String purchaseJson = kitchenConnection.buyItem(JsonTranslator.toJson(purchase));

		purchase = JsonTranslator.toPurchase(purchaseJson);

		kitchenConnection.deletePurchase(purchase.get_id());
		kitchenConnection.deleteItem(item.get_id());
		kitchenConnection.deleteUser(user.get_id());
	}
}
