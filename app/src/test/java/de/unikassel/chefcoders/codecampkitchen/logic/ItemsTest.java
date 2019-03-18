package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ItemsTest extends LogicTest
{
	private Item item1;
	private Item item2;
	private Item item3;
	private Item item4;

	@Before
	public void setUp()
	{
		this.item1 = new Item().set_id("asd").setName("A").setKind("water");
		this.item2 = new Item().set_id("dsa").setName("B").setKind("water");
		this.item3 = new Item().set_id("iop").setName("C").setKind("juice");
		this.item4 = new Item().set_id("jkl").setName("D").setKind("juice");
	}

	@Test
	public void get()
	{
		assertNull(this.items.get("asd"));
		this.items.updateLocal(this.item1);
		assertSame(this.item1, this.items.get("asd"));
		assertNull(this.items.get("dsa"));
		this.items.updateLocal(this.item2);
		assertSame(this.item2, this.items.get("dsa"));
	}

	@Test
	public void getAll()
	{
		assertThat(this.items.getAll(), empty());
		this.items.updateLocal(this.item1);
		this.items.updateLocal(this.item2);
		assertThat(this.items.getAll(), containsInAnyOrder(this.item1, this.item2));
	}

	@Test
	public void getGrouped()
	{
		Map<String, List<Item>> grouped = this.items.getGrouped();
		assertTrue(grouped.isEmpty());

		this.items.updateLocal(this.item1);
		this.items.updateLocal(this.item2);
		this.items.updateLocal(this.item3);
		this.items.updateLocal(this.item4);

		grouped = this.items.getGrouped();
		assertThat(grouped.size(), is(2));
		assertThat(grouped, hasKey("water"));
		assertThat(grouped, hasKey("juice"));
		assertThat(grouped.get("water"), contains(this.item1, this.item2));
		assertThat(grouped.get("juice"), contains(this.item3, this.item4));
	}

	@Test
	public void updateLocal()
	{
		this.items.updateLocal(this.item1);
		this.items.updateLocal(this.item2);
		assertSame(this.item1, this.items.get("asd"));
		assertSame(this.item2, this.items.get("dsa"));

		final Item newItem = new Item().set_id("asd").setName("new");
		this.items.updateLocal(newItem);
		assertSame(newItem, this.items.get("asd"));
		assertSame(this.item2, this.items.get("dsa"));
	}

	@Test
	public void deleteLocal()
	{
		this.items.updateLocal(this.item1);
		this.items.updateLocal(this.item2);
		assertSame(this.item1, this.items.get("asd"));
		assertSame(this.item2, this.items.get("dsa"));

		this.items.deleteLocal(this.item2);
		assertSame(this.item1, this.items.get("asd"));
		assertNull(this.items.get("dsa"));

		this.items.deleteLocal(this.item3);
		assertSame(this.item1, this.items.get("asd"));
		assertNull(this.items.get("iop"));
	}

	@Test
	public void refreshAll()
	{
		// TODO
	}

	@Test
	public void create()
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

	@After
	public void tearDown()
	{
	}
}
