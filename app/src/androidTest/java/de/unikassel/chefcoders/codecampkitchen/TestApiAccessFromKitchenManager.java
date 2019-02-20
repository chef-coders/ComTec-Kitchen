package de.unikassel.chefcoders.codecampkitchen;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.logic.KitchenManager;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.LocalDataStore;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class TestApiAccessFromKitchenManager
{
	private KitchenManager kitchenManager;
	private KitchenConnection kitchenConnection;
	private Context context;
	private String ADMIN_TOKEN;
	private String ADMIN_ID;

	@Before
	public void setup()
	{
		kitchenConnection = new KitchenConnection(new OkHttpConnection());

		User user = new User().setName("admin").setMail("admin.example@admin.com");
		user = JsonTranslator.toUser(kitchenConnection.createAdminUser(JsonTranslator.toJson(user)));

		ADMIN_ID = user.get_id();
		ADMIN_TOKEN = user.getToken();

		context = InstrumentationRegistry.getTargetContext();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPref.edit().putString("userId", ADMIN_ID)
		          .putString("userToken", ADMIN_TOKEN).apply();

		kitchenManager = new KitchenManager(new LocalDataStore(), kitchenConnection);
	}

	@After
	public void teardown()
	{
		kitchenConnection.deleteUser(ADMIN_ID);
	}

	@Test
	public void loginSuccess()
	{
		kitchenManager.tryLogin(context);
	}

	@Test
	public void registerSuccess()
	{
		kitchenManager.register(context, "Peter", "peter.lustig@bla.de", false);
	}

	@Test
	public void itemListIsEmptyWithoutRefresh()
	{
		List<Item> items = kitchenManager.getItems();
		Assert.assertThat(items.size(), equalTo(0));
	}

	@Test
	public void fetchItemsAndAssureEntriesMadeInLocalDataStore()
	{
		kitchenManager.refreshItems();
		List<Item> items = kitchenManager.getItems();
		Assert.assertThat(items.size(), not(equalTo(0)));

	}

	@Test
	public void clearsUserDataWithoutError()
	{
		kitchenManager.clearUserData(context);
	}

	@Test
	public void assureUserIsLoggedIn()
	{
		User loggedInUser = kitchenManager.getLoggedInUser();
		Assert.assertThat(loggedInUser, not(equalTo(null)));
	}
}
