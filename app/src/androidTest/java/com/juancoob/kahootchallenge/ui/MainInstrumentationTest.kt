package com.juancoob.kahootchallenge.ui

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.juancoob.kahootchallenge.R
import com.juancoob.kahootchallenge.data.server.dispatcher.RequestDispatcher
import com.juancoob.kahootchallenge.data.server.dispatcher.RequestDispatcherWithEmptyResult
import com.juancoob.kahootchallenge.data.server.dispatcher.RequestDispatcherWithError
import com.juancoob.kahootchallenge.data.server.rule.MockWebServerRule
import com.juancoob.kahootchallenge.ui.common.EspressoIdlingResource
import com.juancoob.kahootchallenge.ui.common.OkHttp3IdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServerRule.mockWebServer.dispatcher = RequestDispatcher()
        hiltRule.inject()
        val okHttp3IdlingResource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance()
            .register(okHttp3IdlingResource, EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun click_on_the_play_button_starts_the_game() {
        onView(withId(R.id.play_button)).check(matches(withText(R.string.yeah)))
        onView(withId(R.id.play_button)).perform(click())
        onView(withId(R.id.question_number)).check(matches(withText("1/12")))
    }

    @Test
    fun click_on_the_correct_option_from_first_question_shows_the_correct_toolbar() {
        click_on_the_play_button_starts_the_game()

        onView(withId(R.id.choice_list)).perform(
            actionOnItemAtPosition<ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.choice_type_bar)).check(matches(withText(R.string.correct)))
    }

    @Test
    fun click_on_the_correct_option_from_first_question_shows_the_wrong_toolbar() {
        click_on_the_play_button_starts_the_game()

        onView(withId(R.id.choice_list)).perform(
            actionOnItemAtPosition<ViewHolder>(
                1,
                click()
            )
        )

        onView(withId(R.id.choice_type_bar)).check(matches(withText(R.string.wrong)))
    }

    @Test
    fun click_on_the_continue_button_shows_the_next_question() {
        click_on_the_play_button_starts_the_game()

        onView(withId(R.id.choice_list)).perform(
            actionOnItemAtPosition<ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.continue_button)).perform(click())

        onView(withId(R.id.question_number)).check(matches(withText("2/12")))
    }

    @Test
    fun click_no_option_from_first_question_shows_times_up_toolbar() {
        click_on_the_play_button_starts_the_game()

        sleep(1000)

        onView(withId(R.id.choice_type_bar)).check(matches(withText(R.string.times_up)))
    }

    @Test
    fun empty_results_from_server_retrieves_an_unknown_error_message() {
        mockWebServerRule.mockWebServer.dispatcher = RequestDispatcherWithEmptyResult()

        onView(withId(R.id.error_text)).check(matches(withText(containsString("Unknown Error"))))
    }
    @Test
    fun when_an_error_prompts_up_user_can_click_on_the_retry_button_to_retrieve_the_first_question() {
        empty_results_from_server_retrieves_an_unknown_error_message()

        mockWebServerRule.mockWebServer.dispatcher = RequestDispatcher()

        onView(withId(R.id.try_again)).perform(click())

        click_on_the_play_button_starts_the_game()
    }

    @Test
    fun error_from_server_retrieves_a_server_error_message() {
        mockWebServerRule.mockWebServer.dispatcher = RequestDispatcherWithError()

        onView(withId(R.id.error_text)).check(matches(withText(containsString("Server Error"))))
    }
}
