package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LocalDataStoreTest
{
	@Test
	public void users()
	{
		// given:
		final LocalDataStore localDataStore = new LocalDataStore();
		final User user = new User().set_id("asd");

		// when:
		localDataStore.addUser(user);

		// then:
		assertFalse(localDataStore.getUsers().isEmpty());
		assertEquals(user, localDataStore.getUser("asd"));
	}

	@Test
	public void items()
	{
		// given:
		final LocalDataStore localDataStore = new LocalDataStore();
		final Item item = new Item().set_id("asd");

		// when:
		localDataStore.addItem(item);

		// then:
		assertFalse(localDataStore.getItems().isEmpty());
		assertEquals(item, localDataStore.getItem("asd"));
	}

	@Test
	public void addPurchase()
	{
		// given:
		final LocalDataStore localDataStore = new LocalDataStore();
		final Purchase purchase = new Purchase().set_id("asd");

		// when:
		localDataStore.addPurchase(purchase);

		// then:
		assertFalse(localDataStore.getPurchases().isEmpty());
		assertEquals(purchase, localDataStore.getPurchase("asd"));
	}
}
