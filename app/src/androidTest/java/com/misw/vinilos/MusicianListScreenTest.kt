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

/**
 * Pruebas E2E (End-to-End) para la vista del listado de artistas.
 *
 * Flujo:
 * 1. Inicia en MainActivity (WelcomeScreen por defecto).
 * 2. Selecciona "Usuario Visitante".
 * 3. Navega a "Artistas" usando el Bottom Navigation.
 * 4. Valida carga, datos y scroll.
 */
@RunWith(AndroidJUnit4::class)
class MusicianListScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Función auxiliar para navegar hasta la lista de artistas.
     */
    private fun navigateToMusicianList() {
        // Clic en la tarjeta de Usuario Visitante
        onView(withId(R.id.card_visitor)).perform(click())
        
        // Clic en la opción "Artistas" del Bottom Navigation
        onView(withId(R.id.musicianFragment)).perform(click())

        // Esperar a que la API cargue los datos
        Thread.sleep(3000)
    }

    @Test
    fun e2e_musicianList_loadsGridCorrectly() {
        navigateToMusicianList()

        // Verifica que el grid (RecyclerView) carga y es visible
        onView(withId(R.id.rv_musicians))
            .check(matches(isDisplayed()))
    }

    @Test
    fun e2e_musicianList_cardsShowData() {
        navigateToMusicianList()

        // Verifica que el RecyclerView contiene cards con los elementos requeridos
        onView(withId(R.id.rv_musicians))
            .check(matches(hasDescendant(withId(R.id.tv_musician_name))))
            
        onView(withId(R.id.rv_musicians))
            .check(matches(hasDescendant(withId(R.id.iv_musician_photo))))

        onView(withId(R.id.rv_musicians))
            .check(matches(hasDescendant(withId(R.id.tv_musician_type))))
    }

    @Test
    fun e2e_musicianList_scrollsCorrectly() {
        navigateToMusicianList()

        // Hace scroll hasta la posición 3 del listado (si hay al menos 4 elementos)
        // Esto valida explícitamente que el componente permite el scroll
        onView(withId(R.id.rv_musicians))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))

        // Verifica que después del scroll, el grid siga visible
        onView(withId(R.id.rv_musicians))
            .check(matches(isDisplayed()))
            
        // Verifica que en la vista actual se siga renderizando el nombre de algún artista
        onView(withId(R.id.rv_musicians))
            .check(matches(hasDescendant(withId(R.id.tv_musician_name))))
    }

    @Test
    fun e2e_musicianList_toolbarBack_navigatesToAlbums() {
        onView(withId(R.id.card_visitor)).perform(click())
        onView(withId(R.id.musicianFragment)).perform(click())

        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))

        androidx.test.espresso.Espresso.pressBack()

        onView(withId(R.id.rv_recent))
            .check(matches(isDisplayed()))
    }
}
