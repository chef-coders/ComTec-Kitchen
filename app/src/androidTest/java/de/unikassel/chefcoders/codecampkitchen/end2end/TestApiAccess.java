package de.unikassel.chefcoders.codecampkitchen.end2end;

import android.support.test.runner.AndroidJUnit4;
import de.unikassel.chefcoders.codecampkitchen.communication.*;
import de.unikassel.chefcoders.codecampkitchen.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestApiAccess
{
	@Test
	public void createUserAndVerify()
	{
		HttpConnection httpConnection = new SyncHttpConnection();
		KitchenConnection kitchenConnection = new KitchenConnection(httpConnection);

		User user = new User().setCredit(0).setMail("example@web.de").setName("Peter");
		String userJson = kitchenConnection.createRegularUser(JsonTranslator.toJson(user));

		user = JsonTranslator.toUser(userJson);

		kitchenConnection.deleteUser(user.getToken());
	}
}
