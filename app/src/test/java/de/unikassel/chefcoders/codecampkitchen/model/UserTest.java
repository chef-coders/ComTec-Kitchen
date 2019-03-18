package de.unikassel.chefcoders.codecampkitchen.model;

import org.junit.Test;

import java.beans.PropertyChangeListener;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserTest extends DataModelTest<User>
{
	private static final int      NUM_PROPS      = 7;
	private static final String[] NULLABLE_PROPS = { User.PROPERTY__id, User.PROPERTY_role, User.PROPERTY_created,
		User.PROPERTY_name, User.PROPERTY_mail, User.PROPERTY_token };

	@Override
	protected int getNumProps()
	{
		return NUM_PROPS;
	}

	@Override
	protected String[] getNullableProps()
	{
		return NULLABLE_PROPS;
	}

	@Override
	protected User create()
	{
		return new User();
	}

	@Override
	protected void setProps(User user)
	{
		user.set_id("usr").setRole("admin").setCreated("today").setName("testus").setMail("test@example.com")
		    .setToken("foobar").setCredit(123.45);
	}

	@Override
	protected void setNulls(User user)
	{
		user.set_id(null).setRole(null).setCreated(null).setName(null).setMail(null).setToken(null);
	}

	@Override
	protected void addPropertyChangeListener(User object, PropertyChangeListener listener)
	{
		object.addPropertyChangeListener(listener);
	}

	@Override
	protected void addPropertyChangeListener(User object, String name, PropertyChangeListener listener)
	{
		object.addPropertyChangeListener(name, listener);
	}

	@Override
	protected void removePropertyChangeListener(User object, PropertyChangeListener listener)
	{
		object.removePropertyChangeListener(listener);
	}

	@Override
	protected void removePropertyChangeListener(User object, String name, PropertyChangeListener listener)
	{
		object.removePropertyChangeListener(name, listener);
	}

	@Test
	public void setAndGet()
	{
		final User user = new User();
		this.setProps(user);

		assertThat(user.get_id(), is("usr"));
		assertThat(user.getRole(), is("admin"));
		assertThat(user.getCreated(), is("today"));
		assertThat(user.getName(), is("testus"));
		assertThat(user.getMail(), is("test@example.com"));
		assertThat(user.getToken(), is("foobar"));
		assertThat(user.getCredit(), is(123.45));
	}

	@Test
	public void _toString()
	{
		final User user = new User();
		this.setProps(user);
		assertThat(user, hasToString("usr today foobar testus test@example.com admin"));
	}

	@Test
	public void removeYou()
	{
		new User().removeYou();
	}
}
