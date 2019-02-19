package de.unikassel.chefcoders.codecampkitchen.connection;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpMethodException;

@RunWith(AndroidJUnit4.class)
public class TestSyncHttpConnection {
	@Test
	public void testGetMethod() throws SyncHttpMethodException {
		HttpConnection connection = new SyncHttpConnection();

		String result = connection.get("");

		Assert.assertNotNull(result);
		Assert.assertEquals("{\"version\":\"1.0\",\"name\":\"Kitchen Management\"}", result);
	}
}
