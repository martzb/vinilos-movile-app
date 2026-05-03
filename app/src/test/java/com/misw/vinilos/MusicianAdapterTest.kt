package com.misw.vinilos

import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.ui.musician.MusicianAdapter
import org.junit.Assert.*
import org.junit.Test

class MusicianAdapterTest {

    private val diffCallback = MusicianAdapter.DiffCallback()

    @Test
    fun `areItemsTheSame retorna true cuando los ids son iguales`() {
        val m1 = Musician(1, "Rubén Blades", "", "Cantante")
        val m2 = Musician(1, "Otro Nombre", "", "Otro")
        assertTrue(diffCallback.areItemsTheSame(m1, m2))
    }

    @Test
    fun `areItemsTheSame retorna false cuando los ids son distintos`() {
        val m1 = Musician(1, "Rubén Blades", "", "Cantante")
        val m2 = Musician(2, "Rubén Blades", "", "Cantante")
        assertFalse(diffCallback.areItemsTheSame(m1, m2))
    }

    @Test
    fun `areContentsTheSame retorna true cuando los musicians son iguales`() {
        val m1 = Musician(1, "Rubén Blades", "", "Cantante", "1948-07-16")
        val m2 = Musician(1, "Rubén Blades", "", "Cantante", "1948-07-16")
        assertTrue(diffCallback.areContentsTheSame(m1, m2))
    }

    @Test
    fun `areContentsTheSame retorna false cuando difieren en nombre`() {
        val m1 = Musician(1, "Rubén Blades", "", "Cantante")
        val m2 = Musician(1, "Carlos Vives", "", "Cantante")
        assertFalse(diffCallback.areContentsTheSame(m1, m2))
    }

    @Test
    fun `areContentsTheSame retorna false cuando difieren en birthDate`() {
        val m1 = Musician(1, "Rubén Blades", "", "Cantante", "1948-07-16")
        val m2 = Musician(1, "Rubén Blades", "", "Cantante", null)
        assertFalse(diffCallback.areContentsTheSame(m1, m2))
    }
}
