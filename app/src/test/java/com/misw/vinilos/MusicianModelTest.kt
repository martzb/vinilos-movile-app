package com.misw.vinilos

import com.misw.vinilos.data.model.Musician
import org.junit.Assert.*
import org.junit.Test

class MusicianModelTest {

    private fun makeMusician(
        id: Int = 1,
        name: String = "Rubén Blades",
        birthDate: String? = null
    ) = Musician(
        id = id,
        name = name,
        image = "https://example.com/image.jpg",
        description = "Cantante panameño",
        birthDate = birthDate
    )

    @Test
    fun `musician tiene valores correctos`() {
        val musician = makeMusician(id = 1, name = "Rubén Blades")
        assertEquals(1, musician.id)
        assertEquals("Rubén Blades", musician.name)
    }

    @Test
    fun `musician sin birthDate tiene null por defecto`() {
        val musician = makeMusician()
        assertNull(musician.birthDate)
    }

    @Test
    fun `musician con birthDate retorna la fecha correcta`() {
        val musician = makeMusician(birthDate = "1948-07-16T00:00:00.000Z")
        assertEquals("1948-07-16T00:00:00.000Z", musician.birthDate)
    }

    @Test
    fun `musician sin albums tiene lista vacía por defecto`() {
        val musician = makeMusician()
        assertTrue(musician.albums.isEmpty())
    }

    @Test
    fun `dos musicians con mismo id son iguales`() {
        val m1 = makeMusician(id = 1, name = "Rubén Blades")
        val m2 = makeMusician(id = 1, name = "Rubén Blades")
        assertEquals(m1, m2)
    }

    @Test
    fun `dos musicians con distinto id no son iguales`() {
        val m1 = makeMusician(id = 1)
        val m2 = makeMusician(id = 2)
        assertNotEquals(m1, m2)
    }
}
