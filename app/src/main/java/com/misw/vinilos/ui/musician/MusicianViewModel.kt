package com.misw.vinilos.ui.musician

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misw.vinilos.data.model.Musician

class MusicianViewModel : ViewModel() {

    private val _musicians = MutableLiveData<List<Musician>>()
    val musicians: LiveData<List<Musician>> get() = _musicians

    init {
        _musicians.value = listOf(
            Musician(1, "Rubén Blades", ""),
            Musician(2, "Carlos Vives", ""),
            Musician(3, "Shakira", ""),
            Musician(4, "Juanes", ""),
            Musician(5, "Fonseca", ""),
            Musician(6, "ChocQuibTown", "")
        )
    }
}
