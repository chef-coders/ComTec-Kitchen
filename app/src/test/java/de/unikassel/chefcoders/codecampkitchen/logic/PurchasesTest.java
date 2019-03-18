package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PurchasesTest extends LogicTest
{
	private User     user1;
	private User     user2;
	private Purchase purchase1;
	private Purchase purchase2;
	private Purchase purchase3;
	private Purchase purchase4;

	// --------------- Set Up ---------------

	@Before
	public void setUp()
	{
		this.user1 = new User().set_id("asd");
		this.user2 = new User().set_id("dsa");
		this.users.updateLocal(this.user1);
		this.users.updateLocal(this.user2);
		this.session.setLoggedInUser(this.user1);

		this.purchase1 = new Purchase().set_id("a1").setUser_id("asd").setCreated("2019-03-18T12:34:56.789Z");
		this.purchase2 = new Purchase().set_id("a2").setUser_id("asd").setCreated("2019-03-18T14:34:56.789Z");
		this.purchase3 = new Purchase().set_id("a3").setUser_id("asd").setCreated("2019-03-19T12:34:56.789Z");
		this.purchase4 = new Purchase().set_id("b1").setUser_id("dsa").setCreated("2019-03-19T14:34:56.789Z");
	}

	// --------------- Access ---------------

	@Test
	public void getAll()
	{
		assertThat(this.purchases.getAll(), empty());

		this.purchases.updateLocal(this.purchase1);
		this.purchases.updateLocal(this.purchase2);
		this.purchases.updateLocal(this.purchase3);
		this.purchases.updateLocal(this.purchase4);

		assertThat(this.purchases.getAll(),
		           containsInAnyOrder(this.purchase1, this.purchase2, this.purchase3, this.purchase4));
	}

	@Test
	public void getMine()
	{
		assertThat(this.purchases.getMine(), empty());

		this.purchases.updateLocal(this.purchase1);
		this.purchases.updateLocal(this.purchase2);
		this.purchases.updateLocal(this.purchase3);
		this.purchases.updateLocal(this.purchase4);

		assertThat(this.purchases.getMine(), containsInAnyOrder(this.purchase1, this.purchase2, this.purchase3));
	}

	@Test
	public void getMineGrouped()
	{
		Map<String, List<Purchase>> grouped = this.purchases.getMineGrouped();
		assertTrue(grouped.isEmpty());

		this.purchases.updateLocal(this.purchase1);
		this.purchases.updateLocal(this.purchase2);
		this.purchases.updateLocal(this.purchase3);
		this.purchases.updateLocal(this.purchase4);

		grouped = this.purchases.getMineGrouped();
		assertThat(grouped.size(), is(2));
		assertThat(grouped, hasKey("2019-03-18"));
		assertThat(grouped, hasKey("2019-03-19"));
		assertThat(grouped.get("2019-03-18"), contains(this.purchase1, this.purchase2));
		assertThat(grouped.get("2019-03-19"), contains(this.purchase3));
	}

	// --------------- Modification ---------------

	@Test
	public void updateLocal()
	{
		this.purchases.updateLocal(this.purchase1);
		this.purchases.updateLocal(this.purchase2);
		this.purchases.updateLocal(this.purchase3);
		this.purchases.updateLocal(this.purchase4);

		assertThat(this.purchases.getAll(),
		           containsInAnyOrder(this.purchase1, this.purchase2, this.purchase3, this.purchase4));
	}

	// --------------- Communication ---------------

	@Test
	public void refreshAll()
	{
		// TODO
	}

	@Test
	public void refreshMine()
	{
		// TODO
	}

	// --------------- Tear Down ---------------

	@After
	public void tearDown()
	{
	}
}
