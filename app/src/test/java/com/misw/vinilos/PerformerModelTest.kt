package com.misw.vinilos

import com.misw.vinilos.data.model.Performer
import com.misw.vinilos.data.model.Track
import org.junit.Assert.*
import org.junit.Test

class PerformerModelTest {

    @Test
    fun `performer tiene valores correctos`() {
        val performer = Performer(1, "Rubén Blades", "", "Cantante")
        assertEquals(1, performer.id)
        assertEquals("Rubén Blades", performer.name)
        assertEquals("Cantante", performer.description)
    }

    @Test
    fun `performer sin birthDate tiene null por defecto`() {
        val performer = Performer(1, "Rubén Blades", "", "Cantante")
        assertNull(performer.birthDate)
        assertNull(performer.creationDate)
    }

    @Test
    fun `performer con creationDate retorna la fecha correcta`() {
        val performer = Performer(1, "Soda Stereo", "", "Banda", creationDate = "1982-01-01")
        assertEquals("1982-01-01", performer.creationDate)
    }

    @Test
    fun `dos performers con mismo id y datos son iguales`() {
        val p1 = Performer(1, "Rubén Blades", "", "Cantante")
        val p2 = Performer(1, "Rubén Blades", "", "Cantante")
        assertEquals(p1, p2)
    }

    @Test
    fun `track tiene valores correctos`() {
        val track = Track(1, "Decisiones", "4:52")
        assertEquals(1, track.id)
        assertEquals("Decisiones", track.name)
        assertEquals("4:52", track.duration)
    }

    @Test
    fun `dos tracks con mismo id y datos son iguales`() {
        val t1 = Track(1, "Decisiones", "4:52")
        val t2 = Track(1, "Decisiones", "4:52")
        assertEquals(t1, t2)
    }

    @Test
    fun `dos tracks con distinto nombre no son iguales`() {
        val t1 = Track(1, "Decisiones", "4:52")
        val t2 = Track(1, "El Padre Antonio", "5:10")
        assertNotEquals(t1, t2)
    }
}
