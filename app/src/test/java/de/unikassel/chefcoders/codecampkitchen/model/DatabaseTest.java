package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DatabaseTest
{
	@Test
	public void users()
	{
		// given:
		final Database database = new Database();
		final User user = new User().set_id("asd");

		// when:
		database.addUser(user);

		// then:
		assertFalse(database.getUsers().isEmpty());
		assertEquals(user, database.getUser("asd"));
	}

	@Test
	public void items()
	{
		// given:
		final Database database = new Database();
		final Item item = new Item().set_id("asd");

		// when:
		database.addItem(item);

		// then:
		assertFalse(database.getItems().isEmpty());
		assertEquals(item, database.getItem("asd"));
	}

	@Test
	public void addPurchase()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().set_id("asd");

		// when:
		database.addPurchase(purchase);

		// then:
		assertFalse(database.getPurchases().isEmpty());
		assertEquals(purchase, database.getPurchase("asd"));
	}
}
