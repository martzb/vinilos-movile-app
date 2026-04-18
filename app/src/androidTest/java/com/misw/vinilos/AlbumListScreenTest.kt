package com.misw.vinilos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas Espresso para la pantalla de lista de álbumes (AlbumFragment).
 *
 * Para llegar a esta pantalla primero se hace click en la tarjeta de Visitante,
 * igual que lo haría un usuario real.
 */
@RunWith(AndroidJUnit4::class)
class AlbumListScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /** Navega a la lista de álbumes antes de cada test. */
    private fun navigateToAlbumList() {
        onView(withId(R.id.card_visitor)).perform(click())
    }

    // ── Visibilidad de elementos ──────────────────────────────────────────

    @Test
    fun albumList_showsTrendingRecyclerView() {
        navigateToAlbumList()

        onView(withId(R.id.rv_trending))
            .check(matches(isDisplayed()))
    }

    @Test
    fun albumList_showsRecentRecyclerView() {
        navigateToAlbumList()

        onView(withId(R.id.rv_recent))
            .check(matches(isDisplayed()))
    }

    // ── Navegación ────────────────────────────────────────────────────────

    @Test
    fun clickFirstTrendingAlbum_navigatesToDetail() {
        navigateToAlbumList()

        // Espera a que carguen los álbumes (máx 5 s)
        Thread.sleep(5000)

        onView(withId(R.id.rv_trending))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )

        // En el detalle debe aparecer el título del álbum
        onView(withId(R.id.albumTitle))
            .check(matches(isDisplayed()))
    }

    @Test
    fun backButton_returnsToWelcomeScreen() {
        navigateToAlbumList()

        pressBack()

        // Al volver atrás, la tarjeta de visitante vuelve a ser visible
        onView(withId(R.id.card_visitor))
            .check(matches(isDisplayed()))
    }

    // ── Validación de Datos y Scroll ──────────────────────────────────────

    @Test
    fun albumList_cardShowsData() {
        navigateToAlbumList()
        
        // Espera a que carguen los álbumes
        Thread.sleep(5000)

        // Verifica que la lista en tendencia contiene cards que muestran el nombre y artista
        onView(withId(R.id.rv_trending))
            .check(matches(hasDescendant(withId(R.id.tv_album_name))))
        
        onView(withId(R.id.rv_trending))
            .check(matches(hasDescendant(withId(R.id.tv_album_artist))))
    }

    @Test
    fun albumList_scrollsCorrectly() {
        navigateToAlbumList()
        
        // Espera a que carguen los álbumes
        Thread.sleep(5000)

        // Verifica el scroll navegando hacia la posición 5 de la lista
        onView(withId(R.id.rv_trending))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
    }
}
