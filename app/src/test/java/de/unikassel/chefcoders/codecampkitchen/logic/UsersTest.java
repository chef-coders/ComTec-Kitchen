package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class UsersTest extends LogicTest
{
	private User user1;
	private User user2;
	private User user3;
	private User user4;

	// --------------- Set Up ---------------

	@Before
	public void setUp()
	{
		this.user1 = new User().set_id("asd").setName("admin");
		this.user2 = new User().set_id("dsa").setName("Arnold");
		this.user3 = new User().set_id("iop").setName("Bob");
		this.user4 = new User().set_id("jkl").setName("bobby");
	}

	// --------------- Access ---------------

	@Test
	public void get()
	{
		assertNull(this.users.get("asd"));
		this.users.updateLocal(this.user1);
		assertSame(this.user1, this.users.get("asd"));
		assertNull(this.users.get("dsa"));
		this.users.updateLocal(this.user2);
		assertSame(this.user2, this.users.get("dsa"));
	}

	@Test
	public void getAll()
	{
		assertThat(this.users.getAll(), empty());
		this.users.updateLocal(this.user1);
		this.users.updateLocal(this.user2);
		assertThat(this.users.getAll(), containsInAnyOrder(this.user1, this.user2));
	}

	@Test
	public void getGrouped()
	{
		Map<String, List<User>> grouped = this.users.getGrouped();
		assertTrue(grouped.isEmpty());

		this.users.updateLocal(this.user1);
		this.users.updateLocal(this.user2);
		this.users.updateLocal(this.user3);
		this.users.updateLocal(this.user4);

		grouped = this.users.getGrouped();
		assertThat(grouped.size(), is(2));
		assertThat(grouped, hasKey("A"));
		assertThat(grouped, hasKey("B"));
		assertThat(grouped.get("A"), contains(this.user1, this.user2));
		assertThat(grouped.get("B"), contains(this.user3, this.user4));
	}

	// --------------- Modification ---------------

	@Test
	public void updateLocal()
	{
		this.users.updateLocal(this.user1);
		this.users.updateLocal(this.user2);
		assertSame(this.user1, this.users.get("asd"));
		assertSame(this.user2, this.users.get("dsa"));

		final User newUser = new User().set_id("asd").setName("new");
		this.users.updateLocal(newUser);
		assertSame(newUser, this.users.get("asd"));
		assertSame(this.user2, this.users.get("dsa"));
	}

	@Test
	public void deleteLocal()
	{
		this.users.updateLocal(this.user1);
		this.users.updateLocal(this.user2);
		assertSame(this.user1, this.users.get("asd"));
		assertSame(this.user2, this.users.get("dsa"));

		this.users.deleteLocal(this.user2);
		assertSame(this.user1, this.users.get("asd"));
		assertNull(this.users.get("dsa"));

		this.users.deleteLocal(this.user3);
		assertSame(this.user1, this.users.get("asd"));
		assertNull(this.users.get("iop"));
	}

	// --------------- Communication ---------------

	@Test
	public void refreshAll()
	{
		// TODO
	}

	@Test
	public void refresh()
	{
		// TODO
	}

	@Test
	public void update()
	{
		// TODO
	}

	@Test
	public void delete()
	{
		// TODO
	}

	// --------------- Tear Down ---------------

	@After
	public void tearDown()
	{
	}
}
