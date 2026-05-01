package com.misw.vinilos.ui.musician

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.repository.MusicianRepository
import kotlinx.coroutines.launch

class MusicianDetailViewModel : ViewModel() {

    private val repository = MusicianRepository()

    private val _musician = MutableLiveData<Musician>()
    val musician: LiveData<Musician> get() = _musician

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadMusicianDetail(musicianId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getMusician(musicianId)
                _musician.value = result
            } catch (e: Exception) {
                _error.value = "No se pudo cargar el detalle del artista. Verifica tu conexión."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
