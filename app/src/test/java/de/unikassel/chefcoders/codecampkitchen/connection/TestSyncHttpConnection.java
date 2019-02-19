package de.unikassel.chefcoders.codecampkitchen.connection;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;

public class TestSyncHttpConnection
{
	private Mockery context;
	private HttpConnection httpConnection;

	@Before
	public void setup()
	{
		this.context = new Mockery();
		this.httpConnection = this.context.mock(HttpConnection.class);
	}

	@Test
	public void testGetMethod() {
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestSyncHttpConnection.this.httpConnection).get("", null);
		}});

		this.httpConnection.get("", null);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testPostMethod() {
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestSyncHttpConnection.this.httpConnection).post("", "", null);
		}});

		this.httpConnection.post("", "", null);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testPutMethod() {
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestSyncHttpConnection.this.httpConnection).put("", "", null);
		}});

		this.httpConnection.put("", "", null);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testDeleteMethod() {
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestSyncHttpConnection.this.httpConnection).delete("", null);
		}});

		this.httpConnection.delete("", null);

		this.context.assertIsSatisfied();
	}
}
