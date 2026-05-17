package com.misw.vinilos

import com.misw.vinilos.data.model.TrackRequest
import org.junit.Assert.*
import org.junit.Test

class TrackRequestModelTest {

    private fun make(name: String = "Decisiones", duration: String = "04:52") =
        TrackRequest(name = name, duration = duration)

    @Test
    fun `trackRequest tiene valores correctos`() {
        val tr = make(name = "Mi canción", duration = "03:30")
        assertEquals("Mi canción", tr.name)
        assertEquals("03:30", tr.duration)
    }

    @Test
    fun `dos trackRequest con mismos datos son iguales`() {
        assertEquals(make(), make())
    }

    @Test
    fun `dos trackRequest con distinto name no son iguales`() {
        assertNotEquals(make(name = "Canción A"), make(name = "Canción B"))
    }

    @Test
    fun `trackRequest toString contiene el nombre`() {
        assertTrue(make(name = "Decisiones").toString().contains("Decisiones"))
    }
}
