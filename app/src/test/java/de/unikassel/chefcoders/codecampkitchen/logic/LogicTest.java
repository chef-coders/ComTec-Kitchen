package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Before;

public class LogicTest
{
	protected Mockery context;

	protected KitchenConnection connection;
	protected HttpConnection    httpConnection;

	protected Users   users;
	protected Session session;
	protected Items   items;
	protected Cart    cart;

	@Before
	public final void initLogic()
	{
		this.context = new Mockery();
		this.context.setThreadingPolicy(new Synchroniser());

		this.httpConnection = this.context.mock(HttpConnection.class);
		this.connection = new KitchenConnection(this.httpConnection);

		this.users = new Users(this.connection);
		this.session = new Session(this.connection, this.users);
		this.items = new Items(this.connection);
		this.cart = new Cart(this.connection, this.session, this.items);
	}
}
