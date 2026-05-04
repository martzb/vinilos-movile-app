package com.misw.vinilos

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.pressBack

/**
 * Pruebas E2E (End-to-End) para la vista del listado de coleccionistas.
 *
 * Flujo:
 * 1. Inicia en MainActivity (WelcomeScreen por defecto).
 * 2. Selecciona "Usuario Visitante".
 * 3. Navega a "Coleccionistas" usando el Bottom Navigation.
 * 4. Valida carga, datos y scroll.
 */
@RunWith(AndroidJUnit4::class)
class CollectorListScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Función auxiliar para navegar hasta la lista de coleccionistas.
     */
    private fun navigateToCollectorList() {
        // Clic en la tarjeta de Usuario Visitante
        onView(withId(R.id.card_visitor)).perform(click())

        // Clic en la opción "Coleccionistas" del Bottom Navigation
        onView(withId(R.id.collectorFragment)).perform(click())

        // Esperar a que la API cargue los datos
        Thread.sleep(3000)
    }

    @Test
    fun e2e_collectorList_loadsListCorrectly() {
        navigateToCollectorList()

        // Verifica que la lista (RecyclerView) carga y es visible
        onView(withId(R.id.rv_collectors))
            .check(matches(isDisplayed()))
    }

    @Test
    fun e2e_collectorList_cardsShowData() {
        navigateToCollectorList()

        // Verifica que las cards contienen el nombre del coleccionista
        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.tv_collector_name))))

        // Verifica que las cards contienen el avatar circular
        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.iv_collector_avatar))))

        // Verifica que las cards contienen el email del coleccionista
        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.tv_collector_email))))
    }

    @Test
    fun e2e_collectorList_scrollsCorrectly() {
        navigateToCollectorList()

        // Hace scroll hasta la posición 2 de la lista
        onView(withId(R.id.rv_collectors))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))

        // Verifica que después del scroll la lista siga visible
        onView(withId(R.id.rv_collectors))
            .check(matches(isDisplayed()))

        // Verifica que en la vista actual sigan existiendo elementos con nombre y email
        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.tv_collector_name))))

        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.tv_collector_email))))
    }

    @Test
    fun e2e_collectorList_backButton_returnsToAlbumList() {
        navigateToCollectorList()

        onView(withId(R.id.toolbar)).perform(click())
        pressBack()

        onView(withId(R.id.rv_recent))
            .check(matches(isDisplayed()))
    }

    @Test
    fun e2e_collectorList_toolbarBack_navigatesToAlbums() {
        onView(withId(R.id.card_visitor)).perform(click())
        onView(withId(R.id.collectorFragment)).perform(click())

        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.rv_recent))
            .check(matches(isDisplayed()))
    }
}
