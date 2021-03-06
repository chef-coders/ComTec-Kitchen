package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CartTest extends LogicTest
{
	private static final String USER_TOKEN = "foobar";

	private User user;
	private Item item1;
	private Item item2;

	// --------------- Set Up ---------------

	@Before
	public void setUp()
	{
		this.item1 = new Item().set_id("asd").setAmount(10).setPrice(1.2);
		this.items.updateLocal(this.item1);

		this.item2 = new Item().set_id("dsa").setAmount(5).setPrice(0.75);
		this.items.updateLocal(this.item2);

		this.user = new User().set_id("usr").setToken(USER_TOKEN);
		this.users.updateLocal(this.user);
		this.session.setLoggedInUser(this.user);
	}

	// --------------- Access ---------------

	@Test
	public void getPurchases()
	{
		assertThat(this.cart.getPurchases(), empty());
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.getPurchases(), not(empty()));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getPurchasesUnmodifiable()
	{
		this.cart.getPurchases().add(new Purchase());
	}

	@Test
	public void isEmpty()
	{
		assertTrue(this.cart.isEmpty());
		assertThat(this.cart.add(this.item1), is(1));
		assertFalse(this.cart.isEmpty());
	}

	@Test
	public void getTotal()
	{
		assertThat(this.cart.getTotal(), is(0.0));
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.getTotal(), is(1.2));
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.getTotal(), is(2.4));
		assertThat(this.cart.add(this.item2), is(1));
		assertThat(this.cart.getTotal(), is(3.15));
	}

	@Test
	public void getAmount()
	{
		assertThat(this.cart.getAmount(this.item1), is(0));
		assertThat(this.cart.getAmount(this.item2), is(0));
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.getAmount(this.item1), is(1));
		assertThat(this.cart.getAmount(this.item2), is(0));
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.getAmount(this.item1), is(2));
		assertThat(this.cart.getAmount(this.item2), is(0));
		assertThat(this.cart.add(this.item2), is(1));
		assertThat(this.cart.getAmount(this.item1), is(2));
		assertThat(this.cart.getAmount(this.item2), is(1));
	}

	// --------------- Modification ---------------

	@Test
	public void clear()
	{
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.add(this.item2), is(1));
		assertFalse(this.cart.isEmpty());
		this.cart.clear();
		assertTrue(this.cart.isEmpty());
	}

	@Test
	public void add()
	{
		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.getAmount(this.item1), is(1));
		assertThat(this.cart.getAmount(this.item2), is(0));

		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.getAmount(this.item1), is(2));
		assertThat(this.cart.getAmount(this.item2), is(0));

		assertThat(this.cart.add(this.item2), is(1));
		assertThat(this.cart.getAmount(this.item1), is(2));
		assertThat(this.cart.getAmount(this.item2), is(1));

		this.cart.clear();
		this.item1.setAmount(0);
		assertThat(this.cart.add(this.item1), is(0));
		assertTrue(this.cart.isEmpty());
	}

	@Test
	public void addWithAmount()
	{
		assertThat(this.cart.add(this.item1, 3), is(3));
		assertThat(this.cart.getAmount(this.item1), is(3));
		assertThat(this.cart.getAmount(this.item2), is(0));

		assertThat(this.cart.add(this.item1, 6), is(6));
		assertThat(this.cart.getAmount(this.item1), is(9));
		assertThat(this.cart.getAmount(this.item2), is(0));

		assertThat(this.cart.add(this.item2, 8), is(5));
		assertThat(this.cart.getAmount(this.item1), is(9));
		assertThat(this.cart.getAmount(this.item2), is(5));

		assertThat(this.cart.add(this.item1, 8), is(1));
		assertThat(this.cart.getAmount(this.item1), is(10));
		assertThat(this.cart.getAmount(this.item2), is(5));
	}

	@Test
	public void remove()
	{
		assertFalse(this.cart.remove(this.item1));
		assertFalse(this.cart.remove(this.item2));

		assertThat(this.cart.add(this.item1), is(1));
		assertThat(this.cart.add(this.item2, 3), is(3));
		assertThat(this.cart.getAmount(this.item1), is(1));
		assertThat(this.cart.getAmount(this.item2), is(3));

		assertTrue(this.cart.remove(this.item1));
		assertThat(this.cart.getAmount(this.item1), is(0));
		assertThat(this.cart.getAmount(this.item2), is(3));

		assertTrue(this.cart.remove(this.item2));
		assertThat(this.cart.getAmount(this.item1), is(0));
		assertThat(this.cart.getAmount(this.item2), is(0));

		assertFalse(this.cart.remove(this.item1));
		assertFalse(this.cart.remove(this.item2));
	}

	@Test
	public void updateAll()
	{
		assertThat(this.cart.add(this.item1, 10), is(10));
		assertThat(this.cart.add(this.item2, 5), is(5));
		assertThat(this.cart.getAmount(this.item1), is(10));
		assertThat(this.cart.getAmount(this.item2), is(5));

		this.item1.setAmount(5);
		this.item2.setAmount(2);
		this.cart.updateAll();
		assertThat(this.cart.getAmount(this.item1), is(5));
		assertThat(this.cart.getAmount(this.item2), is(2));

		this.item1.setAmount(0);
		this.item2.setAmount(5);
		this.cart.updateAll();
		assertThat(this.cart.getAmount(this.item1), is(0));
		assertThat(this.cart.getAmount(this.item2), is(2));

		this.item2.setAmount(0);
		this.cart.updateAll();
		assertTrue(this.cart.isEmpty());

		this.item1.setAmount(8);
		assertThat(this.cart.add(this.item1), is(1));

		this.items.deleteLocal(this.item1);
		this.cart.updateAll();
		assertTrue(this.cart.isEmpty());
	}

	// --------------- Communication ---------------

	@Test
	public void refreshAll()
	{
		// TODO
	}

	@Test
	public void submit()
	{
		// TODO
	}

	// --------------- Tear Down ---------------

	@After
	public void tearDown()
	{
	}
}
