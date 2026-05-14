package com.misw.vinilos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas E2E para la vista de detalle de coleccionista.
 *
 * Flujo:
 * 1. Seleccionar "Usuario Visitante".
 * 2. Navegar a la lista de coleccionistas.
 * 3. Seleccionar un coleccionista.
 * 4. Validar que se muestran los detalles correctamente.
 * 5. Validar navegación de regreso.
 */
@RunWith(AndroidJUnit4::class)
class CollectorDetailScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private fun navigateToCollectorDetail() {
        // 1. Clic en "Usuario Visitante"
        onView(withId(R.id.card_visitor)).perform(click())

        // 2. Clic en la pestaña "Coleccionistas"
        onView(withId(R.id.collectorFragment)).perform(click())

        // 3. Esperar carga de API
        Thread.sleep(3000)

        // 4. Hacer clic en el primer coleccionista de la lista
        onView(withId(R.id.rv_collectors))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // 5. Esperar carga del detalle
        Thread.sleep(2000)
    }

    @Test
    fun e2e_collectorDetail_showsCollectorInfo() {
        navigateToCollectorDetail()

        // Verifica que el nombre y la información de contacto se muestran
        onView(withId(R.id.tvCollectorName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvCollectorContact)).check(matches(isDisplayed()))
        
        // Verifica los labels de las colecciones
        onView(withText("Álbumes favoritos")).check(matches(isDisplayed()))
        onView(withText("Artistas favoritos")).check(matches(isDisplayed()))
    }

    @Test
    fun e2e_collectorDetail_showsMusicalTastesAndAlbums() {
        navigateToCollectorDetail()

        // Hacer un par de swipes hacia arriba para asegurar que llegamos al final de la vista
        onView(withId(android.R.id.content)).perform(androidx.test.espresso.action.ViewActions.swipeUp())
        onView(withId(android.R.id.content)).perform(androidx.test.espresso.action.ViewActions.swipeUp())
        Thread.sleep(1000)

        // Verifica que la sección de álbumes (RecyclerView) existe
        onView(withId(R.id.rvAlbums)).check(matches(isDisplayed()))

        // Verifica que la sección de artistas (RecyclerView) existe
        onView(withId(R.id.rvPerformers)).check(matches(isDisplayed()))
    }

    @Test
    fun e2e_collectorDetail_backNavigation_works() {
        navigateToCollectorDetail()

        // Probar navegación de regreso mediante la flecha de la toolbar
        // Si tienes navigationIcon, click() en la vista contenedora podría no funcionar igual en toolbar material, pero usaremos contentDescription o pressBack si es necesario.
        pressBack()

        // Deberíamos estar de vuelta en la lista de coleccionistas
        onView(withId(R.id.rv_collectors)).check(matches(isDisplayed()))
    }
}
