package de.unikassel.chefcoders.codecampkitchen.end2end;

import de.unikassel.chefcoders.codecampkitchen.communication.*;
import de.unikassel.chefcoders.codecampkitchen.model.*;
import org.junit.*;


public class TestApiAccessFromKitchenConnection
{
	private String ITEM_ID = "12345656785";
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

		Item item = new Item().setAmount(1).setName("Kokosmilch").setPrice(0.5).setKind("Saft").set_id(ITEM_ID);

		JsonTranslator.toItem(kitchenConnection.createItem(JsonTranslator.toJson(item)));
	}

	@After
	public void teardown() {
		kitchenConnection.deleteItem(ITEM_ID);
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
		Item item = new Item().setAmount(1).setName("Apfelsaft").setPrice(20).setKind("Saft").set_id("12345698");
		String itemJson = kitchenConnection.createItem(JsonTranslator.toJson(item));

		item = JsonTranslator.toItem(itemJson);

		kitchenConnection.deleteItem(item.get_id());
	}

	@Test
	public void createAndDeletePurchase()
	{
		Purchase purchase = new Purchase().setItem_id(ITEM_ID).setUser_id(ADMIN_ID).setAmount(1);
		String purchaseJson = kitchenConnection.buyItem(JsonTranslator.toJson(purchase));

		purchase = JsonTranslator.extractPurchaseFromOrder(purchaseJson);

		kitchenConnection.deletePurchase(purchase.get_id());
	}
}
