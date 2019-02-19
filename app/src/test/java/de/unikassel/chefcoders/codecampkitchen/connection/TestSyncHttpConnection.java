package de.unikassel.chefcoders.codecampkitchen.connection;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpMethodException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestSyncHttpConnection
{
	@Test
	public void testGetMethod() throws SyncHttpMethodException
	{
		HttpConnection connection = new SyncHttpConnection();
		String result = connection.get("/");
		assertNotNull(result);
		assertEquals("{\"version\":\"1.0\",\"name\":\"Kitchen Management\"}", result);
	}
}
