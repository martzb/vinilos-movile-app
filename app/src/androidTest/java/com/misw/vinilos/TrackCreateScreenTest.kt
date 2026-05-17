package com.misw.vinilos

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackCreateScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private fun navigateToCreateTrack() {
        // Clic en la tarjeta de Visitante
        onView(withId(R.id.card_visitor)).perform(click())
        Thread.sleep(5000)

        // Clic en el primer álbum
        onView(withId(R.id.rv_trending))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )
        Thread.sleep(3000)

        // Clic en botón "Agregar Track"
        onView(withId(R.id.fabAddTrack)).perform(click())
        Thread.sleep(2000)
    }

    @Test
    fun emptyNameShowsError() {
        navigateToCreateTrack()

        onView(withId(R.id.etTrackDuration))
            .perform(replaceText("03:45"), closeSoftKeyboard())

        onView(withId(R.id.btnSubmit)).perform(click())

        onView(withId(R.id.etTrackName))
            .check(matches(hasErrorText("Campo requerido")))
    }

    @Test
    fun invalidDurationShowsError() {
        navigateToCreateTrack()

        onView(withId(R.id.etTrackName))
            .perform(replaceText("Canción de prueba"), closeSoftKeyboard())

        onView(withId(R.id.etTrackDuration))
            .perform(replaceText("345"), closeSoftKeyboard())

        onView(withId(R.id.btnSubmit)).perform(click())

        onView(withId(R.id.etTrackDuration))
            .check(matches(hasErrorText("Formato inválido (mm:ss)")))
    }

    @Test
    fun validTrackShowsSuccessDialog() {
        navigateToCreateTrack()

        onView(withId(R.id.etTrackName))
            .perform(replaceText("Mi canción"), closeSoftKeyboard())

        onView(withId(R.id.etTrackDuration))
            .perform(replaceText("03:45"), closeSoftKeyboard())

        onView(withId(R.id.btnSubmit)).perform(click())

        Thread.sleep(1500)

        onView(withText("Ver álbum →"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun recyclerViewShowsAddedTrack() {
        navigateToCreateTrack()

        onView(withId(R.id.etTrackName))
            .perform(replaceText("Track en lista"), closeSoftKeyboard())

        onView(withId(R.id.etTrackDuration))
            .perform(replaceText("02:30"), closeSoftKeyboard())

        onView(withId(R.id.btnSubmit)).perform(click())

        Thread.sleep(3000)

        // Verifica que el diálogo de éxito aparece: el track fue creado exitosamente
        onView(withText("Ver álbum →"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        // Verifica que el contador de tracks se actualizó (visible detrás del diálogo)
        onView(withId(R.id.tvTracksCount))
            .check(matches(withText("1 tracks")))
    }
}
