package com.puntogris.areyouarobot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.puntogris.areyouarobot.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun should_navigate_to_singlePlayer_game(){
        onView(withId(R.id.welcomeFragmentPlayButton)).perform(click())
        onView(withId(R.id.singlePlayerGameLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun should_navigate_to_rankings(){
        onView(withId(R.id.rankingsFragment)).perform(click())
        onView(withId(R.id.rankingsLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun should_navigate_to_multiplayer(){
        onView(withId(R.id.findMatchFragment)).perform(click()).check(matches(isDisplayed()))
    }

    @Test
    fun should_navigate_to_settings(){
        onView(withId(R.id.playerSettingsFragment)).perform(click())
        onView(withId(R.id.settingsLayout)).check(matches(isDisplayed()))
    }
}


