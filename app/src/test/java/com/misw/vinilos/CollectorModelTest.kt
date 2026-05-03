package com.misw.vinilos

import com.misw.vinilos.data.model.Collector
import org.junit.Assert.*
import org.junit.Test

class CollectorModelTest {

    private fun makeCollector(
        id: Int = 1,
        name: String = "Manolo Bellon",
        email: String = "manolo@gmail.com"
    ) = Collector(id = id, name = name, email = email)

    @Test
    fun `collector tiene valores correctos`() {
        val collector = makeCollector()
        assertEquals(1, collector.id)
        assertEquals("Manolo Bellon", collector.name)
        assertEquals("manolo@gmail.com", collector.email)
    }

    @Test
    fun `dos collectors con mismo id y datos son iguales`() {
        val c1 = makeCollector(id = 1, name = "Manolo Bellon")
        val c2 = makeCollector(id = 1, name = "Manolo Bellon")
        assertEquals(c1, c2)
    }

    @Test
    fun `dos collectors con distinto id no son iguales`() {
        val c1 = makeCollector(id = 1)
        val c2 = makeCollector(id = 2)
        assertNotEquals(c1, c2)
    }

    @Test
    fun `collector con email distinto no es igual`() {
        val c1 = makeCollector(email = "a@gmail.com")
        val c2 = makeCollector(email = "b@gmail.com")
        assertNotEquals(c1, c2)
    }

    @Test
    fun `collector toString contiene el nombre`() {
        val collector = makeCollector(name = "Manolo Bellon")
        assertTrue(collector.toString().contains("Manolo Bellon"))
    }
}
