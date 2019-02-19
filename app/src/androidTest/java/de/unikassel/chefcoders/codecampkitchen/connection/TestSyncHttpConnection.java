package de.unikassel.chefcoders.codecampkitchen.connection;

import android.support.test.runner.AndroidJUnit4;
import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestSyncHttpConnection {
	@Test
	public void testConnection() {
		HttpConnection connection = new SyncHttpConnection();

		String result = connection.get("");

		Assert.assertNotNull(result);
		Assert.assertEquals("{\"version\":\"1.0\",\"name\":\"Kitchen Management\"}", result);
	}
}
