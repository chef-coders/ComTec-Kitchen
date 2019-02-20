package de.unikassel.chefcoders.codecampkitchen.ui;

import android.content.ComponentName;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest
{
    @Rule
    public ActivityTestRule<LoginActivity> rule
            = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void test()
    {

        ViewInteraction editTextName = onView(withId(R.id.editTextName));
        ViewInteraction editTextEmail = onView(withId(R.id.editTextEmail));
        ViewInteraction buttonLogin = onView(withId(R.id.buttonLogin));
        ViewInteraction imageView = onView(withId(R.id.imageView));
        ViewInteraction progressBar = onView(withId(R.id.progressBar));
        ViewInteraction switchAdmin = onView(withId(R.id.switchAdmin));

        // region initial state
        editTextName.check(matches((isDisplayed())));
        editTextEmail.check(matches((isDisplayed())));
        buttonLogin.check(matches((isDisplayed())));
        imageView.check(matches((isDisplayed())));
        progressBar.check(matches((withEffectiveVisibility(ViewMatchers.Visibility.GONE))));
        switchAdmin.check(matches((isDisplayed())));
        // endregion

        editTextName.perform(typeText("My Name"));
        editTextName.check(matches(withText("My Name")));

        editTextEmail.perform(typeText("My Email"));
        editTextEmail.check(matches(withText("My Email")));

        editTextEmail.perform(closeSoftKeyboard());

        switchAdmin.check(matches(isNotChecked()));
        switchAdmin.perform(click());
        switchAdmin.check(matches(isChecked()));

        buttonLogin.check(matches(isClickable()));
        buttonLogin.check(matches(isFocusable()));
        buttonLogin.check(matches(isEnabled()));

        buttonLogin.perform(click());

    }

}
