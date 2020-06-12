package com.puntogris.areyouarobot

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTest {

    @Test
    fun should_navigate_to_singlePlayer_game(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.welcomeFragmentPlayButton)).perform(click())
        onView(withId(R.id.singlePlayerGameLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun should_navigate_to_rankings(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.rankingsFragment)).perform(click())
        onView(withId(R.id.rankingsLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun should_navigate_to_multiplayer(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.findMatchFragment)).perform(click()).check(matches(isDisplayed()))
    }

    @Test
    fun should_navigate_to_settings(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.playerSettingsFragment)).perform(click())
        onView(withId(R.id.findMatchFragment)).check(matches(isDisplayed()))
    }
}


