package de.unikassel.chefcoders.codecampkitchen.end2end;

import android.support.test.runner.AndroidJUnit4;
import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestApiAccess
{
	private String USER_ID;
	private String USER_TOKEN;

	HttpConnection httpConnection;
	KitchenConnection kitchenConnection;

	@Before
	public void setup()
	{
		httpConnection = new SyncHttpConnection();
		kitchenConnection = new KitchenConnection(httpConnection);

		User user = JsonTranslator.toUser(kitchenConnection.createAdminUser(""));
		USER_ID = user.get_id();
		USER_TOKEN = user.getToken();

		kitchenConnection.setUserToken(USER_TOKEN);
	}

	@After
	public void teardown() {
		kitchenConnection.deleteUser(USER_ID);
	}

	@Test
	public void createAndDeleteUser()
	{
		User user = new User().setCredit(0).setMail("example@web.de").setName("Peter");
		String userJson = kitchenConnection.createRegularUser(JsonTranslator.toJson(user));

		user = JsonTranslator.toUser(userJson);

		kitchenConnection.deleteUser(user.get_id());
	}

	@Test
	public void createAndDeleteItem()
	{
		Item item = new Item().setAmount(1).setName("itemName").setPrice(20);
		String itemJson = kitchenConnection.createItem(JsonTranslator.toJson(item));

		item = JsonTranslator.toItem(itemJson);

		kitchenConnection.deleteItem(item.get_id());
	}

	@Test
	public void createAndDeletePurchase()
	{
		User user = JsonTranslator.toUser(kitchenConnection.createRegularUser(""));
		Item item = JsonTranslator.toItem(kitchenConnection.createItem(""));

		Purchase purchase = new Purchase().setItem_id(item.get_id()).setUser_id(user.get_id());
		String purchaseJson = kitchenConnection.buyItem(JsonTranslator.toJson(purchase));

		purchase = JsonTranslator.toPurchase(purchaseJson);

		kitchenConnection.deletePurchase(purchase.get_id());
		kitchenConnection.deleteItem(item.get_id());
		kitchenConnection.deleteUser(user.get_id());
	}
}
