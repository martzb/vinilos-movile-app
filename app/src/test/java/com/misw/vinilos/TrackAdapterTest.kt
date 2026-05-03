package com.misw.vinilos

import com.misw.vinilos.data.model.Track
import com.misw.vinilos.ui.album.TrackAdapter
import org.junit.Assert.*
import org.junit.Test

class TrackAdapterTest {

    private val diffCallback = TrackAdapter.TrackDiffCallback()

    @Test
    fun `areItemsTheSame retorna true cuando los ids son iguales`() {
        val t1 = Track(1, "Decisiones", "4:52")
        val t2 = Track(1, "Otro nombre", "3:00")
        assertTrue(diffCallback.areItemsTheSame(t1, t2))
    }

    @Test
    fun `areItemsTheSame retorna false cuando los ids son distintos`() {
        val t1 = Track(1, "Decisiones", "4:52")
        val t2 = Track(2, "Decisiones", "4:52")
        assertFalse(diffCallback.areItemsTheSame(t1, t2))
    }

    @Test
    fun `areContentsTheSame retorna true cuando los tracks son iguales`() {
        val t1 = Track(1, "Decisiones", "4:52")
        val t2 = Track(1, "Decisiones", "4:52")
        assertTrue(diffCallback.areContentsTheSame(t1, t2))
    }

    @Test
    fun `areContentsTheSame retorna false cuando los tracks difieren`() {
        val t1 = Track(1, "Decisiones", "4:52")
        val t2 = Track(1, "El Padre Antonio", "5:10")
        assertFalse(diffCallback.areContentsTheSame(t1, t2))
    }
}
