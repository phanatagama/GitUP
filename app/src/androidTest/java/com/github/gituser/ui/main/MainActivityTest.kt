package com.github.gituser.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.github.gituser.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest{
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun assertViewComponent(){
        onView(withText("GitUser")).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add)).check(matches(isDisplayed()))
        onView(withId(R.id.app_bar_switch)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).perform(click())
    }

//    @Test(expected = PerformException::class)
//    fun itemDoesntExistOnRecycler(){
//        // Attempt to scroll to an item that contains the special text.
//        onView(ViewMatchers.withId(R.id.rv_user))
//            // scrollTo will fail the test if no item matches.
//            .perform(RecyclerViewActions.scrollTo<ListUserAdapter.ListViewHolder>(
//                hasDescendant(withText("not in the list"))
//            ))
//    }

    @Test
    fun assertNavigationToFavorite(){
        onView(withId(R.id.rv_user)).perform(scrollTo())
        onView(withId(R.id.fab_add)).perform(click())
        onView(withText("Favorite")).check(matches(isDisplayed()))

    }
}