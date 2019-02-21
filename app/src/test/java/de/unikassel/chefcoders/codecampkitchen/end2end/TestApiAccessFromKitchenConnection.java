package de.unikassel.chefcoders.codecampkitchen.end2end;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;


public class TestApiAccessFromKitchenConnection
{
	HttpConnection httpConnection;
	KitchenConnection kitchenConnection;
	private String ADMIN_ID;

	@Before
	public void setup()
	{
		httpConnection = new OkHttpConnection();
		kitchenConnection = new KitchenConnection(httpConnection);

		createAdminAndRegisterOnKitchenConnection();
	}

	@After
	public void teardown()
	{

	}

	@Test
	public void test()
	{
		Assert.assertThat(true, equalTo(true));
	}

	private void createAdminAndRegisterOnKitchenConnection()
	{
		User admin = new User().setName("admin").setMail("admin@example.com");
		admin = JsonTranslator.toUser(kitchenConnection.createAdminUser(JsonTranslator.toJson(admin)));
		ADMIN_ID = admin.get_id();
		kitchenConnection.setUserToken(admin.getToken());
	}


}
