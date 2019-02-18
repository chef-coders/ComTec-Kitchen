package de.unikassel.chefcoders.codecampkitchen.connection;

import org.junit.Assert;
import org.junit.Test;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;

public class TestSyncHttpConnection {
	@Test
	public void testGetMethod() {
		try {
			HttpConnection connection = new SyncHttpConnection();
			String result = connection.get("/");
			Assert.assertNotNull(result);
			Assert.assertEquals("{\"version\":\"1.0\",\"name\":\"Kitchen Management\"}", result);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
