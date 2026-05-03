package com.misw.vinilos

import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.ui.collector.CollectorAdapter
import org.junit.Assert.*
import org.junit.Test

class CollectorAdapterTest {

    private val diffCallback = CollectorAdapter.DiffCallback()

    @Test
    fun `areItemsTheSame retorna true cuando los ids son iguales`() {
        val c1 = Collector(1, "Manolo Bellon", "manolo@gmail.com")
        val c2 = Collector(1, "Otro Nombre", "otro@gmail.com")
        assertTrue(diffCallback.areItemsTheSame(c1, c2))
    }

    @Test
    fun `areItemsTheSame retorna false cuando los ids son distintos`() {
        val c1 = Collector(1, "Manolo Bellon", "manolo@gmail.com")
        val c2 = Collector(2, "Manolo Bellon", "manolo@gmail.com")
        assertFalse(diffCallback.areItemsTheSame(c1, c2))
    }

    @Test
    fun `areContentsTheSame retorna true cuando los collectors son iguales`() {
        val c1 = Collector(1, "Manolo Bellon", "manolo@gmail.com")
        val c2 = Collector(1, "Manolo Bellon", "manolo@gmail.com")
        assertTrue(diffCallback.areContentsTheSame(c1, c2))
    }

    @Test
    fun `areContentsTheSame retorna false cuando difieren en nombre`() {
        val c1 = Collector(1, "Manolo Bellon", "manolo@gmail.com")
        val c2 = Collector(1, "Jaime Zuluaga", "manolo@gmail.com")
        assertFalse(diffCallback.areContentsTheSame(c1, c2))
    }

    @Test
    fun `areContentsTheSame retorna false cuando difieren en email`() {
        val c1 = Collector(1, "Manolo Bellon", "manolo@gmail.com")
        val c2 = Collector(1, "Manolo Bellon", "otro@gmail.com")
        assertFalse(diffCallback.areContentsTheSame(c1, c2))
    }
}
