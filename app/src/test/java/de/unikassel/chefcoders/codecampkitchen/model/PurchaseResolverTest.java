package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PurchaseResolverTest
{
	// --------------- resolveUser ---------------

	@Test(expected = IllegalStateException.class)
	public void resolveUser_id_user_null()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase();

		// when:
		PurchaseResolver.resolveUser(database, purchase);
	}

	@Test(expected = IllegalStateException.class)
	public void resolveUser_user_dne()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setUser_id("asdf");

		// when:
		PurchaseResolver.resolveUser(database, purchase);
	}

	@Test
	public void resolveUser_user_from_id()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setUser_id("asdf");
		final User user = new User().set_id("asdf");
		database.addUser(user);

		// when:
		PurchaseResolver.resolveUser(database, purchase);

		// then:
		assertEquals("user was not resolved", user, purchase.getUser());
	}

	@Test
	public void resolveUser_id_from_user()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase();
		final User user = new User().set_id("asdf");
		database.addUser(user);
		purchase.setUser(user);

		// when:
		PurchaseResolver.resolveUser(database, purchase);

		// then:
		assertEquals("user_id was not resolved", "asdf", purchase.getUser_id());
	}

	@Test(expected = IllegalStateException.class)
	public void resolveUser_user_id_mismatch()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setUser_id("hjkl");
		final User user = new User().set_id("asdf");
		database.addUser(user);
		purchase.setUser(user);

		// when:
		PurchaseResolver.resolveUser(database, purchase);
	}

	@Test
	public void resolveUser_already_resolved()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setUser_id("asdf");
		final User user = new User().set_id("asdf");
		database.addUser(user);
		purchase.setUser(user);

		// when:
		PurchaseResolver.resolveUser(database, purchase);

		// then:
		assertEquals("user was not resolved", user, purchase.getUser());
		assertEquals("user_id was not resolved", "asdf", purchase.getUser_id());
	}

	// --------------- resolveItem ---------------

	@Test(expected = IllegalStateException.class)
	public void resolveItem_id_item_null()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase();

		// when:
		PurchaseResolver.resolveItem(database, purchase);
	}

	@Test(expected = IllegalStateException.class)
	public void resolveItem_item_dne()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setItem_id("asdf");

		// when:
		PurchaseResolver.resolveItem(database, purchase);
	}

	@Test
	public void resolveItem_item_from_id()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setItem_id("asdf");
		final Item item = new Item().set_id("asdf");
		database.addItem(item);

		// when:
		PurchaseResolver.resolveItem(database, purchase);

		// then:
		assertEquals("item was not resolved", item, purchase.getItem());
	}

	@Test
	public void resolveItem_id_from_item()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase();
		final Item item = new Item().set_id("asdf");
		database.addItem(item);
		purchase.setItem(item);

		// when:
		PurchaseResolver.resolveItem(database, purchase);

		// then:
		assertEquals("item_id was not resolved", "asdf", purchase.getItem_id());
	}

	@Test(expected = IllegalStateException.class)
	public void resolveItem_item_id_mismatch()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setItem_id("hjkl");
		final Item item = new Item().set_id("asdf");
		database.addItem(item);
		purchase.setItem(item);

		// when:
		PurchaseResolver.resolveItem(database, purchase);
	}

	@Test
	public void resolveItem_already_resolved()
	{
		// given:
		final Database database = new Database();
		final Purchase purchase = new Purchase().setItem_id("asdf");
		final Item item = new Item().set_id("asdf");
		database.addItem(item);
		purchase.setItem(item);

		// when:
		PurchaseResolver.resolveItem(database, purchase);

		// then:
		assertEquals("item was not resolved", item, purchase.getItem());
		assertEquals("item_id was not resolved", "asdf", purchase.getItem_id());
	}
}
