package com.misw.vinilos

import com.misw.vinilos.data.model.CollectorAlbum
import org.junit.Assert.*
import org.junit.Test

class CollectorAlbumModelTest {

    private fun make(id: Int = 1, price: Int = 100, status: String = "Active") =
        CollectorAlbum(id = id, price = price, status = status)

    @Test
    fun `collectorAlbum tiene valores correctos`() {
        val ca = make(id = 3, price = 250, status = "Inactive")
        assertEquals(3, ca.id)
        assertEquals(250, ca.price)
        assertEquals("Inactive", ca.status)
    }

    @Test
    fun `dos collectorAlbum con mismos datos son iguales`() {
        assertEquals(make(), make())
    }

    @Test
    fun `dos collectorAlbum con distinto id no son iguales`() {
        assertNotEquals(make(id = 1), make(id = 2))
    }

    @Test
    fun `collectorAlbum toString contiene el id`() {
        assertTrue(make(id = 7).toString().contains("7"))
    }
}
