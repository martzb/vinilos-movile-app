package com.misw.vinilos

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas Espresso para la pantalla de detalle de álbum (AlbumDetailFragment).
 *
 * Para llegar a esta pantalla primero se hace click en la tarjeta de Visitante,
 * espera a que carguen los álbumes, y se selecciona un álbum.
 */
@RunWith(AndroidJUnit4::class)
class AlbumDetailScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /** Navega hacia la pantalla de detalles de un álbum antes de cada test. */
    private fun navigateToAlbumDetail() {
        // Clic en la opción de Visitante
        onView(withId(R.id.card_visitor)).perform(click())

        // Espera a que carguen los álbumes (máx 5 s)
        Thread.sleep(5000)

        // Clic en el primer álbum del trending
        onView(withId(R.id.rv_trending))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )
            
        // Espera de seguridad a que cargue el detalle de Retrofit
        Thread.sleep(3000)
    }

    @Test
    fun albumDetail_showsAlbumData() {
        navigateToAlbumDetail()

        // Prueba verifica que los datos del álbum se muestran
        onView(withId(R.id.albumTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.albumArtist)).check(matches(isDisplayed()))
        onView(withId(R.id.albumYear)).check(matches(isDisplayed()))
        onView(withId(R.id.albumDescription)).check(matches(isDisplayed()))
    }

    @Test
    fun albumDetail_showsTracksList() {
        navigateToAlbumDetail()

        // Prueba verifica que la lista de tracks carga y está en pantalla
        onView(withId(R.id.tracksRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun backButton_returnsToAlbumList() {
        navigateToAlbumDetail()

        // Prueba verifica la navegación de regreso (hacia AlbumFragment / Lista de álbumes)
        pressBack()

        // Al regresar, los RecyclerViews de trending o recents deben cargar visiblemente.
        onView(withId(R.id.rv_trending)).check(matches(isDisplayed()))
    }
}
