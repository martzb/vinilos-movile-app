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
 * Pruebas Espresso para la pantalla de detalle de artista (MusicianDetailFragment).
 *
 * Para llegar a esta pantalla primero se hace click en la tarjeta de Visitante,
 * se navega a la sección de artistas, se espera a que carguen y se selecciona uno.
 */
@RunWith(AndroidJUnit4::class)
class MusicianDetailScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /** Navega hacia la pantalla de detalles de un artista antes de cada test. */
    private fun navigateToMusicianDetail() {
        // Clic en la tarjeta de Usuario Visitante
        onView(withId(R.id.card_visitor)).perform(click())

        // Clic en la opción "Artistas" del Bottom Navigation
        onView(withId(R.id.musicianFragment)).perform(click())

        // Esperar a que la API cargue los datos de la lista
        Thread.sleep(8000)

        // Clic en el primer artista de la lista (Rubén Blades u otro)
        onView(withId(R.id.rv_musicians))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )

        // Espera de seguridad a que cargue el detalle del artista desde Retrofit
        Thread.sleep(5000)
    }

    @Test
    fun musicianDetail_showsMusicianData() {
        navigateToMusicianDetail()

        // Prueba verifica que los datos del artista se muestran
        onView(withId(R.id.tvName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.tvBirthDate)).check(matches(isDisplayed()))
        onView(withId(R.id.ivPhoto)).check(matches(isDisplayed()))
    }

    @Test
    fun musicianDetail_showsAlbumsSection() {
        navigateToMusicianDetail()

        // Prueba verifica que la sección de álbumes (el contenedor RecyclerView) está en pantalla
        onView(withId(R.id.rvAlbums)).check(matches(isDisplayed()))
    }

    @Test
    fun backButton_returnsToMusicianList() {
        navigateToMusicianDetail()

        // Prueba verifica la navegación de regreso (hacia MusicianFragment / Lista de artistas)
        pressBack()

        // Al regresar, el RecyclerView de músicos debe cargar visiblemente.
        onView(withId(R.id.rv_musicians)).check(matches(isDisplayed()))
    }
}
