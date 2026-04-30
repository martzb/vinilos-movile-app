package com.misw.vinilos

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.misw.vinilos.ui.musician.MusicianFragment
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas Espresso para la pantalla de lista de artistas (MusicianFragment).
 *
 * Verifica que los elementos de la card del artista se muestren correctamente
 * (foto, nombre, y tipo de artista).
 */
@RunWith(AndroidJUnit4::class)
class MusicianListScreenTest {

    @Test
    fun musicianList_showsRecyclerView() {
        launchFragmentInContainer<MusicianFragment>(themeResId = R.style.Theme_Vinilos)

        onView(withId(R.id.rv_musicians))
            .check(matches(isDisplayed()))
    }

    @Test
    fun musicianList_cardShowsData() {
        launchFragmentInContainer<MusicianFragment>(themeResId = R.style.Theme_Vinilos)
        
        // Espera a que carguen los artistas de la API
        Thread.sleep(5000)

        // Verifica que el RecyclerView contiene una card con el TextView del nombre
        onView(withId(R.id.rv_musicians))
            .check(matches(hasDescendant(withId(R.id.tv_musician_name))))
            
        // Verifica que el RecyclerView contiene una card con el ImageView de la foto
        onView(withId(R.id.rv_musicians))
            .check(matches(hasDescendant(withId(R.id.iv_musician_photo))))

        // Verifica que el RecyclerView contiene una card con el TextView del tipo (Banda/Músico)
        onView(withId(R.id.rv_musicians))
            .check(matches(hasDescendant(withId(R.id.tv_musician_type))))
    }
}
