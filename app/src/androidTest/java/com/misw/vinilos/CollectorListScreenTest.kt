package com.misw.vinilos

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.misw.vinilos.ui.collector.CollectorFragment
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas Espresso para la pantalla de lista de coleccionistas (CollectorFragment).
 *
 * Verifica que los elementos de la card del coleccionista se muestren correctamente
 * (foto, nombre y email).
 */
@RunWith(AndroidJUnit4::class)
class CollectorListScreenTest {

    @Test
    fun collectorList_showsRecyclerView() {
        launchFragmentInContainer<CollectorFragment>(themeResId = R.style.Theme_Vinilos)

        onView(withId(R.id.rv_collectors))
            .check(matches(isDisplayed()))
    }

    @Test
    fun collectorList_cardShowsData() {
        launchFragmentInContainer<CollectorFragment>(themeResId = R.style.Theme_Vinilos)
        
        // Espera a que carguen los coleccionistas de la API
        Thread.sleep(5000)

        // Verifica que el RecyclerView contiene una card con el TextView del nombre
        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.tv_collector_name))))
            
        // Verifica que el RecyclerView contiene una card con el ImageView del avatar
        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.iv_collector_avatar))))

        // Verifica que el RecyclerView contiene una card con el TextView del email
        onView(withId(R.id.rv_collectors))
            .check(matches(hasDescendant(withId(R.id.tv_collector_email))))
    }
}
