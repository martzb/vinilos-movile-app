package com.misw.vinilos

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.ViewAction
import androidx.test.espresso.UiController
import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom

@RunWith(AndroidJUnit4::class)
class AlbumCreateScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private fun navigateToCreateAlbum() {
        onView(withId(R.id.card_collector)).perform(click())
        Thread.sleep(4000)
        onView(withId(R.id.fab_add)).perform(click())
        Thread.sleep(2000)
    }

    // Acción auxiliar para simular el tag ISO que el DatePicker guarda
    private fun setTagValue(value: String): ViewAction {
        return object : ViewAction {
            override fun getConstraints() = isAssignableFrom(View::class.java)
            override fun getDescription() = "Set tag value"
            override fun perform(uiController: UiController?, view: View?) {
                view?.tag = value
            }
        }
    }

    @Test
    fun emptyFieldsShowErrors() {
        navigateToCreateAlbum()
        onView(withId(R.id.btnSave)).perform(click())

        onView(withId(R.id.tilAlbumName))
            .check(matches(hasDescendant(withText("Campo requerido"))))
        onView(withId(R.id.tilArtist))
            .check(matches(hasDescendant(withText("Campo requerido"))))
        onView(withId(R.id.tilReleaseDate))
            .check(matches(hasDescendant(withText("Campo requerido"))))
        onView(withId(R.id.tilRecordLabel))
            .check(matches(hasDescendant(withText("Campo requerido"))))
        onView(withId(R.id.tilGenre))
            .check(matches(hasDescendant(withText("Campo requerido"))))
        onView(withId(R.id.tilDescription))
            .check(matches(hasDescendant(withText("Campo requerido"))))
    }

    @Test
    fun invalidDateShowsError() {
        navigateToCreateAlbum()

        onView(withId(R.id.etAlbumName))
            .perform(scrollTo(), replaceText("Nuevo álbum"), closeSoftKeyboard())
        onView(withId(R.id.actvArtist))
            .perform(scrollTo(), replaceText("Shakira"), closeSoftKeyboard())
        onView(withId(R.id.etReleaseDate))
            .perform(scrollTo(), replaceText("2026-03-16"), closeSoftKeyboard()) // formato incorrecto
        onView(withId(R.id.actvRecordLabel))
            .perform(scrollTo(), replaceText("Sony Music"), closeSoftKeyboard())
        onView(withId(R.id.actvGenre))
            .perform(scrollTo(), replaceText("Rock"), closeSoftKeyboard())
        onView(withId(R.id.etDescription))
            .perform(scrollTo(), replaceText("Descripción de prueba"), closeSoftKeyboard())

        onView(withId(R.id.btnSave)).perform(click())

        onView(withText("Selecciona una fecha válida"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun validAlbumShowsSuccessDialog() {
        navigateToCreateAlbum()

        onView(withId(R.id.etAlbumName))
            .perform(scrollTo(), replaceText("Nuevo álbum"), closeSoftKeyboard())
        onView(withId(R.id.actvArtist))
            .perform(scrollTo(), replaceText("Shakira"), closeSoftKeyboard())

        onView(withId(R.id.etReleaseDate))
            .perform(scrollTo(), replaceText("16/05/2026"), closeSoftKeyboard())
        onView(withId(R.id.etReleaseDate))
            .perform(setTagValue("2026-05-16T00:00:00.000Z"))

        onView(withId(R.id.actvRecordLabel))
            .perform(scrollTo(), replaceText("Sony Music"), closeSoftKeyboard())
        onView(withId(R.id.actvGenre))
            .perform(scrollTo(), replaceText("Rock"), closeSoftKeyboard())
        onView(withId(R.id.etDescription))
            .perform(scrollTo(), replaceText("Descripción de prueba"), closeSoftKeyboard())

        onView(withId(R.id.btnSave)).perform(click())

        Thread.sleep(2000)

        onView(withText("Ver álbum →"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun missingDescriptionShowsError() {
        navigateToCreateAlbum()

        onView(withId(R.id.etAlbumName))
            .perform(scrollTo(), replaceText("Álbum sin descripción"), closeSoftKeyboard())
        onView(withId(R.id.actvArtist))
            .perform(scrollTo(), replaceText("Shakira"), closeSoftKeyboard())
        onView(withId(R.id.etReleaseDate))
            .perform(scrollTo(), replaceText("16/05/2026"), closeSoftKeyboard())
        onView(withId(R.id.etReleaseDate))
            .perform(setTagValue("2026-05-16T00:00:00.000Z"))
        onView(withId(R.id.actvRecordLabel))
            .perform(scrollTo(), replaceText("Sony Music"), closeSoftKeyboard())
        onView(withId(R.id.actvGenre))
            .perform(scrollTo(), replaceText("Rock"), closeSoftKeyboard())

        // No llenamos descripción
        onView(withId(R.id.btnSave)).perform(click())

        onView(withId(R.id.tilDescription))
            .check(matches(hasDescendant(withText("Campo requerido"))))
    }

}
