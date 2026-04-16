package com.misw.vinilos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas Espresso para la pantalla de bienvenida (WelcomeFragment).
 *
 * Verifica que los elementos de la pantalla inicial sean visibles
 * y que la navegación hacia la lista de álbumes funcione correctamente.
 */
@RunWith(AndroidJUnit4::class)
class WelcomeScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // ── Visibilidad de elementos ──────────────────────────────────────────

    @Test
    fun welcomeScreen_showsTitleVinilos() {
        onView(withId(R.id.tv_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.app_name)))
    }

    @Test
    fun welcomeScreen_showsVisitorCard() {
        onView(withId(R.id.card_visitor))
            .check(matches(isDisplayed()))
    }

    @Test
    fun welcomeScreen_showsCollectorCard() {
        onView(withId(R.id.card_collector))
            .check(matches(isDisplayed()))
    }

    // ── Navegación ────────────────────────────────────────────────────────

    @Test
    fun clickVisitorCard_navigatesToAlbumList() {
        onView(withId(R.id.card_visitor)).perform(click())

        // Después de hacer click, el RecyclerView de álbumes debe ser visible
        onView(withId(R.id.rv_trending))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickCollectorCard_navigatesToAlbumList() {
        onView(withId(R.id.card_collector)).perform(click())

        onView(withId(R.id.rv_trending))
            .check(matches(isDisplayed()))
    }
}
