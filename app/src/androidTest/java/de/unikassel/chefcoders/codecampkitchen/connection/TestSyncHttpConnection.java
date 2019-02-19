package de.unikassel.chefcoders.codecampkitchen.connection;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;

@RunWith(AndroidJUnit4.class)
public class TestSyncHttpConnection {
	@Test
	public void testConnection() {
		HttpConnection connection = new SyncHttpConnection();
		Map<String, String> headers = new HashMap<>();

		String result = connection.get("", headers);

		Assert.assertNotNull(result);
		Assert.assertEquals("{\"version\":\"1.0\",\"name\":\"Kitchen Management\"}", result);
	}
}
