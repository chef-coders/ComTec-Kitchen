package de.unikassel.chefcoders.codecampkitchen.communication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import de.unikassel.chefcoders.codecampkitchen.logic.KitchenManager;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.LocalDataStore;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class TestKitchenManagerAPIAccess
{
	private KitchenManager kitchenManager;
	private KitchenConnection kitchenConnection;
	private Context context;

	private List<User> usersToDelete;

	@Before
	public void setup()
	{
		kitchenConnection = new KitchenConnection(new OkHttpConnection());
		kitchenManager = new KitchenManager(new LocalDataStore(), kitchenConnection);
		context = InstrumentationRegistry.getTargetContext();

		usersToDelete = new ArrayList<>();

		kitchenManager.register(context, "User", "Email", true);
		usersToDelete.add(kitchenManager.getLoggedInUser());
	}

	@After
	public void teardown()
	{
		for (User u : usersToDelete)
		{
			kitchenConnection.deleteUser(u.get_id());
		}
	}

	@Test
	public void registerUser()
	{
	}

	@Test
	public void tryLogin()
	{
		kitchenManager.tryLogin(context);
	}

	@Test
	public void getAllItems()
	{
		kitchenManager.refreshItems();
		List<Item> items = kitchenManager.getItems();

		Assert.assertThat(0, not(equalTo(items.size())));
	}
}
